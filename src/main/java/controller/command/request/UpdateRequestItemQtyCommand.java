package controller.command.request;

import controller.command.Command;
import model.dao.DaoFactory;
import model.service.RequestItemService;
import model.service.impl.RequestItemServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * updates order item quantity in the cart.
 * available to customer only.
 */
public class UpdateRequestItemQtyCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UpdateRequestStatusCommand.class);
    private final RequestItemService requestItemService =
            new RequestItemServiceImpl(DaoFactory.getRequestItemDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing update order item quantity command-----");
        String action = request.getParameter("action");
        updateOrderItem(request, action);
        logger.debug("-----successfully executed update order item quantity command-----");
        return "redirect:" + WebPages.CART_COMMAND;
    }

    private void updateOrderItem(HttpServletRequest request, String action) {
        int id = Integer.parseInt(request.getParameter("id"));
        if ("increase".equals(action)) {
            requestItemService.increaseQuantity(id);
            logger.debug("increased order item quantity");
        }

        if ("decrease".equals(action)) {
            requestItemService.decreaseQuantity(id);
            logger.debug("decreased order item quantity");
        }
    }
}
