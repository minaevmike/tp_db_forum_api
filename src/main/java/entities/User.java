package entities;

import dataSets.UserData;
import dataSets.UserParser;
import dbService.DataService;
import dbService.executor.SimpleExecutor;
import dbService.executor.TExecutor;
import dbService.handlers.TResultHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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
               "SELECT u.mail FROM Follows f JOIN Users u ON u.id=f.follow WHERE f.user=" + id, resultHandler);
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
                "SELECT user FROM Follows WHERE follow=" + id, resultHandler);
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
                "SELECT thread_id FROM Subscriptions WHERE user_id=" + id, resultHandler);
    }

    private void createUser(UserData user) throws SQLException
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


    private String create(String data)
    {
        JSONParser parser = new JSONParser();
        UserParser userParser = new UserParser();
        while(!userParser.isEnd()){
            try {
                parser.parse(data, userParser, true);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        try {
            createUser(userParser.getUserData());

            UserData created = getUserByMail(userParser.getUserData().getMail());
            JSONObject userObj = UserDataToJson(created);
            JSONObject resultJson = createResponse(userObj);

            System.out.println(resultJson.toJSONString());
            return resultJson.toJSONString();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String details(String query)
    {
        String[] pairs = query.split("=");
        String mail = pairs[1].replaceFirst("%40","@");
        try {
            UserData userData = getUserByMail(mail);
            List<String> followers = getFollowers(userData.getId());
            List<String> following = getFollowing(userData.getId());
            List<String> subscriptions = getSubscriptions(userData.getId());

            JSONObject obj = UserDataToJson(userData);
            obj.put("following", following);
            obj.put("followers", followers);
            obj.put("subscriptions", subscriptions);
            JSONObject resultJson = createResponse(obj);

            System.out.println(resultJson.toJSONString());
            return resultJson.toJSONString();

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

    private JSONObject UserDataToJson(UserData user)
    {
        JSONObject obj = new JSONObject();

        obj.put("username", user.getUsername());
        obj.put("about", user.getAbout());
        obj.put("isAnonymous", user.isAnonymous());
        obj.put("name", user.getName());
        obj.put("email", user.getMail());
        obj.put("id", user.getId());

        return obj;
    }

    private JSONObject createResponse(JSONObject obj)
    {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", 0);
        resultJson.put("response", obj);

        return resultJson;
    }


}
