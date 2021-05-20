package command;

import dao.DishDao;
import dao.impl.DishDaoImpl;
import database.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * deletes dish from database.
 * available to manager only.
 */
public class DeleteDishCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteDishCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing delete dish command-----");
        String dishStr = request.getParameter("dish");
        int id = Integer.parseInt(dishStr);
        DishDao dishDao = new DishDaoImpl(DBManager.getInstance());
        dishDao.deleteById(id);
        logger.debug("-----successfully executed delete dish command-----");
        return WebPages.DEFAULT_PAGE;
    }
}
