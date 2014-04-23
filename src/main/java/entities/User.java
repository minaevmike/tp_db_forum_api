package entities;

import dataSets.UserData;
import dataSets.parser.UserParser;
import dbService.DataService;
import org.json.simple.JSONObject;
import utils.JsonHelper;

import java.sql.SQLException;
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

    private String create(String data)
    {
        try {
            UserParser userParser = new UserParser();
            userParser.parse(data);

            dataService.createUser(userParser.getResult());

            UserData created = dataService.getUserByMail(userParser.getResult().getMail());

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
            UserData userData = dataService.getUserByMail(pairs[1]);
            List<String> followers = dataService.getFollowers(userData.getId());
            List<String> following = dataService.getFollowing(userData.getId());
            List<String> subscriptions = dataService.getSubscriptions(userData.getId());

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
