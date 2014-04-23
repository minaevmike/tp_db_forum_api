package dataSets.parser.jsonDataSets;

import java.util.Date;

/**
 * Created by Andrey
 * 23.04.14.
 */
public class JsonThreadData {

    private String user;
    private String forum;
    private Date date;
    private String message;
    private String slug;
    private String title;
    private boolean isDeleted = false;
    private boolean isClosed;


    public String getUser() {
        return user;
    }

    public String getForum() {
        return forum;
    }

    public Date getDate() {
        return date;
    }

    public String getMessage() {
        return message;
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

    public void setUser(String user) {
        this.user = user;
    }

    public void setForum(String forum) {
        this.forum = forum;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
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
}
