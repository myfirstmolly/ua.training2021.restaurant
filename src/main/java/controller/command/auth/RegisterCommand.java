package controller.command.auth;

import controller.command.Command;
import model.database.DaoFactory;
import model.entities.Role;
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
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * validates user's credentials and registers user if they are valid.
 */
public class RegisterCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);
    private final UserService userService = new UserServiceImpl(DaoFactory.getUserDao());

    // regex to check whether name starts with capital letter, contains letters only
    // and its length is between 4 and 32 symbols
    private static final String NAME_REGEX = "\\p{Lu}\\p{L}{3,31}";

    // regex to check if email matches pattern something@something.smth
    private static final String EMAIL_REGEX = "[A-Za-z]+@[A-Za-z]+\\.[a-z]+";

    // phone numbers can only be entered like (xxx)xxx-xx-xx
    private static final String PHONE_REGEX = "\\([0-9]{3}\\)[0-9]{3}-[0-9]{2}-[0-9]{2}";

    // regex to check if username only contains latin letters and its length is
    // between 4 and 16
    private static final String USERNAME_REGEX = "[A-Za-z]{4,16}";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing register command-----");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phone");
        String username = request.getParameter("username");
        String password = (String) request.getAttribute("password");
        if (!hasValidCredentials(name, email, phoneNumber, username, request)) {
            logger.trace("received invalid credentials, redirecting back");
            return WebPages.REGISTER_PAGE;
        }

        if (userService.findByUsername(username).isPresent()) {
            logger.trace("received duplicated username, redirecting back");
            request.setAttribute("usernameMsg", "username must be unique");
            return WebPages.REGISTER_PAGE;
        }

        User user = new User(username, password, name, phoneNumber, email, Role.CUSTOMER);
        userService.save(user);
        logger.info(String.format("user %s registered as %s", user.getUsername(), user.getRole()));
        HttpSession session = request.getSession();
        session.setAttribute("user", user);
        logger.debug("-----successfully executed register command-----");
        return "redirect:" + WebPages.MENU_COMMAND;
    }

    private boolean hasValidCredentials(String name, String email, String phoneNumber,
                                        String username, HttpServletRequest request) {
        boolean hasErrors = false;
        String locale = (String) request.getSession().getAttribute("lang");
        if (locale == null)
            locale = "en";
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", Locale.forLanguageTag(locale));

        if (name == null || !Pattern.compile(NAME_REGEX).matcher(name).find()) {
            request.setAttribute("nameMsg", bundle.getString("wrong.name.msg"));
            hasErrors = true;
        }
        if (email == null || !Pattern.compile(EMAIL_REGEX).matcher(email).find()) {
            request.setAttribute("emailMsg", bundle.getString("wrong.email.msg"));
            hasErrors = true;
        }
        if (phoneNumber == null || !Pattern.compile(PHONE_REGEX).matcher(phoneNumber).find()) {
            request.setAttribute("phoneMsg", bundle.getString("wrong.phone.msg"));
            hasErrors = true;
        }
        if (username == null || !Pattern.compile(USERNAME_REGEX).matcher(username).find()) {
            request.setAttribute("usernameMsg", bundle.getString("wrong.username.msg"));
            hasErrors = true;
        }

        return !hasErrors;
    }
}
