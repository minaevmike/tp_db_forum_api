package entities;

import dataSets.UserData;
import dataSets.parser.GetRequestParser;
import dataSets.parser.UserParser;
import dbService.DataService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import utils.JsonHelper;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrey  21.04.14.
 */
public class User implements EntityInterface {

    DataService dataService;
    GetRequestParser GETParser = new GetRequestParser();

    public User(DataService dataService)
    {
        this.dataService = dataService;
    }

    private String create(String data)
    {
        try {
            UserParser userParser = new UserParser();
            userParser.parse(data);
            UserData userData = userParser.getResult();

            int id = dataService.createUser(userData);

            userData.setId(id);
            return JsonHelper.createResponse(userData.toJson()).toJSONString();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String details(String query)
    {
        String[] pairs = query.split("=");
        try {
            return JsonHelper.createResponse(dataService.getJsonUserDetails(pairs[1])).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String listPosts(String query)
    {
        //since=2014-01-02 00:00:00&limit=2&user=example@mail.ru&order=asc
        GETParser.parse(query);

        String limit = GETParser.getValue("limit");
        String order = GETParser.getValue("order");
        if( order == null) {
            order = "DESC";
        }
        String since = GETParser.getValue("since");

        String user = GETParser.getValue("user");

        try {
            List<JSONObject> result = new LinkedList<>();
            int id = dataService.getUserIdByMail(user);
            List<Integer> posts = dataService.getUserPostsIdList(id, since, order, limit);
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

    private String updateProfile(String data)
    {
        //{"about": "Wowowowow", "user": "example2@mail.ru", "name": "NewName"}
        JSONObject obj =(JSONObject) JSONValue.parse(data);
        String about = (String) obj.get("about");
        String name = (String) obj.get("name");
        String user = (String) obj.get("user");

        try {
            dataService.updateUser(user, name, about);
            return JsonHelper.createResponse(dataService.getJsonUserDetails(user)).toJSONString();
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
        case "updateProfile":
            return updateProfile(data);
        }
        return null;
    }
}
