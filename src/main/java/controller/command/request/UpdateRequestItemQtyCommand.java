package controller.command.request;

import controller.command.Command;
import model.dao.DaoFactory;
import model.entities.RequestItem;
import model.exceptions.ObjectNotFoundException;
import model.service.RequestService;
import model.service.impl.RequestServiceImpl;
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
    private final RequestService requestService =
            new RequestServiceImpl(DaoFactory.getRequestDao(), DaoFactory.getRequestItemDao());

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
        RequestItem item = requestService.findRequestItemById(id)
                .orElseThrow(() -> new ObjectNotFoundException("request not found"));
        if ("increase".equals(action) && item.getQuantity() < 30) {
            item.setQuantity(item.getQuantity() + 1);
            logger.debug("increased order item quantity");
        }

        if ("decrease".equals(action) && item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            logger.debug("decreased order item quantity");
        }

        requestService.updateRequestItem(item);
    }
}
