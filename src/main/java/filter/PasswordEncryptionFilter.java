package filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.WebPages;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordEncryptionFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(PasswordEncryptionFilter.class);

    public void destroy() {
        logger.debug("destroyed password encryption filter");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        if ("register".equals(req.getParameter("command"))) {
            if (req.getParameter("password") == null ||
                    req.getParameter("password").length() < 6) {
                req.setAttribute("passwordMsg", "password is too short");
                req.getRequestDispatcher(WebPages.REGISTER_PAGE).forward(req, resp);
                return;
            }
        }

        if ("register".equals(req.getParameter("command")) ||
                "login".equals(req.getParameter("command"))) {
            String password = req.getParameter("password");
            req.setAttribute("password", encrypt(password));
            logger.debug("successfully hashed user's password");
        }
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) {
        logger.debug("initialized password encryption filter");
    }

    private String encrypt(String password) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            logger.error("unable to configure password encryption, cause: ", ex);
        }
        return password;
    }
}
