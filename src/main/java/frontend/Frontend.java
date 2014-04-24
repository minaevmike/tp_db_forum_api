package frontend;


import dbService.DataService;
import entities.*;
import entities.Thread;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
/**
 * Created by Andrey on 21.02.14.
 * blah
 */
public class Frontend extends HttpServlet{

    private User user;
    private Forum forum;
    private entities.Thread thread;
    private Utils utils;
    private int counter = 0;

    public Frontend(DataService ds)
    {
        user = new User(ds);
        forum = new Forum(ds);
        thread = new Thread(ds);
        utils = new Utils(ds);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        counter++;
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String[] tokens = parseUrl(request.getPathInfo());
        String decodedQuery = null;
        if(request.getQueryString() != null) {
            decodedQuery = URLDecoder.decode(request.getQueryString(), "UTF-8");
        }
        ////
            System.out.println(counter + "\t GET: \t" + tokens[3] + "\t" + tokens[4] + "\t" + decodedQuery);
        ////
        String result = executeApiQuery(tokens[3], tokens[4], decodedQuery);
        response.getWriter().print(result);
        ////
            System.out.println(result);
        ////
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        counter++;
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String[] tokens = parseUrl(request.getPathInfo()); //  __/db/api/{{entity}}/{{method}}/

        ////
            String line = request.getReader().readLine();
            System.out.println(counter + "\t POST: \t" + tokens[3] + "\t" + tokens[4] + "\t" + line);
        ////

        String result = executeApiQuery(tokens[3], tokens[4], line);
        response.getWriter().print(result);
        ////
            System.out.println(result);
        ////
    }

    private String[] parseUrl(String path)
    {
        return path.split("/");
    }

    private String executeApiQuery(String entity, String method, String content)
    {
        switch (entity) {
        case "forum":
            return forum.exec(method, content);
        case "post":
            break;
        case "user":
            return user.exec(method, content);
        case "thread":
            return thread.exec(method, content);
        case "util":
            return utils.exec(method, content);
        }
        return null;
    }

}
