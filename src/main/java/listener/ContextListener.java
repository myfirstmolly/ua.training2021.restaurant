package listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;

public class ContextListener implements ServletContextListener {

    private static final Logger logger = LogManager.getLogger(ContextListener.class);

    public ContextListener() {
    }

    /**
     * initializes context attribute with logged users
     *
     * @param sce context event
     */
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("context initialization started");
        List<String> loggedUsers = new ArrayList<>();
        sce.getServletContext().setAttribute("loggedUsers", loggedUsers);
        logger.trace("added 'loggedUsers' attribute to servlet context");
        logger.debug("context initialization finished");
    }

    public void contextDestroyed(ServletContextEvent sce) {
        logger.debug("destroyed servlet context");
    }

}
