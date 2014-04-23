package dataSets;

import dataSets.parser.jsonDataSets.JsonThreadData;
import org.json.simple.JSONObject;
import java.util.Date;

/**
 * Created by Andrey
 * 21.04.14.
 */
public class ThreadData {

    private int id;
    private int user_id;
    private int forum_id;
    private Date date;
    private int likes;
    private int dislikes;
    private String message;
    private int points;
    private String slug;
    private String title;
    private boolean isDeleted = false;
    private boolean isClosed;

    public ThreadData(){}
    public ThreadData(JsonThreadData jData, int user_id, int forum_id)
    {
        this.user_id = user_id;
        this.forum_id = forum_id;
        this.date = jData.getDate();
        this.message = jData.getMessage();
        this.slug = jData.getSlug();
        this.title = jData.getTitle();
        this.isDeleted = jData.isDeleted();
        this.isClosed = jData.isClosed();

    }

    public ThreadData(int id, int user_id, int forum_id, Date date, int likes, int dislikes, String message, int points, String slug, String title, boolean isDeleted, boolean isClosed) {
        this.id = id;
        this.user_id = user_id;
        this.forum_id = forum_id;
        this.date = date;
        this.likes = likes;
        this.dislikes = dislikes;
        this.message = message;
        this.points = points;
        this.slug = slug;
        this.title = title;
        this.isDeleted = isDeleted;
        this.isClosed = isClosed;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getForum_id() {
        return forum_id;
    }

    public Date getDate() {
        return date;
    }

    public int getLikes() {
        return likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public String getMessage() {
        return message;
    }

    public int getPoints() {
        return points;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setForum_id(int forum_id) {
        this.forum_id = forum_id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void setClosed(boolean isClosed) {
        this.isClosed = isClosed;
    }

    public JSONObject toJson()
    {
        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("user_id", user_id);
        obj.put("forum_id", forum_id);
        obj.put("date", date);
        obj.put("likes", likes);
        obj.put("dislikes", dislikes);
        obj.put("message", message);
        obj.put("points", points);
        obj.put("slug", slug);
        obj.put("title", title);
        obj.put("isDeleted", isDeleted);
        obj.put("isClosed", isClosed);

        return obj;
    }

}
