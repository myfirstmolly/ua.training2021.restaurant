package controller.command.cart;

import controller.command.Command;
import model.dao.DaoFactory;
import model.entities.Dish;
import model.entities.User;
import model.exceptions.ObjectNotFoundException;
import model.service.DishService;
import model.service.RequestService;
import model.service.impl.DishServiceImpl;
import model.service.impl.RequestServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * adds dish to user's cart.
 * available to customer only.
 */
public class AddDishToCartCommand implements Command {

    private static final Logger logger = LogManager.getLogger(AddDishToCartCommand.class);
    private final RequestService requestService =
            new RequestServiceImpl(DaoFactory.getRequestDao(), DaoFactory.getRequestItemDao());
    private final DishService dishService = new DishServiceImpl(DaoFactory.getDishDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing add dish to cart command-----");
        int dishId = Integer.parseInt(request.getParameter("dish"));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Dish dish = dishService.findById(dishId)
                .orElseThrow(() -> new ObjectNotFoundException("dish not found"));
        requestService.addRequestItem(user, dish);
        logger.debug("-----successfully executed add dish to cart command-----");
        return "redirect:" + WebPages.DISH_COMMAND + dishId;
    }
}
