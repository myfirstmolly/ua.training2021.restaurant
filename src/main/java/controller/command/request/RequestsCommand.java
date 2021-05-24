package controller.command.request;

import controller.command.Command;
import model.dao.DaoFactory;
import model.entities.Request;
import model.entities.Role;
import model.entities.Status;
import model.entities.User;
import model.service.RequestService;
import model.service.impl.RequestServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final RequestService requestService =
            new RequestServiceImpl(DaoFactory.getRequestDao(), DaoFactory.getRequestItemDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing orders command-----");
        setRequestAttributes(request);
        logger.debug("-----successfully executed order command-----");
        return WebPages.ORDERS_PAGE;
    }

    private void setRequestAttributes(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        List<Status> statusList = Arrays.asList(Status.values()).subList(1, Status.values().length);
        request.setAttribute("role", user.getRole());
        request.setAttribute("statusList", statusList);
        int pageIndex = (int) request.getSession().getAttribute("requestPage");

        if (user.getRole().equals(Role.MANAGER)) {
            Page<Request> ordersPage = getManagerOrdersPage(request, pageIndex);
            request.setAttribute("orders", ordersPage);
            return;
        }

        Page<Request> ordersPage = getUserOrdersPage(request, pageIndex, user);
        request.setAttribute("orders", ordersPage);
    }

    private Page<Request> getManagerOrdersPage(HttpServletRequest request, int pageIndex) {
        Status status = (Status) request.getSession().getAttribute("status");
        if (status == null || Status.OPENED.equals(status)) {
            return requestService.findAll(pageIndex);
        }
        return requestService.findAllByStatus(status.getId(), pageIndex);
    }

    private Page<Request> getUserOrdersPage(HttpServletRequest request, int pageIndex, User user) {
        Status status = (Status) request.getSession().getAttribute("status");
        if (status == null) {
            return requestService.findAllByUserId(user.getId(), pageIndex);
        }
        return requestService.findAllByUserAndStatus(user, status, pageIndex);
    }
}
