package dataSets;

import org.json.simple.JSONObject;

/**
 * Created by Andrey
 * 22.04.14.
 */
public class ForumData {

    private int id;
    private String userMail;
    private String name;
    private String short_name;

    public ForumData() {
    }

    public ForumData(int id, String userMail, String short_name, String name) {
        this.id = id;
        this.userMail = userMail;
        this.short_name = short_name;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserMail() {
        return userMail;
    }

    public String getName() {
        return name;
    }

    public String getShort_name() {
        return short_name;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public JSONObject toJson()
    {
        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("name", name);
        obj.put("short_name", short_name);
        obj.put("user", userMail);

        return obj;
    }
}
