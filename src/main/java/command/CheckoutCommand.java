package command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * updates order status to 'pending'.
 * available to customer only.
 */
public class CheckoutCommand implements Command {

    private static final Logger logger = LogManager.getLogger(CheckoutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing checkout command-----");
        logger.debug("-----successfully executed checkout command-----");
        return null;
    }
}
