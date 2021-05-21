package controller.command;

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

import java.util.HashMap;
import java.util.Map;

/**
 * commands container
 */
public class Commands {

    private static final Logger logger = LogManager.getLogger(Commands.class);

    private static final Map<String, Command> commandMap = new HashMap<>();

    static {
        commandMap.put("default", new MenuCommand());
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
    }

    /**
     * @param name name of command
     * @return Command object which executes command with this name.
     * if command is invalid or null, this method will return default
     * Command, which is MenuCommand
     */
    public static Command getCommand(String name) {
        logger.debug("received command " + name);
        if (!commandMap.containsKey(name)) {
            logger.debug(String.format("received unknown command %s, switching to default", name));
            return commandMap.get("default");
        }
        return commandMap.get(name);
    }

}
