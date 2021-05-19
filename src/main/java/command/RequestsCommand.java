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
        logger.debug("-----executing orders command-----");
        RequestService requestService = new RequestServiceImpl(DBManager.getInstance());
        List<Status> statusList = Arrays.asList(Status.values());
        request.setAttribute("statusList", statusList);
        int pageIndex = getPageIndex(request);
        Page<Request> ordersPage = getOrdersPage(request, requestService, pageIndex);
        request.setAttribute("orders", ordersPage);
        logger.debug("-----successfully executed order command-----");
        return WebPages.ORDERS_PAGE;
    }

    private Page<Request> getOrdersPage(HttpServletRequest request, RequestService requestService, int pageIndex) {
        if (request.getParameter("status") == null) {
            return requestService.findAll(pageIndex);
        } else {
            return requestService.findAllByStatus(Integer.parseInt(request.getParameter("status")), pageIndex);
        }
    }

    private int getPageIndex(HttpServletRequest request) {
        String pageParam = request.getParameter("page");
        if (pageParam != null)
            return Integer.parseInt(pageParam);
        return 1;
    }
}
