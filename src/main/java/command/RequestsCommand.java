package command;

import database.DBManager;
import entities.Request;
import entities.Role;
import entities.Status;
import entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.RequestService;
import service.impl.RequestServiceImpl;
import util.Page;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

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
        setRequestAttributes(request);
        logger.debug("-----successfully executed order command-----");
        return WebPages.ORDERS_PAGE;
    }

    private void setRequestAttributes(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        List<Status> statusList;
        int pageIndex = getPageIndex(request);
        Page<Request> ordersPage;

        if (user.getRole().equals(Role.MANAGER)) {
            ordersPage = getOrdersPage(request, requestService, pageIndex);
            statusList = Arrays.asList(Status.values()).subList(1, Status.values().length);
        } else {
            ordersPage = getOrdersPage(request, requestService, pageIndex, user);
            statusList = Arrays.asList(Status.values());
        }

        request.setAttribute("role", user.getRole());
        request.setAttribute("statusList", statusList);
        request.setAttribute("orders", ordersPage);
    }

    private Page<Request> getOrdersPage(HttpServletRequest request,
                                        RequestService requestService,
                                        int pageIndex) {
        if (request.getParameter("status") == null) {
            return requestService.findAll(pageIndex);
        } else {
            return requestService.findAllByStatus(Status.valueOf(request.getParameter("status")).toInt(), pageIndex);
        }
    }

    private Page<Request> getOrdersPage(HttpServletRequest request,
                                        RequestService requestService,
                                        int pageIndex,
                                        User user) {
        if (request.getParameter("status") == null) {
            return requestService.findAllByUserId(user.getId(), pageIndex);
        } else {
            return requestService.findAllByUserAndStatus(user,
                    Status.valueOf(request.getParameter("status")), pageIndex);
        }
    }

    private int getPageIndex(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        if (pageParam != null)
            return Integer.parseInt(pageParam);
        return 1;
    }
}
