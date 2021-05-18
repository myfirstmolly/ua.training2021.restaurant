package command;

import database.DBManager;
import entities.Request;
import entities.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.RequestService;
import service.impl.RequestServiceImpl;
import util.Page;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class RequestsCommand implements Command {

    private static final Logger logger = LogManager.getLogger(RequestsCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.info("executing orders command");
        RequestService requestService = new RequestServiceImpl(DBManager.getInstance());
        List<Status> statusList = Arrays.asList(Status.values());
        request.setAttribute("statusList", statusList);
        int pageIndex = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null)
            pageIndex = Integer.parseInt(pageParam);
        Page<Request> requests;
        if (request.getParameter("status") == null)
            requests = requestService.findAll(pageIndex);
        else
            requests = requestService.findAllByStatus(Integer.parseInt(request.getParameter("status")), pageIndex);
        request.setAttribute("orders", requests);
        return WebPages.ORDERS_PAGE;
    }
}
