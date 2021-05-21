package controller.command;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * main interface for Command pattern implementation
 */
public interface Command {

    /**
     * @param request
     * @param response
     * @return address to go after command execution
     * @throws ServletException
     * @throws IOException
     */
    String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
