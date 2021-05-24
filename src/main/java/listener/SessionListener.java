package listener;

import model.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashSet;

/**
 * session listener
 */
public class SessionListener implements HttpSessionListener, HttpSessionAttributeListener {

    private static final Logger logger = LogManager.getLogger(SessionListener.class);

    /**
     * checks if session attribute 'user' is added (which is equivalent to user logging in).
     * since concurrent logins are not allowed, if user with this username has already got
     * their session, new session will be invalidated.
     *
     * @param event HttpSessionBindingEvent
     */
    @Override
    @SuppressWarnings("unchecked")
    public void attributeAdded(HttpSessionBindingEvent event) {
        if (!event.getName().equals("user") || event.getValue() == null)
            return;

        User user = (User) event.getValue();
        HashSet<String> loggedUsers = (HashSet<String>) event.getSession()
                .getServletContext().getAttribute("loggedUsers");
        if (loggedUsers.contains(user.getUsername())) {
            logger.trace(String.format("user %s is already logged in, invalidating session...",
                    user.getUsername()));
            event.getSession().invalidate();
            loggedUsers.add(user.getUsername()); // <-- adding user back to list, because after
            // invalidating session, method sessionDestroyed() is being invoked, which also deletes
            // user from loggedUsers list
            logger.trace("session invalidated");
        } else {
            loggedUsers.add(user.getUsername());
            logger.trace(String.format("added user %s to list of logged in users",
                    user.getUsername()));
            event.getSession().getServletContext().setAttribute("loggedUsers", loggedUsers);
            logger.trace("added list of logged in users to servlet context");
        }
        logger.trace("logged in users: " + loggedUsers.toString());
    }

    public void sessionCreated(HttpSessionEvent se) {
        logger.debug("traced new session creation");
    }

    /**
     * when user logs out, their session will be removed from list of logged users
     *
     * @param se HttpSessionEvent
     */
    @SuppressWarnings("unchecked")
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.debug("traced session deletion");
        User user = (User) se.getSession().getAttribute("user");
        if (user != null) {
            HashSet<String> loggedUsers = (HashSet<String>) se.getSession()
                    .getServletContext().getAttribute("loggedUsers");
            if (loggedUsers.contains(user.getUsername())) {
                loggedUsers.remove(user.getUsername());
                logger.trace(String.format("removed user %s from list of logged in users",
                        user.getUsername()));
                se.getSession().getServletContext().setAttribute("loggedUsers", loggedUsers);
                logger.trace("added list of logged in users to servlet context");
            }
        }
        logger.debug("session deletion finished");
    }

}
