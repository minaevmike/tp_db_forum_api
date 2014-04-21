package frontend;


import dbService.DataService;
import entities.User;

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
    private User user;

    public Frontend(DataService ds)
    {
        user = new User(ds);
    }

    public static String getTime()
    {
        return FORMATTER.format(new Date());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        System.out.print(request.getQueryString());
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String[] tokens = parseUrl(request.getPathInfo()); //  __/db/api/{{entity}}/{{method}}/

        String result = executeApiQuery(tokens[3], tokens[4], request.getReader().readLine());
        response.getWriter().print(result);
    }

    private String[] parseUrl(String path)
    {
        return path.split("/");
    }

    private String executeApiQuery(String entity, String method, String content)
    {
        switch (entity) {
        case "forum":
            break;
        case "post":
            break;
        case "user":
            return user.exec(method, content);
        case "thread":
            break;
        }
        return null;
    }

}
