import dbService.DataService;
import frontend.Frontend;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.json.simple.JSONObject;

/**
 * Created by Andrey on 15.02.14.
 *
 */
public class Main {
    public static void main(String[] args) throws Exception
    {
        JSONObject obj=new JSONObject();
        obj.put("name","foo");
        obj.put("num",new Integer(100));
        obj.put("balance",new Double(1000.21));
        obj.put("is_vip",new Boolean(true));
        obj.put("nickname",null);
        System.out.print(obj);


        DataService dataService = new DataService();
        dataService.connect();
        Frontend frontend = new Frontend(dataService);

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontend), "/*");

        server.setHandler(context);
        server.start();
        server.join();
    }
}