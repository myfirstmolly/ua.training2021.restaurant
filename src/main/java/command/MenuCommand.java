package command;

import database.DBManager;
import entities.Category;
import entities.Dish;
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
import java.util.List;

public class MenuCommand implements Command {

    private static final Logger logger = LogManager.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        DishService dishService = new DishServiceImpl(DBManager.getInstance());
        CategoryService categoryService = new CategoryServiceImpl(DBManager.getInstance());
        int pageIndex = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null)
            pageIndex = Integer.parseInt(pageParam);

        dishService.findAll(pageIndex);
        logger.trace("executing menu command");
        List<Category> categories = categoryService.findAll();
        Page<Dish> dishes = dishService.findAll(1);
        request.setAttribute("categories", categories);
        request.setAttribute("dishes", dishes);
        request.setAttribute("isUser", true);
        request.setAttribute("isAdmin", false);
        return WebPages.MENU_PAGE;
    }
}
