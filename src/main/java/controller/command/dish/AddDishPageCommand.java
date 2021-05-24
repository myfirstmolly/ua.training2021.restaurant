package controller.command.dish;

import controller.command.Command;
import model.dao.DaoFactory;
import model.service.CategoryService;
import model.service.impl.CategoryServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * returns page add-dish.jsp.
 * available to manager only.
 */
public class AddDishPageCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddDishPageCommand.class);
    private final CategoryService categoryService = new CategoryServiceImpl(DaoFactory.getCategoryDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing add dish page command-----");
        String locale = (String) request.getSession().getAttribute("lang");
        request.setAttribute("categories", categoryService.findAll(locale));
        logger.debug("-----successfully executed add dish page command-----");
        return WebPages.ADD_DISH_PAGE;
    }
}
