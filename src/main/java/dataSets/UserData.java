package dataSets;

import org.json.simple.JSONObject;

import java.util.List;

/**
 * Created by Andrey
 * 21.04.14.
 */
public class UserData {

    private int id;
    private String about;
    private String username;
    private String mail;
    private String name;
    private boolean isAnonymous = false;

    public UserData(){}

    public UserData(int id, String username, String mail, String name, boolean isAnonymous, String about)
    {
        this.id = id;
        this.about = about;
        this.username = username;
        this.mail = mail;
        this.name = name;
        this.isAnonymous = isAnonymous;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public int getId() {

        return id;
    }

    public String getAbout() {

        return about;
    }

    public String getUsername() {
        return username;
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public JSONObject toJson()
    {
        JSONObject obj = new JSONObject();

        obj.put("username", username);
        obj.put("about", about);
        obj.put("isAnonymous", isAnonymous);
        obj.put("name", name);
        obj.put("email", mail);
        obj.put("id", id);

        return obj;
    }

    public JSONObject jsonDetails(List<String> followers, List<String> following, List<Integer> subscriptions)
    {
        JSONObject obj = this.toJson();
        obj.put("followers", followers);
        obj.put("following", following);
        obj.put("subscriptions", subscriptions);
        return obj;
    }
}
