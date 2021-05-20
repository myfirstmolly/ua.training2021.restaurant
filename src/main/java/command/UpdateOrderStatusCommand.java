package command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * updates order status.
 * available to manager only.
 */
public class UpdateOrderStatusCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UpdateOrderStatusCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing update order status command-----");
        logger.debug("-----successfully executed update order status command-----");
        return null;
    }
}
