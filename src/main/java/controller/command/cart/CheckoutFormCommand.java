package controller.command.cart;

import controller.command.Command;
import model.dao.DaoFactory;
import model.entities.Request;
import model.entities.Status;
import model.entities.User;
import model.service.RequestService;
import model.service.impl.RequestServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class CheckoutFormCommand implements Command {

    private static final Logger logger = LogManager.getLogger(CheckoutFormCommand.class);
    private final RequestService requestService =
            new RequestServiceImpl(DaoFactory.getRequestDao());

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
