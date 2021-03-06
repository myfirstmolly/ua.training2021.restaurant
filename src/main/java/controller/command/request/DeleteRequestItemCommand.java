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
 * deletes request item from user's cart.
 * available to customer only.
 */
public class DeleteRequestItemCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteRequestItemCommand.class);
    private final RequestItemService requestItemService =
            new RequestItemServiceImpl(DaoFactory.getRequestItemDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing delete request command-----");
        int id = Integer.parseInt(request.getParameter("id"));
        requestItemService.deleteById(id);
        logger.debug("-----successfully executed delete request command-----");
        return "redirect:" + WebPages.CART_COMMAND;
    }
}
