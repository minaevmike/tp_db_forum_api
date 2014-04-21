package entities;

import dataSets.UserData;
import dataSets.UserParser;
import dbService.DataService;
import dbService.dao.UserDAO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.sql.SQLException;

/**
 * Created by Andrey  21.04.14.
 */
public class User implements TableInterface {

    DataService ds;
    UserDAO dao;

    public User(DataService ds)
    {
        this.ds = ds;
        dao = new UserDAO(ds);
    }

    private String create(String data)
    {
        JSONParser parser = new JSONParser();
        UserParser userParser = new UserParser();
        while(!userParser.isEnd()){
            try {
                parser.parse(data, userParser, true);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        try {
            dao.createUser(userParser.getUserData());

            UserData created = dao.getUserByMail(userParser.getUserData().getMail());
            JSONObject obj = new JSONObject();
            JSONObject resultJson = new JSONObject();

            obj.put("username", created.getUsername());
            obj.put("about", created.getAbout());
            obj.put("isAnonymous", created.isAnonymous());
            obj.put("name", created.getName());
            obj.put("email", created.getMail());
            obj.put("id", created.getId());

            resultJson.put("code", 0);
            resultJson.put("response", obj);

            System.out.print(resultJson.toJSONString());

            return resultJson.toJSONString();


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String details(String query)
    {
        String[] pairs = query.split("=");
        UserData userData = null;
        try {
            userData = dao.getUserByMail(pairs[1]);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(userData != null) {

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
