package smsele.techniche.com.ele_sms_pranav.utils;

import android.text.format.DateFormat;

import java.util.regex.Pattern;

/**
 * @author Pranav J.Dev E-mail : pranav@techniche.co
 *         Date : 16/5/17
 *         Module : ELE_SMS_Pranav.
 */

public class DateTimeUtils {

    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
    }

    public static boolean isValidMobile(String phone) {
        boolean check=false;
        if(android.util.Patterns.PHONE.matcher(phone).matches()) {
           check = true;
        } else {
            check=false;
        }
        return check;
    }
}
