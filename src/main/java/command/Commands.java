package command;

import java.util.HashMap;
import java.util.Map;

public class Commands {

    private static final Map<String, Command> commandMap = new HashMap<>();

    static {
        commandMap.put("login", new LoginCommand());
        commandMap.put("menu", new MenuCommand());
    }

    public static Command getCommand(String name) {
        if (!commandMap.containsKey(name)) {
            return null;
        }
        return commandMap.get(name);
    }

}
