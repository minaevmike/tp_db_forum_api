package entities;

import dataSets.ForumData;
import dataSets.UserData;
import dataSets.parser.ForumParser;
import dbService.DataService;
import org.json.simple.JSONObject;
import utils.JsonHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Andrey on 22.04.14.
 */
public class Forum implements EntityInterface {

    DataService dataService;

    public Forum(DataService dataService)
    {
        this.dataService = dataService;
    }

    private String create(String data)
    {
        try {
            ForumParser forumParser = new ForumParser();
            forumParser.parse(data);
            ForumData forumData = forumParser.getResult();

            int id = dataService.createForum(forumData);
            forumData.setId(id);

            return JsonHelper.createResponse(forumData.toJson()).toJSONString();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String details(String query)
    {
        //related=['user']&forum=forumwithsufficientlylargename
        String[] expressions = query.split("&");
        boolean use_related = expressions[0].split("=")[1] == "['user']";
        String forumName = expressions[1].split("=")[1];

        try {
            ForumData forumData = dataService.getForumByName(forumName);

            JSONObject forumObj = forumData.toJson();

            if(use_related) {
                UserData userData = dataService.getUserByMail(forumData.getUserMail());
                JSONObject userObj = userData.toJson();

                List<String> followers = dataService.getFollowers(userData.getId());
                List<String> following = dataService.getFollowing(userData.getId());
                List<String> subscriptions = dataService.getSubscriptions(userData.getId());

                userObj.put("following", following);
                userObj.put("followers", followers);
                userObj.put("subscriptions", subscriptions);

                forumObj.remove("user");
                forumObj.put("user", userObj);
            }

        return JsonHelper.createResponse(forumObj).toJSONString();

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
