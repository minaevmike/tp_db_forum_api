package dataSets.parser;

import dataSets.UserData;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Created by Andrey
 * 21.04.14.
 */
public class UserParser extends EntityParser{

    UserData userData = new UserData();

    public UserData getResult()
    {
        return userData;
    }

    public boolean primitive(Object value) throws ParseException, IOException {
        if(key != null){
            switch(key) {
            case "username":
                userData.setUsername((String) value);
                break;
            case "about":
                userData.setAbout((String) value);
                break;
            case "isAnonymous":
                userData.setAnonymous((boolean) value);
                break;
            case "name":
                userData.setName((String) value);
                break;
            case "email":
                userData.setMail((String) value);
                break;
            }
        }
        return true;
    }
}
