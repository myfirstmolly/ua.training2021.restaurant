package controller.command.auth;

import controller.command.Command;
import model.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * deletes user session and logs them out.
 */
public class LogoutCommand implements Command {

    private static final Logger logger = LogManager.getLogger(LogoutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing logout command-----");
        User user = (User) request.getSession(false).getAttribute("user");
        if (user != null) {
            logger.trace(String.format("invalidating user %s session...", user.getUsername()));
            request.getSession(false).invalidate();
            logger.trace("successfully invalidated session");
            logger.info(String.format("user %s logged out", user.getUsername()));
        }
        logger.debug("-----successfully executed logout command-----");
        return WebPages.DEFAULT_PAGE;
    }
}
