package entities;

import dbService.DataService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Andrey
 * 22.04.14.
 */
public class Utils implements EntityInterface {

    DataService dataService;

    public Utils(DataService dataService)
    {
        this.dataService = dataService;
    }


    private String clear()
    {
        Connection connection = dataService.getConnection();
        PreparedStatement ups = null;
        try {
            ups = connection.prepareStatement("select concat(\'TRUNCATE TABLE \', table_schema" +
                    ",\'.\',TABLE_NAME,\';\') FROM INFORMATION_SCHEMA.TABLES where table_schema = \'forum\';");
            ResultSet resultSet = ups.executeQuery();
            connection.setAutoCommit(false);
            PreparedStatement FKoff = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=0");
            FKoff.executeUpdate();
            while (resultSet.next()){
                PreparedStatement t = connection.prepareStatement(resultSet.getString(1));
                t.executeUpdate();
            }
            PreparedStatement FKon = connection.prepareStatement("SET FOREIGN_KEY_CHECKS=1");
            FKon.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "CLEARED";
    }

    @Override
    public String exec(String method, String data) {
        switch (method) {
            case "clear":
                return clear();
        }
        return null;
    }
}
