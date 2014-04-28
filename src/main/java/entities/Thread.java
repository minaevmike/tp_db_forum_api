package entities;

import dataSets.ForumData;
import dataSets.ThreadData;
import dataSets.UserData;
import dataSets.parser.GetRequestParser;
import dataSets.parser.ThreadParser;
import dbService.DataService;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import utils.JsonHelper;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Andrey
 * 23.04.14.
 */
public class Thread implements EntityInterface {

    DataService dataService;
    GetRequestParser GETParser= new GetRequestParser();

    public Thread(DataService dataService)
    {
        this.dataService = dataService;
    }

    private String create(String data)
    {
        try {
            ThreadParser threadParser = new ThreadParser();
            threadParser.parse(data);

            int user_id = dataService.getUserIdByMail(threadParser.getResult().getUser());
            int forum_id = dataService.getForumIdByShortName(threadParser.getResult().getForum());
            ThreadData threadData = new ThreadData(threadParser.getResult(), user_id, forum_id);

            int id = dataService.createThread(threadData);
            threadData.setId(id);
            return JsonHelper.createResponse(threadData.toJson()).toJSONString();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String details(String query)
    {
        //related=['forum', 'user']&thread=1
        GETParser.parse(query);
        boolean related_user = GETParser.checkRelated("user");
        boolean related_forum = GETParser.checkRelated("forum");
        int id = Integer.parseInt(GETParser.getValue("thread"));

        try {
            return JsonHelper.createResponse(dataService.getJsonThreadDetails(id, related_user, related_forum )).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String listPosts(String query)
    {
        //since=2014-01-02 00:00:00&limit=2&order=asc&thread=4
        GETParser.parse(query);

        String limit = GETParser.getValue("limit");
        String order = GETParser.getValue("order");
        if( order == null) {
            order = "DESC";
        }
        String since = GETParser.getValue("since");

        int thread = Integer.parseInt(GETParser.getValue("thread"));

        try {
            List<JSONObject> result = new LinkedList<>();
            List<Integer> posts = dataService.getThreadPostsIdList(thread, since, order, limit);
            Iterator<Integer> postsIterator = posts.iterator();

            while (postsIterator.hasNext()) {
                result.add(dataService.getJsonPostDetails(postsIterator.next(), false, false, false));
            }
            return  JsonHelper.createArrayResponse(result).toJSONString();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String list(String query)
    {
        //since=2014-01-02 00:00:00&limit=2&order=asc&forum=forumwithsufficientlylargename
        GETParser.parse(query);

        String limit = GETParser.getValue("limit");
        String order = GETParser.getValue("order");
        if( order == null) {
            order = "DESC";
        }
        String since = GETParser.getValue("since");

        String forum = GETParser.getValue("forum");
        String user = GETParser.getValue("user");

        try {
            List<JSONObject> result = new LinkedList<>();
            List<Integer> threads;
            if(forum != null) {
                int id = dataService.getForumIdByShortName(forum);
                threads = dataService.getForumThreadsIdList(id, since, order, limit);
            } else {
                int id = dataService.getUserIdByMail(user);
                threads = dataService.getForumThreadsIdListByUser(id, since, order, limit);
            }
            Iterator<Integer> threadsIterator = threads.iterator();

            while (threadsIterator.hasNext()) {
                result.add(dataService.getJsonThreadDetails(threadsIterator.next(), false, false));
            }
            return  JsonHelper.createArrayResponse(result).toJSONString();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    private String remove(String data)
    {
        //thread=1
        JSONObject obj =(JSONObject) JSONValue.parse(data);
        Long id = (Long) obj.get("thread");

        try {
            dataService.removeThread(id.intValue());
            JSONObject res = new JSONObject();

            res.put("thread", id);
            return JsonHelper.createResponse(res).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    private String restore(String data)
    {
        //thread=1
        JSONObject obj =(JSONObject) JSONValue.parse(data);
        Long id = (Long) obj.get("thread");

        try {
            dataService.restoreThread(id.intValue());
            JSONObject res = new JSONObject();

            res.put("thread", id);
            return JsonHelper.createResponse(res).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String close(String data)
    {
        //thread=1
        JSONObject obj =(JSONObject) JSONValue.parse(data);
        Long id = (Long) obj.get("thread");

        try {
            dataService.closeThread(id.intValue());
            JSONObject res = new JSONObject();

            res.put("thread", id);
            return JsonHelper.createResponse(res).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String open(String data)
    {
        //thread=1
        JSONObject obj =(JSONObject) JSONValue.parse(data);
        Long id = (Long) obj.get("thread");

        try {
            dataService.openThread(id.intValue());
            JSONObject res = new JSONObject();

            res.put("thread", id);
            return JsonHelper.createResponse(res).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String update(String data)
    {
        //post=1
        JSONObject obj =(JSONObject)JSONValue.parse(data);
        Long id = (Long) obj.get("thread");
        String message = (String) obj.get("message");
        String slug = (String) obj.get("slug");

        try {
            dataService.updateThread(id.intValue(), message, slug);
            return JsonHelper.createResponse(dataService.getJsonThreadDetails(id.intValue(), false, false)).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    private String vote(String data)
    {
        //post=1
        JSONObject obj =(JSONObject)JSONValue.parse(data);
        Long id = (Long) obj.get("thread");
        Long vote = (Long) obj.get("vote");

        try {
            dataService.voteThread(id.intValue(), vote.intValue());
            return JsonHelper.createResponse(dataService.getJsonThreadDetails(id.intValue(), false, false)).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String subscribe(String data)
    {
        JSONObject obj =(JSONObject)JSONValue.parse(data);
        Long id = (Long) obj.get("thread");
        String user = (String) obj.get("user");

        try {
            int user_id = dataService.getUserIdByMail(user);
            dataService.subscribeUserToThread(id.intValue(),user_id);
            return JsonHelper.createResponse(obj).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String unsubscribe(String data)
    {
        JSONObject obj =(JSONObject)JSONValue.parse(data);
        Long id = (Long) obj.get("thread");
        String user = (String) obj.get("user");

        try {
            int user_id = dataService.getUserIdByMail(user);
            dataService.unsubscribeUserToThread(id.intValue(), user_id);
            return JsonHelper.createResponse(obj).toJSONString();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public String exec(String method, String data) {
        switch (method) {
            case "create":
                return create(data);
            case "details":
                return details(data);
            case "listPosts":
                return listPosts(data);
            case "list":
                return list(data);
            case "remove":
                return remove(data);
            case "restore":
                return restore(data);
            case "close":
                return close(data);
            case "open":
                return open(data);
            case "update":
                return update(data);
            case "vote":
                return vote(data);
            case "subscribe":
                return subscribe(data);
            case "unsubscribe":
                return unsubscribe(data);
        }
        return null;
    }
}
