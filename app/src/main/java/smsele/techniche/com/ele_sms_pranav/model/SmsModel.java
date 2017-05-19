package smsele.techniche.com.ele_sms_pranav.model;

/**
 * @author Pranav J.Dev E-mail : pranav@techniche.co
 *         Date : 16/5/17
 *         Module : ELE_SMS_Pranav.
 */

public class SmsModel {

    private String id = null;

    private String message = null;

    private String type = null;

    private String count = null;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String from = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String date = null;
}
