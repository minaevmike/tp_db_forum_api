package dbService;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * Created by Andrey
 */
public class DataService {

    private Connection connection = null;

    public void connect()
    {
        try{
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            connection = DriverManager.getConnection(
                    "jdbc:mysql://" +
                    "localhost:" +
                    "3306/" +
                    "forum?" +
                    "user=sqadmin&" +
                    "password=sqadmin");

        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection()
    {
        return connection;
    }
}

