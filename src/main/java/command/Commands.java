package command;

import java.util.HashMap;
import java.util.Map;

public class Commands {

    private static final Map<String, Command> commandMap = new HashMap<>();

    static {
        commandMap.put("addDish", new AddDishCommand());
        commandMap.put("addToCart", new AddDishToCartCommand());
        commandMap.put("checkout", new CheckoutCommand());
        commandMap.put("cart", new CartCommand());
        commandMap.put("deleteDish", new DeleteDishCommand());
        commandMap.put("deleteRequestItem", new DeleteRequestItemCommand());
        commandMap.put("dish", new DishCommand());
        commandMap.put("login", new LoginCommand());
        commandMap.put("logout", new LogoutCommand());
        commandMap.put("menu", new MenuCommand());
        commandMap.put("orders", new RequestsCommand());
        commandMap.put("updateStatus", new UpdateOrderStatusCommand());
        commandMap.put("updateQty", new UpdateRequestItemQtyCommand());
    }

    public static Command getCommand(String name) {
        if (!commandMap.containsKey(name)) {
            return null;
        }
        return commandMap.get(name);
    }

}
