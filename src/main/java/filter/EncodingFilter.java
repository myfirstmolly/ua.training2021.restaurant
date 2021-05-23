package filter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import java.io.IOException;

public class EncodingFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(EncodingFilter.class);
    private String encoding;

    public void destroy() {
        logger.debug("destroying encoding filter");
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        logger.debug("encoding filter started");
        if (req.getCharacterEncoding() == null) {
            logger.trace("setting request encoding: " + encoding);
            req.setCharacterEncoding(encoding);
        }
        resp.setContentType("text/html; charset=" + encoding);
        resp.setCharacterEncoding(encoding);
        logger.debug("encoding filter finished");
        chain.doFilter(req, resp);
    }

    public void init(FilterConfig config) {
        logger.debug("initializing encoding filter");
        encoding = config.getInitParameter("encoding");
        logger.debug("initialization finished, encoding: " + encoding);
    }

}
