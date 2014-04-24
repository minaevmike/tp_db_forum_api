package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Andrey
 * 24.04.14.
 */
public class DateHelper {

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-DD HH:mm:ss");

    public static Date dateFromStr(String str)
    {
        Date date = null;
        try {
            date = format.parse(str);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateToStr(Date date)
    {
       return format.format(date);
    }
}
