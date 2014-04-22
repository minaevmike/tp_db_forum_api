package dataSets.parser;

import dataSets.ForumData;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Created by Andrey
 * 21.04.14.
 */
public class ForumParser extends EntityParser{

    ForumData forumData = new ForumData();

    public ForumData getResult()
    {
        return forumData;
    }

    public boolean primitive(Object value) throws ParseException, IOException {
        if(key != null){
            switch(key) {
                case "name":
                    forumData.setName((String) value);
                    break;
                case "short_name":
                    forumData.setShort_name((String) value);
                    break;
                case "user":
                    forumData.setUserMail((String) value);
                    break;
            }
        }
        return true;
    }
}