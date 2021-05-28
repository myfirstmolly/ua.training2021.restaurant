package controller.command.cart;

import controller.command.Command;
import model.dao.DaoFactory;
import model.entities.Request;
import model.entities.RequestItem;
import model.entities.Status;
import model.entities.User;
import model.service.RequestItemService;
import model.service.RequestService;
import model.service.impl.RequestItemServiceImpl;
import model.service.impl.RequestServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * returns user's cart page.
 * available to customer only.
 */
public class CartCommand implements Command {

    private static final Logger logger = LogManager.getLogger(CartCommand.class);
    private final RequestService requestService = new RequestServiceImpl(DaoFactory.getRequestDao());
    private final RequestItemService requestItemService =
            new RequestItemServiceImpl(DaoFactory.getRequestItemDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing cart command-----");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Optional<Request> req = requestService.findOneByUserAndStatus(user, Status.OPENED);

        if (req.isPresent()) {
            List<RequestItem> requestItems = requestItemService.findAllByRequestId(req.get().getId());
            request.setAttribute("requestItems", requestItems);
            request.setAttribute("request", req.get());
        }

        logger.debug("-----successfully executed cart command-----");
        return WebPages.CART_PAGE;
    }

}
