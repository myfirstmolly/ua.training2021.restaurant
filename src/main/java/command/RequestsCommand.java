package command;

import database.DBManager;
import entities.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.RequestService;
import service.impl.RequestServiceImpl;
import util.Page;
import util.WebPages;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * returns list of orders based on user's role.
 * available to authorized users only.
 */
public class RequestsCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RequestsCommand.class);
    private final RequestService requestService = new RequestServiceImpl(DBManager.getInstance());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing orders command-----");
        setRequestAttributes(request, response);
        logger.debug("-----successfully executed order command-----");
        return WebPages.ORDERS_PAGE;
    }

    private void setRequestAttributes(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute("user");
        List<Status> statusList;
        int pageIndex = getPageIndex(request, response);
        Page<Request> ordersPage;

        if (user.getRole().equals(Role.MANAGER)) {
            ordersPage = getManagerOrdersPage(request, response, requestService, pageIndex);
            statusList = Arrays.asList(Status.values()).subList(1, Status.values().length);
        } else {
            ordersPage = getUserOrdersPage(request, response, requestService, pageIndex, user);
            statusList = Arrays.asList(Status.values());
        }

        request.setAttribute("role", user.getRole());
        request.setAttribute("statusList", statusList);
        request.setAttribute("orders", ordersPage);
    }

    private Page<Request> getManagerOrdersPage(HttpServletRequest request,
                                               HttpServletResponse response,
                                               RequestService requestService,
                                               int pageIndex) {
        Status status = getStatus(request, response);
        if (status == null ||
                Objects.equals(status, Status.OPENED)) {
            return requestService.findAll(pageIndex);
        }
        return requestService.findAllByStatus(status.toInt(),
                pageIndex);
    }

    private Page<Request> getUserOrdersPage(HttpServletRequest request,
                                            HttpServletResponse response,
                                            RequestService requestService,
                                            int pageIndex,
                                            User user) {
        Status status = getStatus(request, response);
        if (status == null) {
            return requestService.findAllByUserId(user.getId(), pageIndex);
        }
        return requestService.findAllByUserAndStatus(user,
                status, pageIndex);
    }

    private Status getStatus(HttpServletRequest request, HttpServletResponse response) {
        String status = request.getParameter("status");
        if (status != null && status.equals("all")) {
            for (Cookie c : request.getCookies()) {
                if (c.getName().equals("status") || c.getName().equals("page")) {
                    c.setMaxAge(0);
                    response.addCookie(c);
                }
            }
            return null;
        }

        if (status != null) {
            Cookie cookie = new Cookie("status", status);
            response.addCookie(cookie);
            return Status.valueOf(status);
        }

        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("status")) {
                return Status.valueOf(c.getValue());
            }
        }
        return null;
    }

    private int getPageIndex(HttpServletRequest request, HttpServletResponse response) {
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            Cookie cookie = new Cookie("page", pageParam);
            response.addCookie(cookie);
            return Integer.parseInt(pageParam);
        }

        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("page")) {
                return Integer.parseInt(c.getValue());
            }
        }
        Cookie cookie = new Cookie("page", "1");
        response.addCookie(cookie);
        return 1;
    }
}
