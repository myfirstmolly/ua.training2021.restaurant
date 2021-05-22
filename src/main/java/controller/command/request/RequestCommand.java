package controller.command.request;

import controller.command.Command;
import model.database.DaoFactory;
import model.entities.Request;
import model.entities.Role;
import model.entities.Status;
import model.entities.User;
import model.exceptions.ObjectNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.service.RequestService;
import model.service.impl.RequestServiceImpl;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * returns page with details about specific order.
 * available to authorized users only.
 */
public class RequestCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RequestCommand.class);
    private final RequestService requestService =
            new RequestServiceImpl(DaoFactory.getRequestDao(), DaoFactory.getRequestItemDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing order command-----");
        int id = Integer.parseInt(request.getParameter("id"));
        User user = (User) request.getSession().getAttribute("user");
        Request r = requestService.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("request not found"));
        int statusId = r.getStatus().getId();
        if (r.getStatus().equals(Status.OPENED)) {
            return "redirect:" + WebPages.CART_COMMAND;
        }
        List<Status> statusList = Arrays.asList(Status.values()).subList(statusId + 1, Status.values().length);
        request.setAttribute("statusList", statusList);
        request.setAttribute("request", r);
        request.setAttribute("role", user.getRole());
        logger.debug("-----successfully executed order command-----");
        return WebPages.ORDER_PAGE;
    }

}
