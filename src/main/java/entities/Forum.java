package entities;

import dataSets.ForumData;
import dataSets.parser.ForumParser;
import dataSets.parser.GetRequestParser;
import dbService.DataService;
import org.json.simple.JSONObject;
import utils.JsonHelper;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Andrey
 * 22.04.14.
 */
public class Forum implements EntityInterface {

    DataService dataService;
    GetRequestParser GETParser= new GetRequestParser();

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
        GETParser.parse(query);
        String forumName = GETParser.getValue("forum");
        boolean use_related = GETParser.checkRelated("user");

        try {
            return JsonHelper.createResponse(dataService.getJsonForumDetails(forumName, use_related)).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private String listPosts(String query)
    {
        //related=['thread']&since=2014-01-02 00:00:00&limit=2&order=asc&forum=forumwithsufficientlylargename
        GETParser.parse(query);

        boolean related_user = GETParser.checkRelated("user");
        boolean related_thread = GETParser.checkRelated("thread");
        boolean related_forum = GETParser.checkRelated("forum");
        String limit = GETParser.getValue("limit");
        String order = GETParser.getValue("order");
        if( order == null) {
            order = "DESC";
        }
        String since = GETParser.getValue("since");


        String forum = GETParser.getValue("forum");


        try {
            List<JSONObject> result = new LinkedList<>();
            int id = dataService.getForumIdByShortName(forum);
            List<Integer> posts = dataService.getForumPostsIdList(id, since, order, limit);
            Iterator<Integer> postsIterator = posts.iterator();

            while (postsIterator.hasNext()) {
                result.add(dataService.getJsonPostDetails(postsIterator.next(), related_user, related_thread, related_forum));
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
            case "listPosts":
                return listPosts(data);
        }
        return null;
    }
}
