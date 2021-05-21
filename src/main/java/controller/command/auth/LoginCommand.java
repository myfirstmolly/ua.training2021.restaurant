package controller.command.auth;

import controller.command.Command;
import model.database.DaoFactory;
import model.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.service.UserService;
import model.service.impl.UserServiceImpl;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * authenticates user by their login and password.
 */
public class LoginCommand implements Command {

    private static final Logger logger = LogManager.getLogger(LoginCommand.class);
    private final UserService userService = new UserServiceImpl(DaoFactory.getUserDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing login command-----");
        String login = request.getParameter("login");
        String password = (String) request.getAttribute("password");

        if (userService.hasValidCredentials(login, password)) {
            User user = userService.findByUsername(login).get();
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            logger.info(String.format("user %s logged in as %s", user.getUsername(), user.getRole()));
            logger.debug("-----successfully executed login command-----");
            return WebPages.MENU_COMMAND;
        } else {
            request.setAttribute("message", "Please, enter correct credentials");
            logger.info(String.format("user %s tried to log in", login));
            logger.debug("-----successfully executed login command-----");
            return WebPages.LOGIN_PAGE;
        }
    }
}
