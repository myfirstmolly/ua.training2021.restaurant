package command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckoutCommand implements Command {

    private static final Logger logger = LogManager.getLogger(CheckoutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("executing checkout command");
        return null;
    }
}
