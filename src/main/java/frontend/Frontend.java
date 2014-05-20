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
    private Post post;
    private Utils utils;
    private int counter = 0;

    public Frontend(DataService ds)
    {
        user = new User(ds);
        forum = new Forum(ds);
        thread = new Thread(ds);
        utils = new Utils(ds);
        post = new Post(ds);
        utils.exec("clear", null);
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

        if(tokens.length > 4) {
            String result = executeApiQuery(tokens[3], tokens[4], decodedQuery);
            response.getWriter().print(result);
        } else {
            response.getWriter().print("{code:1, \"message\":\"invalid url\"}");
        }
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        counter++;
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String[] tokens = parseUrl(request.getPathInfo()); //  __/db/api/{{entity}}/{{method}}/

        String line = request.getReader().readLine();
        if(tokens.length > 4) {
            String result = executeApiQuery(tokens[3], tokens[4], line);
            response.getWriter().print(result);
        } else {
            response.getWriter().print("{code:1, \"message\":\"invalid request\"}");
        }
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
            return post.exec(method, content);
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
