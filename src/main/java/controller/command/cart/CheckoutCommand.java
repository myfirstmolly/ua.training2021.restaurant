package controller.command.cart;

import controller.command.Command;
import model.dao.DaoFactory;
import model.entities.Request;
import model.entities.Status;
import model.entities.User;
import model.exceptions.ObjectNotFoundException;
import model.service.RequestService;
import model.service.impl.RequestServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * updates order status to 'pending'.
 * available to customer only.
 */
public class CheckoutCommand implements Command {

    private static final Logger logger = LogManager.getLogger(CheckoutCommand.class);
    private final RequestService requestService =
            new RequestServiceImpl(DaoFactory.getRequestDao());

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("-----executing checkout command-----");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String address = request.getParameter("address");

        if (!isValid(address))
            return redirectBack(request, user);

        requestService.approveRequest(user, address);
        logger.debug("-----successfully executed checkout command-----");
        return "redirect:" + WebPages.ORDERS_COMMAND;
    }

    private String redirectBack(HttpServletRequest request, User user) {
        Request req = requestService.findOneByUserAndStatus(user, Status.OPENED)
                .orElseThrow(() -> new ObjectNotFoundException("request not found"));
        request.setAttribute("totalPrice", req.getTotalPrice());
        String locale = (String) request.getSession().getAttribute("lang");
        if (locale == null)
            locale = "en";
        ResourceBundle bundle = ResourceBundle.getBundle("i18n", Locale.forLanguageTag(locale));
        request.setAttribute("addressMsg", bundle.getString("incorrect.address"));
        return WebPages.CHECKOUT_FORM_PAGE;
    }

    private boolean isValid(String address) {
        return address != null && address.length() >= 6 && address.length() <= 128;
    }
}
