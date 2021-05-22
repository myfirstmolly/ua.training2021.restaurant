package filter;

import model.entities.Role;
import model.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebFilter(servletNames = "Controller", urlPatterns = "/*", filterName = "AuthorizationFilter",
        initParams = {@WebInitParam(name = "manager", value = "addDish addDishGetPage deleteDish updateStatus"),
                @WebInitParam(name = "customer", value = "addToCart checkout checkoutForm cart deleteRequestItem updateQty"),
                @WebInitParam(name = "authorized", value = "logout order orders"),
                @WebInitParam(name = "any", value = "dish setLocale menu login register")})
public class AuthorizationFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(AuthorizationFilter.class);
    private final Map<Role, List<String>> authoritiesMap = new HashMap<>();
    private final Map<String, List<String>> commonCommands = new HashMap<>();

    public void destroy() {
        logger.info("destroyed command access filter");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        String command = req.getParameter("command");
        HttpServletRequest request = (HttpServletRequest) req;
        logger.debug("received command: " + command);
        logger.debug("authorization filter started, uri: " + request.getRequestURI());
        if (accessAllowed(command, request)) {
            logger.trace("access to page allowed");
            chain.doFilter(req, resp);
        }
        else {
            logger.trace("access to page is not allowed");
            if (request.getSession() != null) {
                logger.trace("invalidating session");
                request.getSession().invalidate();
            }
            logger.trace("redirecting to login page");
            req.getRequestDispatcher(WebPages.LOGIN_PAGE).forward(req, resp);
        }
    }

    public void init(FilterConfig config) {
        logger.debug("initializing command access filter...");
        List<String> managerCommands = Arrays.asList(config.getInitParameter("manager").split("\\s+"));
        List<String> customerCommands = Arrays.asList(config.getInitParameter("customer").split("\\s+"));
        List<String> authorizedCommands = Arrays.asList(config.getInitParameter("authorized").split("\\s+"));
        List<String> any = Arrays.asList(config.getInitParameter("any").split("\\s+"));
        authoritiesMap.put(Role.MANAGER, managerCommands);
        authoritiesMap.put(Role.CUSTOMER, customerCommands);
        logger.debug("authorities: " + authoritiesMap.toString());
        commonCommands.put("authorized", authorizedCommands);
        commonCommands.put("any", any);
        logger.debug("common: " + commonCommands.toString());
        logger.debug("initialized command access filter");
    }

    private boolean accessAllowed(String command, HttpServletRequest request) {
        if (command == null || command.isEmpty())
            return true;

        if (commonCommands.get("any").contains(command))
            return true;

        HttpSession session = request.getSession(false);

        if (commonCommands.get("authorized").contains(command)) {
            return session != null && session.getAttribute("user") != null;
        }

        User user = (User) request.getSession().getAttribute("user");

        if (user.getRole().equals(Role.CUSTOMER))
            return authoritiesMap.get(Role.CUSTOMER).contains(command);

        return user.getRole().equals(Role.MANAGER) && authoritiesMap.get(Role.MANAGER).contains(command);
    }

}
