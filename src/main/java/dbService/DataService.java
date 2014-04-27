package dbService;

import dataSets.ForumData;
import dataSets.PostData;
import dataSets.ThreadData;
import dataSets.UserData;
import dbService.executor.SimpleExecutor;
import dbService.executor.TExecutor;
import dbService.handlers.TResultHandler;
import org.json.simple.JSONObject;
import utils.DateHelper;
import utils.JsonHelper;
import utils.ValueStringBuilder;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrey
 */
public class DataService {

    private Connection connection = null;

    public void connect()
    {
        try{
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            connection = DriverManager.getConnection(
                    "jdbc:mysql://" +
                    "localhost:" +
                    "3306/" +
                    "forum?" +
                    "user=sqadmin&" +
                    "password=sqadmin");

        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection()
    {
        return connection;
    }

    public String getUserMailById(int id) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<String> resultHandler = new TResultHandler<String>(){

            public String handle(ResultSet result) throws SQLException {
                result.next();
                return result.getString(1);
            }
        };
        return exec.execQuery(getConnection(), "SELECT mail FROM Users WHERE id=" + String.valueOf(id), resultHandler);
    }

    public int getUserIdByMail(String userMail) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<Integer> resultHandler = new TResultHandler<Integer>(){

            public Integer handle(ResultSet result) throws SQLException {
                result.next();
                return result.getInt(1);
            }
        };
        return exec.execQuery(getConnection(), "SELECT id FROM Users WHERE mail='" + userMail +"'", resultHandler);
    }


