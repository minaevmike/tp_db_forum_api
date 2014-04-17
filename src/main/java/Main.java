import frontend.Frontend;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created by Andrey on 15.02.14.
 *
 */
public class Main {
    public static void main(String[] args) throws Exception
    {
        Frontend frontend = new Frontend();

        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontend), "/*");

        server.setHandler(context);
        server.start();
        server.join();
    }
}