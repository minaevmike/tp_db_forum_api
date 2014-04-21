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
 * Created by Andrey on 21.04.14.
 */
public class User implements TableInterface {
/*  [2014-04-21 16:43:12] Requesting http://localhost:8080/db/api/user/create/ with
    {'username': 'user1', 'about': 'hello im user1', 'isAnonymous': False, 'name': '
    John', 'email': 'example@mail.ru'} (POST)
*/
    DataService ds;

    public User(DataService ds)
    {
        this.ds = ds;
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
        UserDAO  ud = new UserDAO(ds);
        try {
            ud.createUser(userParser.getUserData());

            UserData created = ud.getUserByMail(userParser.getUserData().getMail());
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


    @Override
    public String exec(String method, String data) {
        switch (method) {
        case "create":
            return create(data);
        }
        return null;
    }
}
