package controller.command.request;

import controller.command.Command;
import model.dao.DaoFactory;
import model.entities.Status;
import model.entities.User;
import model.service.RequestService;
import model.service.impl.RequestServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * updates order status.
 * available to manager only.
 */
public class UpdateRequestStatusCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UpdateRequestStatusCommand.class);
    private final RequestService requestService =
            new RequestServiceImpl(DaoFactory.getRequestDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing update order status command-----");
        User user = (User) request.getSession().getAttribute("user");
        int orderId = Integer.parseInt(request.getParameter("id"));
        int status = Integer.parseInt(request.getParameter("status"));
        if (status < Status.values().length) {
            Status s = Status.values()[status];
            requestService.setRequestStatus(orderId, s, user);
        }
        logger.debug("-----successfully executed update order status command-----");
        return "redirect:" + WebPages.ORDERS_COMMAND;
    }
}
