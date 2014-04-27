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
public class User implements EntityInterface {

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
