package utils;

import org.json.simple.JSONObject;

import java.util.List;

/**
 * Created by Andrey
 * 22.04.14.
 */
public class JsonHelper {
    public static JSONObject createResponse(JSONObject obj)
    {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", 0);
        resultJson.put("response", obj);

        return resultJson;
    }


    public static <T> JSONObject createArrayResponse(List<T> obj)
    {
        JSONObject resultJson = new JSONObject();
        resultJson.put("code", 0);
        resultJson.put("response", obj);

        return resultJson;
    }
}
