package command;

import database.DBManager;
import entities.Status;
import entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.RequestService;
import service.impl.RequestServiceImpl;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * updates order status.
 * available to manager only.
 */
public class UpdateRequestStatusCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UpdateRequestStatusCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing update order status command-----");
        RequestService requestService = new RequestServiceImpl(DBManager.getInstance());
        User user = (User) request.getSession().getAttribute("user");
        int orderId = Integer.parseInt(request.getParameter("id"));
        String statusName = request.getParameter("status");
        Status status = Status.valueOf(statusName);
        requestService.setRequestStatus(orderId, status, user);
        logger.debug("-----successfully executed update order status command-----");
        return WebPages.ORDERS_COMMAND;
    }
}
