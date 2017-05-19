package smsele.techniche.com.ele_sms_pranav.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import smsele.techniche.com.ele_sms_pranav.model.SmsModel;

/**
 * @author Pranav J.Dev E-mail : pranav@techniche.co
 *         Date : 16/5/17
 *         Module : ELE_SMS_Pranav.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_SMS = "sms";


    public static final String KEY_ID = "sms_id";
    public static final String KEY_FROM = "from_mob";
    public static final String KEY_DATE = "date_message";
    public static final String KEY_TYPE = "type_message";
    public static final String KEY_MESSAGE = "message_body";

    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "ele_sms";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE = "CREATE TABLE " + TABLE_SMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FROM + " TEXT,"
                + KEY_DATE + " TEXT ,"
                + KEY_MESSAGE + " TEXT ,"
                + KEY_TYPE + " TEXT)";


        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long createTable(SmsModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FROM,model.getFrom());
        values.put(KEY_DATE,model.getDate());
        values.put(KEY_TYPE,model.getType());
        values.put(KEY_MESSAGE,model.getMessage());
        long id = db.insert(TABLE_SMS, null, values);
        db.close();
        return id;
    }

    public ArrayList<SmsModel> getAllMessges(  String mobile) {
        ArrayList<SmsModel> models = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_SMS + " where from_mob='" + mobile+"' order by date_message asc";
        Log.e("---Querry", "" + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SmsModel smsModel = new SmsModel();
                smsModel.setFrom(cursor.getString(1));
                smsModel.setDate(cursor.getString(2));
                smsModel.setMessage(cursor.getString(3));
                smsModel.setType(cursor.getString(4));
                models.add(smsModel);
            } while (cursor.moveToNext());
        }

        db.close();
        return models;
    }

    public void clearSms(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_SMS);
        db.close();
    }
}
