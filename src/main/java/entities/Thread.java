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
        //related=['forum', 'user']&thread=1
        String[] expressions = query.split("&");
        boolean related_user = false;
        boolean related_forum = false;

        if(expressions[0] != null) {
            String[] values = expressions[0].split("=");
            related_user = (values[1].indexOf("user") != -1);
            related_forum = (values[1].indexOf("forum") != -1);
        }

        int id = Integer.parseInt(expressions[1].split("=")[1]);

        try {
            ThreadData threadData = dataService.getThreadById(id);

            JSONObject threadObj = threadData.toJson();
            threadObj.remove("user_id");
            threadObj.remove("forum_id");

            if(related_user) {
                UserData userData = dataService.getUserById(threadData.getUser_id());
                JSONObject userObj = userData.toJson();

                List<String> followers = dataService.getFollowers(userData.getId());
                List<String> following = dataService.getFollowing(userData.getId());
                List<String> subscriptions = dataService.getSubscriptions(userData.getId());

                userObj.put("following", following);
                userObj.put("followers", followers);
                userObj.put("subscriptions", subscriptions);

                threadObj.put("user", userObj);
            }
            else {
                threadObj.put("user", dataService.getUserMailById(threadData.getUser_id()));
            }

            if(related_forum) {
                ForumData forumData = dataService.getForumById(threadData.getForum_id());

                threadObj.put("forum", forumData.toJson());
            } else {
                threadObj.put("forum", dataService.getForumShortNameById(threadData.getForum_id()));
            }

        return JsonHelper.createResponse(threadObj).toJSONString();

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
