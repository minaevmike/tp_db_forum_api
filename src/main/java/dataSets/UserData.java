package dataSets;

/**
 * Created by Andrey on 21.04.14.
 */
public class UserData {

    private int id;
    private String about;
    private String username;
    private String mail;
    private String name;
    private boolean isAnonymous = false;

    public UserData(){}

    public UserData(int id, String about, String username, String mail, String name, boolean isAnonymous)
    {
        this.id = id;
        this.about = about;
        this.username = username;
        this.mail = mail;
        this.name = name;
        this.isAnonymous = isAnonymous;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setId(int id) {
        this.id = id;
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
}
