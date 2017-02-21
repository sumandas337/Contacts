package domain.com.contacts.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sumandas on 29/01/2017.
 */
public class TimeUtils {


    public static final int TIMEOUT=30;

    public static String sTimeFormat = "yyyy-MM-dd HH:mm:ss";


    public static String getTimeNow(){
        SimpleDateFormat simpleDateFormat =
                new SimpleDateFormat(sTimeFormat);
        return simpleDateFormat.format(new Date());
    }
}
