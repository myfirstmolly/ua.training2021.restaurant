package controller.command;

import model.dao.DaoFactory;
import model.entities.Category;
import model.entities.Dish;
import model.entities.User;
import model.service.CategoryService;
import model.service.DishService;
import model.service.impl.CategoryServiceImpl;
import model.service.impl.DishServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.Page;
import util.WebPages;

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
        String locale = (String) request.getSession().getAttribute("lang");
        List<Category> categories = categoryService.findAll(locale);
        request.setAttribute("categories", categories);
        Page<Dish> dishes = getPage(request);
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

    private Page<Dish> getPage(HttpServletRequest request) {
        int pageIndex = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            if (Integer.parseInt(pageParam) > 1)
                pageIndex = Integer.parseInt(pageParam);
            request.getSession().setAttribute("menuPage", pageIndex);
        }
        return getDishPage(request, pageIndex);
    }

    private Page<Dish> getDishPage(HttpServletRequest request, int pageIndex) {
        HttpSession session = request.getSession();
        String orderBy = request.getParameter("orderBy");
        String category = request.getParameter("category");

        if (category != null && category.equals("all")) {
            session.removeAttribute("orderBy");
            session.removeAttribute("category");
            return dishService.findAll(pageIndex);
        }

        setSessionAttributes(session, orderBy, category);

        if (session.getAttribute("category") != null) {
            int categoryId = (Integer) session.getAttribute("category");
            logger.debug("retrieved session attribute with category id for user");
            return dishService.findAllByCategoryId(categoryId, pageIndex);
        }

        if (session.getAttribute("orderBy") != null) {
            orderBy = (String) session.getAttribute("orderBy");
            logger.debug("retrieved session attribute with orderBy value for user");
            return dishService.findAllOrderBy(orderBy, pageIndex, (String) request.getSession().getAttribute("lang"));
        }

        return dishService.findAll(pageIndex);
    }

    private void setSessionAttributes(HttpSession session, String orderBy, String category) {
        if (category != null) {
            int categoryId = Integer.parseInt(category);
            session.setAttribute("category", categoryId);
            logger.debug("set session attribute with category id for user");
            session.removeAttribute("orderBy");
        }

        if (orderBy != null) {
            session.setAttribute("orderBy", orderBy);
            logger.debug("set session attribute with orderBy value for user");
            session.removeAttribute("category");
        }
    }

}
