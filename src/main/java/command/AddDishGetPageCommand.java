package command;

import database.DBManager;
import service.CategoryService;
import service.impl.CategoryServiceImpl;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddDishGetPageCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        CategoryService categoryService = new CategoryServiceImpl(DBManager.getInstance());
        request.setAttribute("categories", categoryService.findAll());
        return WebPages.ADD_DISH_PAGE;
    }
}
