package command;

import database.DBManager;
import entities.Dish;
import entities.User;
import service.DishService;
import service.RequestService;
import service.impl.DishServiceImpl;
import service.impl.RequestServiceImpl;
import util.WebPages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AddDishToCartCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int dishId = Integer.parseInt(request.getParameter("dish"));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        RequestService requestService = new RequestServiceImpl(DBManager.getInstance());
        DishService dishService = new DishServiceImpl(DBManager.getInstance());
        Dish dish = dishService.findById(dishId).get();
        requestService.addRequestItem(user, dish, 1);
        return WebPages.CART_COMMAND;
    }
}
