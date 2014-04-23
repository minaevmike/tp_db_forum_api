package dbService;

import dataSets.ForumData;
import dataSets.UserData;
import dbService.executor.SimpleExecutor;
import dbService.executor.TExecutor;
import dbService.handlers.TResultHandler;
import utils.ValueStringBuilder;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

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
                    "forum_api?" +
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

    public int getUserIdByMail(String userMail) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<Integer> resultHandler = new TResultHandler<Integer>(){

            public Integer handle(ResultSet result) throws SQLException {
                result.next();
                return result.getInt(1);
            }
        };
        return exec.execQuery(getConnection(), "SELECT id FROM Users WHERE mail='" + userMail +"'", resultHandler);
    }

    public UserData getUserByMail(String userMail) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<UserData> resultHandler = new TResultHandler<UserData>(){

            public UserData handle(ResultSet result) throws SQLException {
                result.next();
                return new UserData(result.getInt(1), result.getString(2), result.getString(3), result.getString(4),
                        result.getBoolean(5), result.getString(6));
            }
        };
        return exec.execQuery(getConnection(), "SELECT * FROM Users WHERE mail='" + userMail +"'", resultHandler);
    }

    public List<String> getFollowers(int id) throws SQLException {
        TExecutor exec = new TExecutor();
        TResultHandler<List<String>> resultHandler = new TResultHandler<List<String>>(){

            public List<String> handle(ResultSet result) throws SQLException {
                List<String> list= new LinkedList<>();
                while(result.next()) {
                    list.add(result.getString(1));
                }
                return list;
            }
        };

        return exec.execQuery(getConnection(),
                "SELECT u.mail FROM Follows f JOIN Users u ON u.id=f.follow WHERE f.user=" + String.valueOf(id), resultHandler);
    }

    public List<String> getFollowing(int id) throws SQLException {
        TExecutor exec = new TExecutor();
        TResultHandler<List<String>> resultHandler = new TResultHandler<List<String>>(){

            public List<String> handle(ResultSet result) throws SQLException {
                List<String> list= new LinkedList<>();
                while(result.next()) {
                    list.add(result.getString(1));
                }
                return list;
            }
        };

        return exec.execQuery(getConnection(),
                "SELECT u.mail FROM Follows f JOIN Users u ON u.id=f.user WHERE f.follow=" + String.valueOf(id), resultHandler);
    }

    public List<String> getSubscriptions(int id) throws SQLException {
        TExecutor exec = new TExecutor();
        TResultHandler<List<String>> resultHandler = new TResultHandler<List<String>>(){

            public List<String> handle(ResultSet result) throws SQLException {
                List<String> list= new LinkedList<>();
                while(result.next()) {
                    list.add(result.getString(1));
                }
                return list;
            }
        };

        return exec.execQuery(getConnection(),
                "SELECT thread_id FROM Subscriptions WHERE user_id=" + String.valueOf(id), resultHandler);
    }

    public int createUser(UserData user) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        ValueStringBuilder vsb = new ValueStringBuilder("INSERT INTO Users (`username`, `mail`, `name`, `isAnonymous`, `about`) VALUES (");
        vsb.append(user.getUsername())
                .append(user.getMail())
                .append(user.getName())
                .append(user.isAnonymous())
                .append(user.getAbout())
                .close();

        System.out.println(vsb.toString());
        return exec.execUpdateAndReturnId(getConnection(), vsb.toString());
    }

    public int getForumIdByShortName(String sName) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<Integer> resultHandler = new TResultHandler<Integer>(){

            public Integer handle(ResultSet result) throws SQLException {
                result.next();
                return result.getInt(1);
            }
        };
        return exec.execQuery(getConnection(), "SELECT id FROM Forums WHERE short_name='" + sName +"'", resultHandler);
    }



    public ForumData getForumByShortName(String sName) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<ForumData> resultHandler = new TResultHandler<ForumData>(){

            public ForumData handle(ResultSet result) throws SQLException {
                result.next();
                return new ForumData(result.getInt(1), result.getString(2), result.getString(3),
                        result.getString(4));
            }
        };
        return exec.execQuery(getConnection(), "SELECT * FROM Forums WHERE short_name='" + sName + "'", resultHandler);
    }

    public ForumData getForumByName(String name) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<ForumData> resultHandler = new TResultHandler<ForumData>(){

            public ForumData handle(ResultSet result) throws SQLException {
                result.next();
                return new ForumData(result.getInt(1), result.getString(2), result.getString(3),
                        result.getString(4));
            }
        };
        return exec.execQuery(getConnection(), "SELECT * FROM Forums WHERE short_name='" + name + "'", resultHandler);
    }


    public int createForum(ForumData forum) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        ValueStringBuilder vsb = new ValueStringBuilder("INSERT INTO Forums (`user_mail`, `name`, `short_name`) VALUES (");
        vsb.append(forum.getUserMail())
                .append(forum.getName())
                .append(forum.getShort_name())
                .close();

        System.out.println(vsb.toString());
        return exec.execUpdateAndReturnId(getConnection(), vsb.toString());
    }




}

