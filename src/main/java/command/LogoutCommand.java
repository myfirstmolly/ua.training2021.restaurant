package command;

import entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutCommand implements Command {

    private static final Logger logger = LogManager.getLogger(LogoutCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("executing logout command");
        User user = (User) request.getSession().getAttribute("user");
        logger.info(String.format("user %s logged out", user.getUsername()));
        request.getSession().invalidate();
        return WebPages.DEFAULT_PAGE;
    }
}
