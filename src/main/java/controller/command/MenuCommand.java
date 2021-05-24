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
        int pageIndex = (int) request.getSession().getAttribute("menuPage");
        HttpSession session = request.getSession();

        if (session.getAttribute("categoryId") != null) {
            int categoryId = (Integer) session.getAttribute("categoryId");
            logger.debug("retrieved session attribute with category id for user");
            return dishService.findAllByCategoryId(categoryId, pageIndex);
        }

        if (session.getAttribute("orderBy") != null) {
            String orderBy = (String) session.getAttribute("orderBy");
            logger.debug("retrieved session attribute with orderBy value for user");
            return dishService.findAllOrderBy(orderBy, pageIndex, (String) request.getSession().getAttribute("lang"));
        }

        return dishService.findAll(pageIndex);
    }
}
