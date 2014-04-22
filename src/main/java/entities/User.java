package entities;

import dataSets.UserData;
import dataSets.parser.UserParser;
import dbService.DataService;
import dbService.executor.SimpleExecutor;
import dbService.executor.TExecutor;
import dbService.handlers.TResultHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import utils.JsonHelper;
import utils.ValueStringBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrey  21.04.14.
 */
public class User implements TableInterface {

    DataService dataService;

    public User(DataService dataService)
    {
        this.dataService = dataService;
    }

    private UserData getUserByMail(String userMail) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<UserData> resultHandler = new TResultHandler<UserData>(){

            public UserData handle(ResultSet result) throws SQLException {
                result.next();
                return new UserData(result.getInt(1), result.getString(2), result.getString(3), result.getString(4),
                        result.getBoolean(5), result.getString(6));
            }
        };
        return exec.execQuery(dataService.getConnection(), "SELECT * FROM Users WHERE mail='" + userMail +"'", resultHandler);
    }

    private List<String> getFollowers(int id) throws SQLException {
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

        return exec.execQuery(dataService.getConnection(),
               "SELECT u.mail FROM Follows f JOIN Users u ON u.id=f.follow WHERE f.user=" + String.valueOf(id), resultHandler);
    }

    private List<String> getFollowing(int id) throws SQLException {
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

        return exec.execQuery(dataService.getConnection(),
                "SELECT u.mail FROM Follows f JOIN Users u ON u.id=f.user WHERE f.follow=" + String.valueOf(id), resultHandler);
    }

    private List<String> getSubscriptions(int id) throws SQLException {
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

        return exec.execQuery(dataService.getConnection(),
                "SELECT thread_id FROM Subscriptions WHERE user_id=" + String.valueOf(id), resultHandler);
    }

    private void createUser(UserData user) throws SQLException
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
        exec.execUpdate(dataService.getConnection(), vsb.toString());
    }


    private String create(String data)
    {
        try {
            UserParser userParser = new UserParser();
            userParser.parse(data);

            createUser(userParser.getResult());

            UserData created = getUserByMail(userParser.getResult().getMail());

            return JsonHelper.createResponse(created.toJson()).toJSONString();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String details(String query)
    {
        String[] pairs = query.split("=");
        try {
            UserData userData = getUserByMail(pairs[1]);
            List<String> followers = getFollowers(userData.getId());
            List<String> following = getFollowing(userData.getId());
            List<String> subscriptions = getSubscriptions(userData.getId());

            JSONObject obj = userData.toJson();
            obj.put("following", following);
            obj.put("followers", followers);
            obj.put("subscriptions", subscriptions);

            return JsonHelper.createResponse(obj).toJSONString();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String exec(String method, String data) {
        switch (method) {
        case "create":
            return create(data);
        case "details":
            return details(data);
        }
        return null;
    }
}
