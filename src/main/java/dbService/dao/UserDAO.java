package dbService.dao;

import dataSets.UserData;
import dbService.DataService;
import dbService.executor.SimpleExecutor;
import dbService.executor.TExecutor;
import dbService.handlers.TResultHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Andrey on 21.04.14.
 */
public class UserDAO {

    private DataService dataService;

    public UserDAO(DataService ds)
    {
        dataService = ds;
    }

    public UserData getUserByMail(String userMail) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<UserData> resultHandler = new TResultHandler<UserData>(){

            public UserData handle(ResultSet result) throws SQLException {
                result.next();
                return new UserData(result.getInt(1), result.getString(2), result.getString(3), result.getString(4),
                                        result.getString(5), result.getBoolean(6));
            }
        };
        return exec.execQuery(dataService.getConnection(), "SELECT * FROM Users WHERE mail='" + userMail +"'", resultHandler);
    }

    public void createUser(UserData user) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO Users (`username`, `mail`, `name`, `isAnonymous`, `about`) VALUES (")
                .append("'").append(user.getUsername()).append("'") .append(",")
                .append("'").append(user.getMail()).append("'")     .append(",")
                .append("'").append(user.getName()).append("'")     .append(",")
                            .append(user.isAnonymous())             .append(",")
                .append("'").append(user.getAbout()).append("'")
                .append(")");

        exec.execUpdate(dataService.getConnection(), sb.toString());
    }
}
