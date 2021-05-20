package command;

import database.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.CategoryService;
import service.impl.CategoryServiceImpl;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * returns page add-dish.jsp.
 * available to manager only.
 */
public class AddDishPageCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddDishPageCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing add dish page command-----");
        CategoryService categoryService = new CategoryServiceImpl(DBManager.getInstance());
        request.setAttribute("categories", categoryService.findAll());
        logger.debug("-----successfully executed add dish page command-----");
        return WebPages.ADD_DISH_PAGE;
    }
}
