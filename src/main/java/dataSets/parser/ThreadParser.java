package dataSets.parser;

import dataSets.parser.jsonDataSets.JsonThreadData;
import org.json.simple.parser.ParseException;
import utils.DateHelper;

import java.io.IOException;
/**
 * Created by Andrey
 * 21.04.14.
 */
public class ThreadParser extends EntityParser{

    JsonThreadData jtData = new JsonThreadData();

    public JsonThreadData getResult()
    {
        return jtData;
    }

    public boolean primitive(Object value) throws ParseException, IOException {
        if(key != null){
            switch(key) {
            case "forum":
                jtData.setForum((String) value);
                break;
            case "title":
                jtData.setTitle((String) value);
                break;
            case "isClosed":
                jtData.setClosed((boolean) value);
                break;
            case "user":
                jtData.setUser((String) value);
                break;
            case "date":
                jtData.setDate(DateHelper.dateFromStr((String) value));
                break;
            case "message":
                jtData.setMessage((String) value);
                break;
            case "slug":
                jtData.setSlug((String) value);
                break;
            case "isDeleted":
                jtData.setDeleted((boolean) value);
                break;
            }
        }
        return true;
    }
}
