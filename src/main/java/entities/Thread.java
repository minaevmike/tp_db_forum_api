package entities;

import dataSets.ForumData;
import dataSets.ThreadData;
import dataSets.UserData;
import dataSets.parser.ThreadParser;
import dbService.DataService;
import org.json.simple.JSONObject;
import utils.JsonHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Andrey
 * 23.04.14.
 */
public class Thread implements EntityInterface {

    DataService dataService;

    public Thread(DataService dataService)
    {
        this.dataService = dataService;
    }

    private String create(String data)
    {
        try {
            ThreadParser threadParser = new ThreadParser();
            threadParser.parse(data);

            int user_id = dataService.getUserIdByMail(threadParser.getResult().getUser());
            int forum_id = dataService.getForumIdByShortName(threadParser.getResult().getForum());
            ThreadData threadData = new ThreadData(threadParser.getResult(), user_id, forum_id);

            int id = dataService.createThread(threadData);
            threadData.setId(id);
            return JsonHelper.createResponse(threadData.toJson()).toJSONString();

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
