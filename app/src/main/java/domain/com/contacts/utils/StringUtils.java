package domain.com.contacts.utils;

/**
 * Created by sumandas on 04/02/2017.
 */

public class StringUtils {
    public static String EMPTY = "";

    public static final int NAME_VALID_LENGTH = 3;

    public static final String PHONE_MATCHER = "((\\+91)|(0))?(\\d){12}";

    public static boolean isNullOrEmpty(String string) {
        return !isNotNullOrEmpty(string);
    }

    public static boolean isNotNullOrEmpty(String string) {
        return string != null && !string.equals(EMPTY);
    }

    public static boolean isValidName(String string) {
        return isNotNullOrEmpty(string) && string.length() >= NAME_VALID_LENGTH;
    }


    public static boolean isValidPhoneNumber(String phone) {
        if(isNotNullOrEmpty(phone)){
            return phone.matches(PHONE_MATCHER);
        }else{
            return false;
        }

    }

    public static boolean isValidEmail(String email) {
        return true;
    }
}
