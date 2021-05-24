package controller.command.request;

import controller.command.Command;
import model.dao.DaoFactory;
import model.service.RequestService;
import model.service.impl.RequestServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * deletes request item from user's cart.
 * available to customer only.
 */
public class DeleteRequestItemCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteRequestItemCommand.class);
    private final RequestService requestService =
            new RequestServiceImpl(DaoFactory.getRequestDao(), DaoFactory.getRequestItemDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing delete request command-----");
        int id = Integer.parseInt(request.getParameter("id"));
        requestService.deleteRequestItem(id);
        logger.debug("-----successfully executed delete request command-----");
        return "redirect:" + WebPages.CART_COMMAND;
    }
}
