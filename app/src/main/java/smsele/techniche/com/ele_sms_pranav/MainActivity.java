package smsele.techniche.com.ele_sms_pranav;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import smsele.techniche.com.ele_sms_pranav.adapter.MessagesAdapter;
import smsele.techniche.com.ele_sms_pranav.model.SmsModel;
import smsele.techniche.com.ele_sms_pranav.utils.DataBaseHelper;

public class MainActivity extends AppCompatActivity {

    private RecyclerView smsList = null;

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.READ_SMS
    };

    private static final int INITIAL_REQUEST = 13;

    private DataBaseHelper dataBaseHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        smsList = (RecyclerView) findViewById(R.id.smsList);
        smsList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        dataBaseHelper = new DataBaseHelper(this);
        dataBaseHelper.clearSms();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (canAccessMessages()) {

new LoadSms().execute();
            } else {
                ActivityCompat.requestPermissions(this, INITIAL_PERMS, INITIAL_REQUEST);
            }
        } else {
            new LoadSms().execute();
        }
    }


    private ArrayList<SmsModel> getMessages(HashMap<String, ArrayList<SmsModel>> sms) {
        ArrayList<SmsModel> list = new ArrayList<>();
        Iterator it = sms.entrySet().iterator();
        while (it.hasNext()) {
            SmsModel smsModel = new SmsModel();
            Map.Entry pair = (Map.Entry) it.next();
            //smsModel.setFrom((String)pair.getKey());
            ArrayList<SmsModel> smsList = (ArrayList<SmsModel>) pair.getValue();
            if (smsList != null && smsList.size() > 0) {
                smsModel.setMessage(smsList.get(0).getMessage());
                smsModel.setDate(smsList.get(0).getDate());
            }
            smsModel.setFrom((String) pair.getKey());
            smsModel.setCount("(" + (smsList.size() + 1) + ")");

            list.add(smsModel);
            it.remove(); // avoids a ConcurrentModificationException
        }
        return list;
    }

    private boolean canAccessMessages() {
        return (hasPermission(Manifest.permission.READ_SMS));
    }


    private boolean hasPermission(String perm) {

        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(this, perm));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case INITIAL_REQUEST:
                if (canAccessMessages()) {
                    new LoadSms().execute();
                } else {
                    Toast.makeText(MainActivity.this, "Please enable sms permission", Toast.LENGTH_SHORT).show();
                }

                break;


        }
    }

    private ArrayList<SmsModel> loadSmsFromPhone() {
        ArrayList<SmsModel> sms = new ArrayList();

        Uri uriSms = Uri.parse("content://sms/inbox");
        // Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"}, null, null, null);
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);

        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            SmsModel smsModel = new SmsModel();
            smsModel.setId(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
            smsModel.setFrom(cursor.getString(cursor.getColumnIndexOrThrow("address")));
            smsModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
            smsModel.setMessage(cursor.getString(cursor.getColumnIndexOrThrow("body")));

            if (cursor.getString(cursor.getColumnIndexOrThrow("type")).contains("1")) {
                smsModel.setType("inbox");
            } else {
                smsModel.setType("sent");
            }


            sms.add(smsModel);
        }
        return sms;
    }

    private HashMap<String, ArrayList<SmsModel>> getSmsFromList() {
        ArrayList<SmsModel> sms = new ArrayList();

        Uri uriSms = Uri.parse("content://sms/inbox");
        // Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"}, null, null, null);
        Cursor cursor = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, "date desc");
        HashMap<String, ArrayList<SmsModel>> smsList = new HashMap<>();
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            String from = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            SmsModel smsModel = new SmsModel();
            smsModel.setId(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
            smsModel.setFrom(from);
            smsModel.setDate(cursor.getString(cursor.getColumnIndexOrThrow("date")));
            smsModel.setMessage(cursor.getString(cursor.getColumnIndexOrThrow("body")));

            if (cursor.getString(cursor.getColumnIndexOrThrow("type")).contains("1")) {
                smsModel.setType("inbox");
            } else {
                smsModel.setType("sent");
            }

            dataBaseHelper.createTable(smsModel);
            if (smsList.containsKey(from)) {
                ArrayList<SmsModel> model = smsList.get(from);
                model.add(smsModel);
                smsList.put(from, model);
            } else {
                ArrayList<SmsModel> models = new ArrayList<>();
                models.add(smsModel);
                smsList.put(from, models);

            }


            sms.add(smsModel);
        }
        return smsList;
    }

    private class LoadSms extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pd = null;

        private ArrayList<SmsModel> allMessages = null;

        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, ArrayList<SmsModel>> sms = getSmsFromList();
            allMessages = getMessages(sms);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pd!=null)
                pd.dismiss();
            MessagesAdapter messagesAdapter = new MessagesAdapter(allMessages, MainActivity.this);
            smsList.setAdapter(messagesAdapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("loading");
            pd.show();
        }
    }
}
