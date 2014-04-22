package entities;

import dataSets.ForumData;
import dataSets.UserData;
import dataSets.parser.ForumParser;
import dbService.DataService;
import dbService.executor.SimpleExecutor;
import dbService.executor.TExecutor;
import dbService.handlers.TResultHandler;
import org.json.simple.JSONObject;
import utils.JsonHelper;
import utils.ValueStringBuilder;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Andrey on 22.04.14.
 */
public class Forum implements TableInterface {

    DataService dataService;

    public Forum(DataService dataService)
    {
        this.dataService = dataService;
    }

    private ForumData getForumByUserMail(String mail) throws SQLException
    {
        TExecutor exec = new TExecutor();
        TResultHandler<ForumData> resultHandler = new TResultHandler<ForumData>(){

            public ForumData handle(ResultSet result) throws SQLException {
                result.next();
                return new ForumData(result.getInt(1), result.getString(2), result.getString(3),
                        result.getString(4));
            }
        };
        return exec.execQuery(dataService.getConnection(), "SELECT * FROM Forums WHERE user_mail='" + mail + "'", resultHandler);
    }

    private void createForum(ForumData forum) throws SQLException
    {
        SimpleExecutor exec = new SimpleExecutor();
        ValueStringBuilder vsb = new ValueStringBuilder("INSERT INTO Forums (`user_mail`, `name`, `short_name`) VALUES (");
        vsb.append(forum.getUserMail())
           .append(forum.getName())
           .append(forum.getShort_name())
           .close();

        System.out.println(vsb.toString());
        exec.execUpdate(dataService.getConnection(), vsb.toString());
    }

    private String create(String data)
    {
        try {
            ForumParser forumParser = new ForumParser();
            forumParser.parse(data);

            createForum(forumParser.getResult());
            ForumData created = getForumByUserMail(forumParser.getResult().getUserMail());

            return JsonHelper.createResponse(created.toJson()).toJSONString();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String details(String data) {
        return null;
    }

    @Override
    public String exec(String method, String data) {
        switch (method) {
            case "create":
                return create(data);
            case "details":
                return details(data);
        }
        return null;
    }
}
