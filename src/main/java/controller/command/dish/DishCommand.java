package controller.command.dish;

import controller.command.Command;
import model.dao.DaoFactory;
import model.entities.Dish;
import model.entities.User;
import model.exceptions.ObjectNotFoundException;
import model.service.DishService;
import model.service.impl.DishServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * returns dish page.
 * available to all users.
 */
public class DishCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DishCommand.class);
    private final DishService dishService = new DishServiceImpl(DaoFactory.getDishDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing dish command-----");
        int id = Integer.parseInt(request.getParameter("id"));
        Dish dish = dishService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("dish not found"));
        User user = (User) request.getSession().getAttribute("user");
        if (user != null)
            request.setAttribute("role", user.getRole());
        request.setAttribute("dish", dish);
        logger.debug("-----successfully executed dish command-----");
        return WebPages.DISH_PAGE;
    }
}
