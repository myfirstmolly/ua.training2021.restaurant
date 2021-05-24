package filter;

import model.entities.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessionAttributesFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(SessionAttributesFilter.class);

    public void destroy() {
        logger.debug("session attribute filter destroyed");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        if (req.getParameter("command").equals("menu"))
            setMenuSessionAttributes(httpRequest);
        if (req.getParameter("command").equals("orders"))
            setRequestsSessionAttributes(httpRequest);
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) {
        logger.debug("session attribute filter initialized");
    }

    private void setMenuSessionAttributes(HttpServletRequest req) {
        String category = req.getParameter("category");
        String orderBy = req.getParameter("orderBy");
        HttpSession session = req.getSession();
        setPageAttributes(req, "menuPage");

        if (category != null) {
            logger.error(category);
            if (!category.equals("ALL")) {
                session.setAttribute("categoryId", Integer.parseInt(category));
            } else {
                session.removeAttribute("categoryId");
            }
            session.setAttribute("menuPage", 1);
            session.removeAttribute("orderBy");
        }

        if (orderBy != null) {
            session.setAttribute("orderBy", orderBy);
            session.setAttribute("menuPage", 1);
            session.removeAttribute("categoryId");
        }
    }

    private void setRequestsSessionAttributes(HttpServletRequest req) {
        String status = req.getParameter("status");
        HttpSession session = req.getSession();
        setPageAttributes(req, "requestPage");

        if (status != null) {
            if (!status.equals("ALL")) {
                session.setAttribute("status", Status.valueOf(status));
            } else {
                session.removeAttribute("status");
            }
            session.setAttribute("requestPage", 1);
        }
    }

    private void setPageAttributes(HttpServletRequest req, String attributeName) {
        String page = req.getParameter("page");

        if (page == null || Integer.parseInt(page) < 1)
            req.getSession().setAttribute(attributeName, 1);

        if (page != null)
            req.getSession().setAttribute(attributeName, Integer.parseInt(page));
    }

}
