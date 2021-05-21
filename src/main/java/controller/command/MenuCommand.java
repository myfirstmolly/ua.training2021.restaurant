package controller.command;

import model.database.DaoFactory;
import model.entities.Category;
import model.entities.Dish;
import model.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.service.CategoryService;
import model.service.DishService;
import model.service.impl.CategoryServiceImpl;
import model.service.impl.DishServiceImpl;
import util.Page;
import util.WebPages;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * returns menu page.
 * available to all users.
 */
public class MenuCommand implements Command {

    private static final Logger logger = LogManager.getLogger(MenuCommand.class);
    private final DishService dishService = new DishServiceImpl(DaoFactory.getDishDao());
    private final CategoryService categoryService = new CategoryServiceImpl(DaoFactory.getCategoryDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing menu command-----");
        List<Category> categories = categoryService.findAll();
        request.setAttribute("categories", categories);
        Page<Dish> dishes = getPage(request, response);
        request.setAttribute("dishes", dishes);
        setRoleAttributes(request);
        logger.debug("-----successfully executed menu command-----");
        return WebPages.MENU_PAGE;
    }

    private void setRoleAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null)
            return;
        request.setAttribute("role", user.getRole());
    }

    private Page<Dish> getPage(HttpServletRequest request, HttpServletResponse response) {
        int pageIndex = 1;
        String pageParam = request.getParameter("page");
        String orderBy = request.getParameter("orderBy");
        String category = request.getParameter("category");
        if (pageParam != null)
            pageIndex = Integer.parseInt(pageParam);
        if (pageIndex <= 1)
            pageIndex = 1;
        return getDishPageWithCookies(request, response, pageIndex, orderBy, category);
    }

    private Page<Dish> getDishPageWithCookies(HttpServletRequest request, HttpServletResponse response,
                                              int pageIndex, String orderBy, String category) {
        int categoryId;
        Cookie[] cookies = request.getCookies();

        if (category != null && category.equals("all")) {
            dropCookie(cookies, "orderBy", response);
            dropCookie(cookies, "category", response);
            return dishService.findAll(pageIndex);
        }

        if (category != null) {
            categoryId = Integer.parseInt(category);
            response.addCookie(new Cookie("category", String.valueOf(categoryId)));
            logger.debug("created cookie with category id for user");
            dropCookie(cookies, "orderBy", response);
            return dishService.findAllByCategoryId(categoryId, pageIndex);
        }

        if (orderBy != null) {
            response.addCookie(new Cookie("orderBy", orderBy));
            logger.debug("created cookie with orderBy value for user");
            dropCookie(cookies, "category", response);
            return dishService.findAllOrderBy(orderBy, pageIndex);
        }

        if (getCookie("category", cookies) != null) {
            categoryId = Integer.parseInt(getCookie("category", cookies).getValue());
            logger.debug("retrieved cookie with category id for user");
            return dishService.findAllByCategoryId(categoryId, pageIndex);
        }

        if (getCookie("orderBy", cookies) != null) {
            orderBy = getCookie("orderBy", cookies).getValue();
            logger.debug("retrieved cookie with orderBy value for user");
            return dishService.findAllOrderBy(orderBy, pageIndex);
        }

        return dishService.findAll(pageIndex);
    }

    private void dropCookie(Cookie[] cookies, String name, HttpServletResponse response) {
        for (Cookie c : cookies) {
            if (c.getName().equals(name)) {
                c.setMaxAge(0);
                response.addCookie(c);
            }
        }
    }

    private Cookie getCookie(String name, Cookie[] cookies) {
        if (cookies == null || cookies.length == 0)
            return null;
        for (Cookie c : cookies) {
            if (c.getName().equals(name))
                return c;
        }
        return null;
    }

}
