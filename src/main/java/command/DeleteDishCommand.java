package command;

import dao.DishDao;
import dao.impl.DishDaoImpl;
import database.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteDishCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteDishCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("executing delete dish command");
        String dishStr = request.getParameter("dish");
        int id = Integer.parseInt(dishStr);
        DishDao dishDao = new DishDaoImpl(DBManager.getInstance());
        dishDao.deleteById(id);
        return WebPages.DEFAULT_PAGE;
    }
}
