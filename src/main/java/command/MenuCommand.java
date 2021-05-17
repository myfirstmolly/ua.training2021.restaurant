package command;

import database.DBManager;
import entities.Category;
import entities.Dish;
import entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.CategoryService;
import service.DishService;
import service.impl.CategoryServiceImpl;
import service.impl.DishServiceImpl;
import util.Page;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class MenuCommand implements Command {

    private static final Logger logger = LogManager.getLogger(LoginCommand.class);
    private final DishService dishService = new DishServiceImpl(DBManager.getInstance());
    private final CategoryService categoryService = new CategoryServiceImpl(DBManager.getInstance());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("executing menu command");
        List<Category> categories = categoryService.findAll();
        request.setAttribute("categories", categories);
        Page<Dish> dishes = getPage(request);
        request.setAttribute("dishes", dishes);
        setRoleAttributes(request);
        return WebPages.MENU_PAGE;
    }

    private void setRoleAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) return;
        request.setAttribute("role", user.getRole());
    }

    private Page<Dish> getPage(HttpServletRequest request) {
        int pageIndex = 1;
        String pageParam = request.getParameter("page");
        String orderBy = request.getParameter("orderBy");
        String category = request.getParameter("category");
        if (pageParam != null)
            pageIndex = Integer.parseInt(pageParam);
        int categoryId;

        if (orderBy == null) {
            if (category == null) {
                return dishService.findAll(pageIndex);
            }
            categoryId = Integer.parseInt(category);
            return dishService.findAllByCategoryId(categoryId, pageIndex);
        }
        return dishService.findAllOrderBy(pageIndex, orderBy);
    }
}
