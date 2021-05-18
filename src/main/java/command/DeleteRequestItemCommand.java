package command;

import database.DBManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.RequestService;
import service.impl.RequestServiceImpl;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteRequestItemCommand implements Command {

    private static final Logger logger = LogManager.getLogger(DeleteRequestItemCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("executing delete request command");
        int id = Integer.parseInt(request.getParameter("id"));
        RequestService requestService = new RequestServiceImpl(DBManager.getInstance());
        requestService.deleteRequestItem(id);
        return WebPages.CART_COMMAND;
    }
}
