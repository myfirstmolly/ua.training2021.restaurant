package controller.command.cart;

import controller.command.Command;
import model.database.DaoFactory;
import model.entities.Request;
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
import javax.servlet.http.HttpSession;

/**
 * updates order status to 'pending'.
 * available to customer only.
 */
public class CheckoutCommand implements Command {

    private static final Logger logger = LogManager.getLogger(CheckoutCommand.class);
    private final RequestService requestService =
            new RequestServiceImpl(DaoFactory.getRequestDao(), DaoFactory.getRequestItemDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing checkout command-----");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Request req = requestService.findOneByUserAndStatus(user, Status.OPENED)
                .orElseThrow(() -> new ObjectNotFoundException("request not found"));
        String address = request.getParameter("address");

        if (!isValid(address)) {
            request.setAttribute("totalPrice", req.getTotalPrice());
            request.setAttribute("addressMsg", "address must be between 6 and 128 symbols");
            return WebPages.CHECKOUT_FORM_PAGE;
        }

        req.setStatus(Status.PENDING);
        req.setDeliveryAddress(address);
        requestService.updateRequest(req);
        logger.debug("-----successfully executed checkout command-----");
        return WebPages.ORDER_COMMAND + req.getId();
    }

    private boolean isValid(String address) {
        return address != null && address.length() >= 6 && address.length() <= 128;
    }
}
