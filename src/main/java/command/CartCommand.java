package command;

import database.DBManager;
import entities.Request;
import entities.Status;
import entities.User;
import service.RequestService;
import service.impl.RequestServiceImpl;
import util.WebPages;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class CartCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
