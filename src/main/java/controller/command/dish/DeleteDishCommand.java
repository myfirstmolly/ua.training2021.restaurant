package controller.command.dish;

import controller.command.Command;
import model.dao.DaoFactory;
import model.service.DishService;
import model.service.impl.DishServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * deletes dish from model.database.
 * available to manager only.
 */
public class DeleteDishCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteDishCommand.class);
    private final DishService dishService = new DishServiceImpl(DaoFactory.getDishDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing delete dish command-----");
        String dishStr = request.getParameter("dish");
        int id = Integer.parseInt(dishStr);
        dishService.deleteById(id);
        logger.debug("-----successfully executed delete dish command-----");
        return WebPages.DEFAULT_PAGE;
    }
}
