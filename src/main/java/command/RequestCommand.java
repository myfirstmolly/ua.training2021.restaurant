package command;

import database.DBManager;
import entities.Request;
import entities.Status;
import entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.RequestService;
import service.impl.RequestServiceImpl;
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

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing order command-----");
        int id = Integer.parseInt(request.getParameter("id"));
        RequestService requestService = new RequestServiceImpl(DBManager.getInstance());
        User user = (User) request.getSession().getAttribute("user");
        Request r = requestService.findById(id).get();
        int statusId = r.getStatus().toInt();
        List<Status> statusList = Arrays.asList(Status.values()).subList(statusId + 1, Status.values().length);
        request.setAttribute("statusList", statusList);
        request.setAttribute("request", r);
        request.setAttribute("role", user.getRole());
        logger.debug("-----successfully executed order command-----");
        return WebPages.ORDER_PAGE;
    }

}
