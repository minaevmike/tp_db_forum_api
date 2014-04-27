package entities;

import dataSets.ForumData;
import dataSets.PostData;
import dataSets.ThreadData;
import dataSets.UserData;
import dataSets.parser.ForumParser;
import dataSets.parser.GetRequestParser;
import dataSets.parser.PostParser;
import dataSets.parser.jsonDataSets.JsonPostData;
import dbService.DataService;
import org.json.simple.JSONObject;
import utils.JsonHelper;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrey on 22.04.14.
 */
public class Post implements EntityInterface {

    DataService dataService;
    GetRequestParser GETParser= new GetRequestParser();

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
        GETParser.parse(query);
        boolean related_user = GETParser.checkRelated("user");
        boolean related_forum = GETParser.checkRelated("forum");
        boolean related_thread = GETParser.checkRelated("thread");
        int id = Integer.parseInt(GETParser.getValue("post"));

        try {
            return JsonHelper.createResponse(dataService.getJsonPostDetails(id, related_user, related_thread, related_forum)).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    private String list(String query)
    {
        //since=2014-01-02 00:00:00&limit=2&order=asc&forum=forumwithsufficientlylargename
        GETParser.parse(query);

        String limit = GETParser.getValue("limit");
        String order = GETParser.getValue("order");
        if( order == null) {
            order = "DESC";
        }
        String since = GETParser.getValue("since");


        String forum = GETParser.getValue("forum");
        String thread = GETParser.getValue("thread");

        try {
            List<JSONObject> result = new LinkedList<>();
            List<Integer> posts = new LinkedList<>();
            if(forum != null) {
                int id = dataService.getForumIdByShortName(forum);
                posts = dataService.getForumPostsIdList(id, since, order, limit);
            } else {
                int id = Integer.parseInt(thread);
                posts = dataService.getThreadPostsIdList(id, since, order, limit);
            }
            Iterator<Integer> postsIterator = posts.iterator();

            while (postsIterator.hasNext()) {
                result.add(dataService.getJsonPostDetails(postsIterator.next(), false, false, false));
            }
            return  JsonHelper.createArrayResponse(result).toJSONString();

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
            case "list":
                return list(data);
        }
        return null;
    }
}
