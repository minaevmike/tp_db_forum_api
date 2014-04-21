package dbService.dao;

import dataSets.UserData;
import dbService.DataService;
import dbService.executor.SimpleExecutor;
import dbService.executor.TExecutor;

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

//    public UserData getUser(String name) throws SQLException
//    {
//        TExecutor exec = new TExecutor();
//        TResultHandler<User> resultHandler = new TResultHandler<User>(){
//
//            public User handle(ResultSet result) throws SQLException {
//                result.next();
//                return new User(result.getI(1), result.getString(2), result.getString(3));
//            }
//        };
//        return exec.execQuery(dataService.getConnection(), "SELECT * FROM users WHERE name="
//                + escape(name), resultHandler);
//    }

    public void createUser(UserData user) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO `forum_api`.`Users` (`username`, `mail`, `name`, `isAnonymous`, `about`) VALUES (")
                .append("'").append(user.getUsername()).append("'") .append(",")
                .append("'").append(user.getMail()).append("'")     .append(",")
                .append("'").append(user.getName()).append("'")     .append(",")
                            .append(user.isAnonymous())             .append(",")
                .append("'").append(user.getAbout()).append("'")
                .append(")");

        exec.execUpdate(dataService.getConnection(), sb.toString());
    }
}
