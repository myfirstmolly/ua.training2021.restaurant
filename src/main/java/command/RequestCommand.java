package command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * returns page with details about specific order.
 * available to authorized users only.
 */
public class RequestCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RequestCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing order command-----");
        logger.debug("-----successfully executed order command-----");
        return null;
    }

}
