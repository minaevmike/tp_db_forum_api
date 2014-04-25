package entities;

import dataSets.ForumData;
import dataSets.PostData;
import dataSets.ThreadData;
import dataSets.UserData;
import dataSets.parser.ForumParser;
import dataSets.parser.PostParser;
import dataSets.parser.jsonDataSets.JsonPostData;
import dbService.DataService;
import org.json.simple.JSONObject;
import utils.JsonHelper;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Andrey on 22.04.14.
 */
public class Post implements EntityInterface {

    DataService dataService;

    public Post(DataService dataService)
    {
        this.dataService = dataService;
    }

    private String create(String data)
    {
        try {
            PostParser postParser = new PostParser();
            postParser.parse(data);
            JsonPostData jsonPostData  = postParser.getResult();

            int user_id = dataService.getUserIdByMail(jsonPostData.getUser());
            int forum_id = dataService.getForumIdByShortName(jsonPostData.getForum());

            PostData postData = new PostData(jsonPostData, user_id, forum_id);

            int id = dataService.createPost(postData);
            postData.setId(id);

            return JsonHelper.createResponse(postData.toJson()).toJSONString();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String details(String query)
    {
        //post=1&related=['thread', 'forum', 'user']
        String[] expressions = query.split("&");
        boolean related_user = false;
        boolean related_forum = false;
        boolean related_thread = false;

        if(expressions[1] != null) {
            String[] values = expressions[1].split("=");
            related_user = (values[1].indexOf("user") != -1);
            related_forum = (values[1].indexOf("forum") != -1);
            related_thread = (values[1].indexOf("thread") != -1);
        }

        int id = Integer.parseInt(expressions[0].split("=")[1]);
        try {
            PostData postData = dataService.getPostById(id);
            JSONObject postObj = postData.toJson();

            postObj.remove("user_id");
            postObj.remove("user_id");

            if(related_user) {
                UserData userData = dataService.getUserById(postData.getUser_id());
                JSONObject userObj = userData.toJson();

                List<String> followers = dataService.getFollowers(userData.getId());
                List<String> following = dataService.getFollowing(userData.getId());
                List<String> subscriptions = dataService.getSubscriptions(userData.getId());

                userObj.put("following", following);
                userObj.put("followers", followers);
                userObj.put("subscriptions", subscriptions);

                postObj.put("user", userObj);
            }
            else {
                postObj.put("user", dataService.getUserMailById(postData.getUser_id()));
            }


            if(related_forum) {
                ForumData forumData = dataService.getForumById(postData.getForum_id());

                postObj.put("forum", forumData.toJson());
            } else {
                postObj.put("forum", dataService.getForumShortNameById(postData.getForum_id()));
            }

            if(related_thread) {
                postObj.remove("thread");
                ThreadData threadData = dataService.getThreadById(postData.getThread_id());

                JSONObject threadObj = threadData.toJson();

                threadObj.put("forum", dataService.getForumShortNameById(threadData.getForum_id()));
                threadObj.put("posts", dataService.countThreadPosts(threadData.getId()));
                threadObj.put("user", dataService.getUserMailById(threadData.getUser_id()));

                postObj.put("thread", threadObj);
            }


            return JsonHelper.createResponse(postObj).toJSONString();

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
