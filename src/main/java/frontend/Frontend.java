package frontend;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 * Created by Andrey on 21.02.14.
 * blah
 */
public class Frontend extends HttpServlet{

    private final static DateFormat FORMATTER = new SimpleDateFormat("HH:mm:ss");

    public Frontend(){}

    public static String getTime()
    {
        return FORMATTER.format(new Date());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String path = request.getPathInfo();
        switch (path) {
        default:
            response.getWriter().println("default");
            break;
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        String path = request.getPathInfo();

        switch (path) {
        case "/login":
            break;
        case "/registerUser":
            break;
        }
    }

}
