package dataSets.parser;

import dataSets.UserData;
import org.json.simple.parser.ContentHandler;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

/**
 * Created by Andrey
 * 22.04.14.
 */
public abstract class EntityParser implements ContentHandler {
    private boolean end = false;
    protected String key;

    public abstract boolean primitive(Object value) throws ParseException, IOException;
    public abstract Object getResult();

    public void parse(String data)
    {
        JSONParser parser = new JSONParser();
        while(!this.isEnd()){
            try {
                parser.parse(data, this, true);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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
