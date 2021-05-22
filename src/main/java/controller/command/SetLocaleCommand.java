package controller.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;

public class SetLocaleCommand implements Command {

    private static final Logger logger = LogManager.getLogger(SetLocaleCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing setLocale command-----");
        String locale = request.getParameter("lang");
        logger.trace("locale to set: " + locale);
        HttpSession session = request.getSession();
        Config.set(session, "javax.servlet.jsp.jstl.fmt.locale", locale);
        session.setAttribute("lang", locale);
        logger.trace("added to session attribute lang=" + locale);
        logger.debug("-----successfully executed setLocale command-----");
        return WebPages.MENU_COMMAND;
    }

}
