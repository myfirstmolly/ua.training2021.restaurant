package controller;

import controller.command.Command;
import controller.command.MenuCommand;
import controller.command.SetLocaleCommand;
import controller.command.auth.LoginCommand;
import controller.command.auth.LogoutCommand;
import controller.command.auth.RegisterCommand;
import controller.command.cart.AddDishToCartCommand;
import controller.command.cart.CartCommand;
import controller.command.cart.CheckoutCommand;
import controller.command.cart.CheckoutFormCommand;
import controller.command.dish.AddDishCommand;
import controller.command.dish.AddDishPageCommand;
import controller.command.dish.DeleteDishCommand;
import controller.command.dish.DishCommand;
import controller.command.request.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "Controller", urlPatterns = "/api")
public class Controller extends HttpServlet {

    private static final Logger logger = LogManager.getLogger(Controller.class);

    private Map<String, Command> commandMap;

    @Override
    public void init() throws ServletException {
        commandMap = new HashMap<>();
        commandMap.put("default", new MenuCommand());
        commandMap.put("setLocale", new SetLocaleCommand());
        commandMap.put("addDish", new AddDishCommand());
        commandMap.put("addDishGetPage", new AddDishPageCommand());
        commandMap.put("addToCart", new AddDishToCartCommand());
        commandMap.put("checkout", new CheckoutCommand());
        commandMap.put("checkoutForm", new CheckoutFormCommand());
        commandMap.put("cart", new CartCommand());
        commandMap.put("deleteDish", new DeleteDishCommand());
        commandMap.put("deleteRequestItem", new DeleteRequestItemCommand());
        commandMap.put("dish", new DishCommand());
        commandMap.put("login", new LoginCommand());
        commandMap.put("logout", new LogoutCommand());
        commandMap.put("menu", new MenuCommand());
        commandMap.put("register", new RegisterCommand());
        commandMap.put("order", new RequestCommand());
        commandMap.put("orders", new RequestsCommand());
        commandMap.put("updateStatus", new UpdateRequestStatusCommand());
        commandMap.put("updateQty", new UpdateRequestItemQtyCommand());
        super.init();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String commandParameter = request.getParameter("command");
        logger.debug("received command ---> " + commandParameter);
        Command command = commandMap.get(commandParameter);
        if (command == null) {
            logger.trace("command is unknown, switching to default");
            command = commandMap.get("default");
        }

        logger.trace("executing command...");
        String page = command.execute(request, response);
        if (page.contains("redirect:")) {
            logger.trace("successfully executed command, sending redirect to " + page);
            response.sendRedirect(page.replace("redirect:/", ""));
        } else {
            RequestDispatcher dispatcher = request.getRequestDispatcher(page);
            logger.trace("successfully executed command, forwarding request to " + page);
            dispatcher.forward(request, response);
        }
    }

}
