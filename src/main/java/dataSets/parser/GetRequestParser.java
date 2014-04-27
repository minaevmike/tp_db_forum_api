package dataSets.parser;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andrey
 * 25.04.14.
 */
public class GetRequestParser {

    Map<String, String> expressions = new HashMap<String, String>();

    public void parse(String data)
    {
        if(!expressions.isEmpty())
            expressions.clear();

        String strings[] = data.split("&");
        for(int i = 0; i < strings.length; i++) {
            String values[] = strings[i].split("=");
            expressions.put(values[0],values[1]);
        }
    }

    public String getValue(String param)
    {
        return expressions.get(param);
    }

    public boolean checkRelated(String value)
    {
        String array = expressions.get("related");
        if (array != null)
            return array.indexOf(value) != -1;
        else
            return false;
    }


}
