package command;

import database.DBManager;
import entities.RequestItem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.RequestService;
import service.impl.RequestServiceImpl;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateRequestItemQtyCommand implements Command {

    private static final Logger logger = LogManager.getLogger(UpdateOrderStatusCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing update order item quantity command-----");
        String action = request.getParameter("action");
        updateOrderItem(request, action);
        logger.debug("-----successfully executed update order item quantity command-----");
        return WebPages.CART_COMMAND;
    }

    private void updateOrderItem(HttpServletRequest request, String action) {
        RequestService requestService = new RequestServiceImpl(DBManager.getInstance());
        int id = Integer.parseInt(request.getParameter("id"));
        RequestItem item = requestService.findRequestItemById(id).get();
        if ("increase".equals(action)) {
            item.setQuantity(item.getQuantity() + 1);
            logger.debug("increased order item quantity");
        }

        if ("decrease".equals(action)) {
            item.setQuantity(item.getQuantity() - 1);
            logger.debug("decreased order item quantity");
        }

        requestService.updateRequestItem(item);
    }
}