    public UserData getUserByMail(String userMail) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<UserData> resultHandler = new TResultHandler<UserData>(){

            public UserData handle(ResultSet result) throws SQLException {
                result.next();
                return new UserData(result.getInt(1), result.getString(2), result.getString(3), result.getString(4),
                        result.getBoolean(5), result.getString(6));
            }
        };
        return exec.execQuery(getConnection(), "SELECT * FROM Users WHERE mail='" + userMail +"'", resultHandler);
    }

    public UserData getUserById(int id) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<UserData> resultHandler = new TResultHandler<UserData>(){

            public UserData handle(ResultSet result) throws SQLException {
                result.next();
                return new UserData(result.getInt(1), result.getString(2), result.getString(3), result.getString(4),
                        result.getBoolean(5), result.getString(6));
            }
        };
        return exec.execQuery(getConnection(), "SELECT * FROM Users WHERE id=" + String.valueOf(id), resultHandler);
    }

    public List<String> getFollowers(int id) throws SQLException {
        TExecutor exec = new TExecutor();
        TResultHandler<List<String>> resultHandler = new TResultHandler<List<String>>(){

            public List<String> handle(ResultSet result) throws SQLException {
                List<String> list= new LinkedList<>();
                while(result.next()) {
                    list.add(result.getString(1));
                }
                return list;
            }
        };

        return exec.execQuery(getConnection(),
                "SELECT u.mail FROM Follows f JOIN Users u ON u.id=f.follow WHERE f.user=" + String.valueOf(id), resultHandler);
    }

    public List<String> getFollowing(int id) throws SQLException {
        TExecutor exec = new TExecutor();
        TResultHandler<List<String>> resultHandler = new TResultHandler<List<String>>(){

            public List<String> handle(ResultSet result) throws SQLException {
                List<String> list= new LinkedList<>();
                while(result.next()) {
                    list.add(result.getString(1));
                }
                return list;
            }
        };

        return exec.execQuery(getConnection(),
                "SELECT u.mail FROM Follows f JOIN Users u ON u.id=f.user WHERE f.follow=" + String.valueOf(id), resultHandler);
    }

    public List<String> getSubscriptions(int id) throws SQLException {
        TExecutor exec = new TExecutor();
        TResultHandler<List<String>> resultHandler = new TResultHandler<List<String>>(){

            public List<String> handle(ResultSet result) throws SQLException {
                List<String> list= new LinkedList<>();
                while(result.next()) {
                    list.add(result.getString(1));
                }
                return list;
            }
        };

        return exec.execQuery(getConnection(),
                "SELECT thread_id FROM Subscriptions WHERE user_id=" + String.valueOf(id), resultHandler);
    }

    public int createUser(UserData user) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        ValueStringBuilder vsb = new ValueStringBuilder("INSERT INTO Users (`username`, `mail`, `name`, `isAnonymous`, `about`) VALUES (");
        vsb.append(user.getUsername())
                .append(user.getMail())
                .append(user.getName())
                .append(user.isAnonymous())
                .append(user.getAbout())
                .close();

        System.out.println(vsb.toString());
        return exec.execUpdateAndReturnId(getConnection(), vsb.toString());
    }

    public String getForumShortNameById(int id) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<String> resultHandler = new TResultHandler<String>(){

            public String handle(ResultSet result) throws SQLException {
                result.next();
                return result.getString(1);
            }
        };
        return exec.execQuery(getConnection(), "SELECT short_name FROM Forums WHERE id=" + String.valueOf(id), resultHandler);
    }

    public int getForumIdByShortName(String sName) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<Integer> resultHandler = new TResultHandler<Integer>(){

            public Integer handle(ResultSet result) throws SQLException {
                result.next();
                return result.getInt(1);
            }
        };
        return exec.execQuery(getConnection(), "SELECT id FROM Forums WHERE short_name='" + sName +"'", resultHandler);
    }

    public ForumData getForumByName(String name) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<ForumData> resultHandler = new TResultHandler<ForumData>(){

            public ForumData handle(ResultSet result) throws SQLException {
                result.next();
                return new ForumData(result.getInt(1), result.getString(2), result.getString(3),
                        result.getString(4));
            }
        };
        return exec.execQuery(getConnection(), "SELECT * FROM Forums WHERE short_name='" + name + "'", resultHandler);
    }

    public ForumData getForumById(int id) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<ForumData> resultHandler = new TResultHandler<ForumData>(){

            public ForumData handle(ResultSet result) throws SQLException {
                result.next();
                return new ForumData(result.getInt(1), result.getString(2), result.getString(3),
                        result.getString(4));
            }
        };
        return exec.execQuery(getConnection(), "SELECT * FROM Forums WHERE id=" + String.valueOf(id), resultHandler);
    }

    public int createForum(ForumData forum) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        ValueStringBuilder vsb = new ValueStringBuilder("INSERT INTO Forums (`user_mail`, `name`, `short_name`) VALUES (");
        vsb.append(forum.getUserMail())
                .append(forum.getName())
                .append(forum.getShort_name())
                .close();

        System.out.println(vsb.toString());
        return exec.execUpdateAndReturnId(getConnection(), vsb.toString());
    }


    public List<Integer> getForumPostsIdList(int forum_id, String since, String order, String limit) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<List<Integer>> resultHandler = new TResultHandler<List<Integer>>(){

            public List<Integer> handle(ResultSet result) throws SQLException {
                List<Integer> list= new LinkedList<>();
                while(result.next()) {
                    list.add(result.getInt(1));
                }
                return list;
            }
        };

        StringBuilder sb = new StringBuilder("SELECT id FROM Posts WHERE forum_id=");
        sb.append(forum_id);
        if(since != null) {
            sb.append(" AND date >='").append(since).append("'");
        }
        sb.append(" ORDER BY date ").append(order);
        if (limit != null) {
            sb.append(" LIMIT ").append(limit);
        }
        System.out.println(sb.toString());
        return exec.execQuery(getConnection(), sb.toString(), resultHandler);
    }

    public List<Integer> getForumThreadsIdList(int forum_id, String since, String order, String limit) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<List<Integer>> resultHandler = new TResultHandler<List<Integer>>(){

            public List<Integer> handle(ResultSet result) throws SQLException {
                List<Integer> list= new LinkedList<>();
                while(result.next()) {
                    list.add(result.getInt(1));
                }
                return list;
            }
        };

        StringBuilder sb = new StringBuilder("SELECT id FROM Threads WHERE forum_id=");
        sb.append(forum_id);
        if(since != null) {
            sb.append(" AND date >='").append(since).append("'");
        }
        sb.append(" ORDER BY date ").append(order);
        if (limit != null) {
            sb.append(" LIMIT ").append(limit);
        }
        System.out.println(sb.toString());
        return exec.execQuery(getConnection(), sb.toString(), resultHandler);
    }


    public List<Integer> getForumUsersIdList(int forum_id, String since_id, String order, String limit) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<List<Integer>> resultHandler = new TResultHandler<List<Integer>>(){

            public List<Integer> handle(ResultSet result) throws SQLException {
                List<Integer> list= new LinkedList<>();
                while(result.next()) {
                    list.add(result.getInt(1));
                }
                return list;
            }
        };

        StringBuilder sb = new StringBuilder("SELECT DISTINCT user_id FROM Posts WHERE forum_id=");
        sb.append(forum_id);
        if(since_id != null) {
            sb.append(" AND user_id >='").append(since_id).append("'");
        }
        sb.append(" ORDER BY user_id ").append(order);
        if (limit != null) {
            sb.append(" LIMIT ").append(limit);
        }
        System.out.println(sb.toString());
        return exec.execQuery(getConnection(), sb.toString(), resultHandler);
    }


    public int createThread(ThreadData thread) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        ValueStringBuilder vsb = new ValueStringBuilder("INSERT INTO Threads (" +
        "`user_id`, `forum_id`, `date`, `likes`, `dislikes`,`message`, `points`, `slug`, `title`, `isDeleted`, `isClosed`) VALUES (");
        vsb.append(thread.getUser_id())
           .append(thread.getForum_id())
           .append(thread.getDate())
           .append(thread.getLikes())
           .append(thread.getDislikes())
           .append(thread.getMessage())
           .append(thread.getPoints())
           .append(thread.getSlug())
           .append(thread.getTitle())
           .append(thread.isDeleted())
           .append(thread.isClosed())
           .close();

        System.out.println(vsb.toString());
        return exec.execUpdateAndReturnId(getConnection(), vsb.toString());
    }

    public ThreadData getThreadById(int id) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<ThreadData> resultHandler = new TResultHandler<ThreadData>(){

            public ThreadData handle(ResultSet result) throws SQLException {
                result.next();
                return new ThreadData(result.getInt(1), result.getInt(2), result.getInt(3),
                        DateHelper.dateFromStr(result.getString(4)),
                        result.getInt(5), result.getInt(6), result.getString(7), result.getInt(8), result.getString(9),
                        result.getString(10), result.getBoolean(11), result.getBoolean(12));

            }
        };
        return exec.execQuery(getConnection(), "SELECT * FROM Threads WHERE id=" + String.valueOf(id), resultHandler);
    }

    public int countThreadPosts(int id) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<Integer> resultHandler = new TResultHandler<Integer>(){
            public Integer handle(ResultSet result) throws SQLException {
                result.next();
                return result.getInt(1);

            }
        };
        return exec.execQuery(getConnection(), "SELECT COUNT(id) FROM Posts WHERE thread_id=" + String.valueOf(id), resultHandler);
    }


    public List<Integer> getThreadPostsIdList(int thread_id, String since, String order, String limit) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<List<Integer>> resultHandler = new TResultHandler<List<Integer>>(){

            public List<Integer> handle(ResultSet result) throws SQLException {
                List<Integer> list= new LinkedList<>();
                while(result.next()) {
                    list.add(result.getInt(1));
                }
                return list;
            }
        };

        StringBuilder sb = new StringBuilder("SELECT id FROM Posts WHERE thread_id=");
        sb.append(thread_id);
        if(since != null) {
            sb.append(" AND date >='").append(since).append("'");
        }
        sb.append(" ORDER BY date ").append(order);
        if (limit != null) {
            sb.append(" LIMIT ").append(limit);
        }
        System.out.println(sb.toString());
        return exec.execQuery(getConnection(), sb.toString(), resultHandler);
    }




    public int createPost(PostData post) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        ValueStringBuilder vsb = new ValueStringBuilder("INSERT INTO Posts (" +
                "`thread_id`, `user_id`, `forum_id`, `parent_post`, `message`, `date`, `likes`, `dislikes`," +
                " `points`, `isApproved`, `isHighlighted`, `isEdited`, `isSpam`, `isDeleted`) VALUES(");
        vsb.append(post.getThread_id())
           .append(post.getUser_id())
           .append(post.getForum_id())
           .append(post.getParent_post())
           .append(post.getMessage())
           .append(post.getDate())
           .append(post.getLikes())
           .append(post.getDislikes())
           .append(post.getPoints())
           .append(post.isApproved())
           .append(post.isHighlighted())
           .append(post.isEdited())
           .append(post.isSpam())
           .append(post.isDeleted())
           .close();

        System.out.println(vsb.toString());
        return exec.execUpdateAndReturnId(getConnection(), vsb.toString());
    }

    private PostData makePostData(ResultSet result) throws SQLException {
        Object parentPostObj = result.getObject(5);
        Long parentPost = null;
        if(parentPostObj != null) {
            parentPost = result.getLong(5);
        }
        return new PostData(result.getInt(1), result.getInt(2), result.getInt(3), result.getInt(4),
                parentPost, result.getString(6), DateHelper.dateFromStr(result.getString(7)),
                result.getInt(8), result.getInt(9), result.getInt(10), result.getBoolean(11),
                result.getBoolean(12), result.getBoolean(13), result.getBoolean(14), result.getBoolean(15));
    }

    public PostData getPostById(int id) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<PostData> resultHandler = new TResultHandler<PostData>(){
            public PostData handle(ResultSet result) throws SQLException {
                result.next();
                return makePostData(result);
            }
        };
        return exec.execQuery(getConnection(), "SELECT * FROM Posts WHERE id=" + String.valueOf(id), resultHandler);
    }

    public boolean removePost(int id) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        ValueStringBuilder vsb = new ValueStringBuilder("UPDATE Posts SET isDeleted=TRUE WHERE id=");
        vsb.append(id);

        System.out.println(vsb.toString());
        exec.execUpdate(getConnection(), vsb.toString());
        return true;
    }

    public boolean restorePost(int id) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        ValueStringBuilder vsb = new ValueStringBuilder("UPDATE Posts SET isDeleted=FALSE WHERE id=");
        vsb.append(id);

        System.out.println(vsb.toString());
        exec.execUpdate(getConnection(), vsb.toString());
        return true;
    }

    public boolean updatePost(int id, String message) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        StringBuilder sb = new StringBuilder("UPDATE Posts SET message='");
        sb.append(message).append("'").append(" WHERE id=").append(id);

        System.out.println(sb.toString());
        exec.execUpdate(getConnection(), sb.toString());
        return true;
    }


    public boolean votePost(int id, int vote) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        StringBuilder sb = new StringBuilder("UPDATE Posts SET points=points");
        if (vote == 1) {
            sb.append("+1, likes=likes+1");
        } else {
            sb.append("-1, dislikes=dislikes+1");
        }
        sb.append(" WHERE id=").append(id);

        System.out.println(sb.toString());
        exec.execUpdate(getConnection(), sb.toString());
        return true;
    }

    public JSONObject getJsonUserDetails(String mail) throws SQLException
    {
        UserData userData = getUserByMail(mail);

        return userData.jsonDetails(getFollowers(userData.getId()),
                getFollowing(userData.getId()),
                getSubscriptions(userData.getId()));
    }

    public JSONObject getJsonUserDetails(int id) throws SQLException
    {
        UserData userData = getUserById(id);

        return userData.jsonDetails(getFollowers(userData.getId()),
                getFollowing(userData.getId()),
                getSubscriptions(userData.getId()));
    }


    public JSONObject getJsonThreadDetails(int id, boolean related_user, boolean related_forum) throws SQLException
    {
        ThreadData threadData = getThreadById(id);

        JSONObject threadObj = threadData.toJson();
        threadObj.remove("user_id");
        threadObj.remove("forum_id");

        threadObj.put("posts", countThreadPosts(threadData.getId()));

        if(related_user) {
            threadObj.put("user", getJsonUserDetails(threadData.getUser_id()));
        }
        else {
            threadObj.put("user", getUserMailById(threadData.getUser_id()));
        }

        if(related_forum) {
            threadObj.put("forum", getJsonForumDetails(threadData.getForum_id(), false));
        } else {
            threadObj.put("forum", getForumShortNameById(threadData.getForum_id()));
        }
        return threadObj;
    }

    public JSONObject getJsonForumDetails(String name, boolean related_user) throws SQLException
    {
        ForumData forumData = getForumByName(name);
        JSONObject forumObj = forumData.toJson();

        if(related_user) {
            JSONObject userObj = getJsonUserDetails(forumData.getUserMail());
            forumObj.remove("user");
            forumObj.put("user", userObj);
        }

        return forumObj;
    }

    public JSONObject getJsonForumDetails(int id, boolean related_user) throws SQLException
    {
        ForumData forumData = getForumById(id);
        JSONObject forumObj = forumData.toJson();

        if(related_user) {
            JSONObject userObj = getJsonUserDetails(forumData.getUserMail());
            forumObj.remove("user");
            forumObj.put("user", userObj);
        }

        return forumObj;
    }


    public JSONObject getJsonPostDetails(int id, boolean related_user, boolean related_thread , boolean related_forum) throws SQLException
    {
        PostData postData = getPostById(id);
        JSONObject postObj = postData.toJson();

        postObj.remove("user_id");
        postObj.remove("forum_id");

        if(related_user) {
            postObj.put("user", getJsonUserDetails(postData.getUser_id()));
        } else {
            postObj.put("user", getUserMailById(postData.getUser_id()));
        }

        if(related_forum) {
            postObj.put("forum", getJsonForumDetails(postData.getForum_id(), false));
        } else {
            postObj.put("forum",getForumShortNameById(postData.getForum_id()));
        }

        if(related_thread) {
            postObj.remove("thread");
            postObj.put("thread", getJsonThreadDetails(postData.getThread_id(), false, false));
        }

        return postObj;
    }


}

