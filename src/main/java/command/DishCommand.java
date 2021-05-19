package command;

import database.DBManager;
import entities.Dish;
import entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.DishService;
import service.impl.DishServiceImpl;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DishCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DishCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing dish command-----");
        int id = Integer.parseInt(request.getParameter("id"));
        DishService dishService = new DishServiceImpl(DBManager.getInstance());
        Dish dish = dishService.findById(id).get();
        User user = (User) request.getSession().getAttribute("user");
        if (user != null)
            request.setAttribute("role", user.getRole());
        request.setAttribute("dish", dish);
        logger.debug("-----successfully executed dish command-----");
        return WebPages.DISH_PAGE;
    }
}
