package command;

import database.DBManager;
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
import java.util.List;

public class CartCommand implements Command {

    private static final Logger logger = LogManager.getLogger(CartCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.trace("executing cart command");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        RequestService requestService = new RequestServiceImpl(DBManager.getInstance());
        List<Request> requests = requestService.findAllByUserAndStatus(user, Status.OPENED);
        if (requests.isEmpty())
            request.setAttribute("request", null);
        else request.setAttribute("request", requests.get(0));
        return WebPages.CART_PAGE;
    }
}
