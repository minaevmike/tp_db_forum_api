package dataSets.parser;

import dataSets.parser.jsonDataSets.JsonPostData;
import org.json.simple.parser.ParseException;
import utils.DateHelper;

import java.io.IOException;

/**
 * Created by Andrey
 * 21.04.14.
 */
public class PostParser extends EntityParser{

    JsonPostData jpData = new JsonPostData();

    public JsonPostData getResult()
    {
        return jpData;
    }

    public boolean primitive(Object value) throws ParseException, IOException {
        if(key != null){
            switch(key) {
            case "parent":
                jpData.setParent_post((Long) value);
                break;
            case "isApproved":
                jpData.setApproved((boolean) value);
                break;
            case "isHighlighted":
                jpData.setHighlighted((boolean) value);
                break;
            case "isEdited":
                jpData.setEdited((boolean) value);
                break;
            case "isSpam":
                jpData.setSpam((boolean) value);
                break;
            case "isDeleted":
                jpData.setDeleted((boolean) value);
                break;
            case "date":
                jpData.setDate(DateHelper.dateFromStr((String) value));
                break;
            case "thread":
                jpData.setThread((Long) value);
                break;
            case "message":
                jpData.setMessage((String) value);
                break;
            case "user":
                jpData.setUser((String) value);
                break;
            case "forum":
                jpData.setForum((String) value);
                break;
            }
        }
        return true;
    }
}
