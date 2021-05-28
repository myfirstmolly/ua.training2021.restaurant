package controller.command.auth;

import controller.command.Command;
import model.dao.DaoFactory;
import model.entities.User;
import model.service.UserService;
import model.service.impl.UserServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

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
        Optional<User> u = userService.findByUsername(login);

        if (u.isPresent() && u.get().getPassword().equals(password)) {
            User user = u.get();
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            logger.info(String.format("user %s logged in as %s", user.getUsername(), user.getRole()));
            logger.debug("-----successfully executed login command-----");
            return "redirect:" + WebPages.MENU_COMMAND;
        } else {
            String locale = (String) request.getSession().getAttribute("lang");
            if (locale == null)
                locale = "en";
            ResourceBundle bundle = ResourceBundle.getBundle("i18n", Locale.forLanguageTag(locale));
            request.setAttribute("message", bundle.getString("incorrect.credentials"));
            logger.info(String.format("user %s tried to log in", login));
            logger.debug("-----successfully executed login command-----");
            return WebPages.LOGIN_PAGE;
        }
    }
}
