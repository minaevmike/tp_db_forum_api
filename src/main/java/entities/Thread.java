package entities;

import dataSets.ForumData;
import dataSets.ThreadData;
import dataSets.UserData;
import dataSets.parser.GetRequestParser;
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
    GetRequestParser GETParser= new GetRequestParser();

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
        GETParser.parse(query);
        boolean related_user = GETParser.checkRelated("user");
        boolean related_forum = GETParser.checkRelated("forum");
        int id = Integer.parseInt(GETParser.getValue("thread"));

        try {
            return JsonHelper.createResponse(dataService.getJsonThreadDetails(id, related_user, related_forum )).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }



    private String listPosts(String query)
    {
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
