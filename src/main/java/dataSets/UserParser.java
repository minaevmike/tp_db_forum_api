package dataSets;

import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Created by Andrey on 21.04.14.
 */
public class UserParser implements ContentHandler{
    private Object value;
    private boolean end = false;
    private String key;

    private UserData userData = new UserData();

    public UserData getUserData()
    {
        return userData;
    }

    public Object getValue(){
        return value;
    }

    public boolean isEnd(){
        return end;
    }

    public void startJSON() throws ParseException, IOException {
        end = false;
    }

    public void endJSON() throws ParseException, IOException {
        end = true;
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

    public boolean startArray() throws ParseException, IOException {
        return true;
    }


    public boolean startObject() throws ParseException, IOException {
        return true;
    }

    public boolean startObjectEntry(String key) throws ParseException, IOException {
        this.key = key;
        return true;
    }

    public boolean endArray() throws ParseException, IOException {
        return false;
    }

    public boolean endObject() throws ParseException, IOException {
        return true;
    }

    public boolean endObjectEntry() throws ParseException, IOException {
        return true;
    }
}
