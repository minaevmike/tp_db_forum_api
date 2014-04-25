package dataSets;

import dataSets.parser.jsonDataSets.JsonPostData;
import org.json.simple.JSONObject;
import utils.DateHelper;

import java.util.Date;

/**
 * Created by Andrey on 25.04.14.
 */
public class PostData {

    private int id;
    private int thread_id;
    private int user_id;
    private int forum_id;
    private Long parent_post = null;
    private String message;
    private Date date;
    private int likes = 0;
    private int dislikes = 0;
    private int points = 0;
    private boolean isApproved = false;
    private boolean isHighlighted = false;
    private boolean isEdited = false;
    private boolean isSpam = false;
    private boolean isDeleted = false;

    public PostData(int id, int thread_id, int user_id, int forum_id, Long parent_post, String message, Date date, int likes, int dislikes, int points, boolean isApproved, boolean isHighlighted, boolean isEdited, boolean isSpam, boolean isDeleted) {
        this.id = id;
        this.thread_id = thread_id;
        this.user_id = user_id;
        this.forum_id = forum_id;
        this.parent_post = parent_post;
        this.message = message;
        this.date = date;
        this.likes = likes;
        this.dislikes = dislikes;
        this.points = points;
        this.isApproved = isApproved;
        this.isHighlighted = isHighlighted;
        this.isEdited = isEdited;
        this.isSpam = isSpam;
        this.isDeleted = isDeleted;
    }

    public PostData(JsonPostData jpd, int user_id, int forum_id) {
        parent_post = jpd.getParent_post();
        message = jpd.getMessage();

        isApproved = jpd.isApproved();
        isHighlighted = jpd.isHighlighted();
        isEdited = jpd.isEdited();
        isSpam = jpd.isSpam();
        isDeleted = jpd.isDeleted();

        date = jpd.getDate();
        thread_id = jpd.getThread().intValue();

        this.user_id = user_id;
        this.forum_id = forum_id;
    }

    public int getId() {
        return id;
    }

    public int getThread_id() {
        return thread_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getForum_id() {
        return forum_id;
    }

    public Long getParent_post() {
        return parent_post;
    }

    public String getMessage() {
        return message;
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

    public int getPoints() {
        return points;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public boolean isSpam() {
        return isSpam;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setThread_id(int thread_id) {
        this.thread_id = thread_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setForum_id(int forum_id) {
        this.forum_id = forum_id;
    }

    public void setParent_post(Long parent_post) {
        this.parent_post = parent_post;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public void setPoints(int points) {
        this.points = points;
    }

    public void setApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public void setHighlighted(boolean isHighlighted) {
        this.isHighlighted = isHighlighted;
    }

    public void setEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }

    public void setSpam(boolean isSpam) {
        this.isSpam = isSpam;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public JSONObject toJson()
    {
        JSONObject obj = new JSONObject();

        obj.put("id", id);
        obj.put("thread", thread_id);
        obj.put("user_id", user_id);
        obj.put("forum_id", forum_id);
        obj.put("parent", parent_post);
        obj.put("date",DateHelper.dateToStr(date));
        obj.put("likes", likes);
        obj.put("dislikes", dislikes);
        obj.put("message", message);
        obj.put("points", points);
        obj.put("isApproved", isApproved);
        obj.put("isHighlighted", isHighlighted);
        obj.put("isEdited", isEdited);
        obj.put("isSpam", isSpam);
        obj.put("isDeleted", isDeleted);

        return obj;
    }
}
