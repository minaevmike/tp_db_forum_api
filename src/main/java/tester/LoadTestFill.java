package tester;

import dataSets.UserData;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by Andrey
 * 20.05.14.
 */
public class LoadTestFill {
    static int TESTS = 1000000;
    static String url = "http://localhost/db/api/user/create";


    static void requestCreateUser(UserData userData) throws Exception
    {
<<<<<<< HEAD
        String url = "http://localhost:8081/db/api/user/create";
=======
>>>>>>> 5b4541b4df51aa6cf6e06f884f93d35306ae5de2
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("POST");
        String urlParameters = userData.toJson().toJSONString();

        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
    }


    public static void main(String[] args) throws Exception
    {
        for( int i = 0; i < TESTS; i++) {
            UserData userData = new UserData(
                -1,
                "username" + String.valueOf(i),
                "mail" + String.valueOf(i),
                "name" + String.valueOf(i),
                false,
                "about" + String.valueOf(i)
            );
            requestCreateUser(userData);
        }
    }
}
