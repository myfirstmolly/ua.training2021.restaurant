package command;

import database.DaoFactory;
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
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class CheckoutFormCommand implements Command {

    private static final Logger logger = LogManager.getLogger(CheckoutFormCommand.class);
    private final RequestService requestService =
            new RequestServiceImpl(DaoFactory.getRequestDao(), DaoFactory.getRequestItemDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing checkout form command-----");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Optional<Request> req = requestService.findOneByUserAndStatus(user, Status.OPENED);

        if (!req.isPresent()) {
            return WebPages.CART_COMMAND;
        } else {
            request.setAttribute("totalPrice", req.get().getTotalPrice());
        }

        logger.debug("-----successfully executed checkout form command-----");
        return WebPages.CHECKOUT_FORM_PAGE;
    }
}
