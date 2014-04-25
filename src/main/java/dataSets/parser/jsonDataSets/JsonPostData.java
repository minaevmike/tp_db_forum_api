package dataSets.parser.jsonDataSets;

import java.util.Date;

/**
 * Created by Andrey
 * 25.04.14.
 */
public class JsonPostData {

    private Long parent_post = null;
    private boolean isApproved = false;
    private boolean isHighlighted = false;
    private boolean isEdited = false;
    private boolean isSpam = false;
    private boolean isDeleted = false;

    private Date date;
    private Long thread;
    private String message;
    private String user;
    private String forum;

    public Long getParent_post() {
        return parent_post;
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

    public Date getDate() {
        return date;
    }

    public Long getThread() {
        return thread;
    }

    public String getMessage() {
        return message;
    }

    public String getUser() {
        return user;
    }

    public String getForum() {
        return forum;
    }

    public void setParent_post(Long parent_post) {
        this.parent_post = parent_post;
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

    public void setDate(Date date) {
        this.date = date;
    }

    public void setThread(Long thread) {
        this.thread = thread;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }
}

