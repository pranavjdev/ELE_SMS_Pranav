package smsele.techniche.com.ele_sms_pranav;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import smsele.techniche.com.ele_sms_pranav.adapter.ViewPagerAdapter;
import smsele.techniche.com.ele_sms_pranav.fragments.PromotionsFragment;
import smsele.techniche.com.ele_sms_pranav.model.SmsModel;
import smsele.techniche.com.ele_sms_pranav.utils.DataBaseHelper;
import smsele.techniche.com.ele_sms_pranav.utils.DateTimeUtils;

public class MainActivity extends AppCompatActivity {


    private TabLayout tabLayout = null;
    private DataBaseHelper dataBaseHelper = null;
    private ViewPager viewPager = null;
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.READ_SMS
    };

    private static final int INITIAL_REQUEST = 13;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        dataBaseHelper = new DataBaseHelper(MainActivity.this);
        dataBaseHelper.clearSms();
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (canAccessMessages()) {
                new CreateSmsTable().execute();
            } else {
                ActivityCompat.requestPermissions(this,INITIAL_PERMS, INITIAL_REQUEST);
            }
        } else {

            new CreateSmsTable().execute();
        }




    }
    private boolean canAccessMessages() {
        return (hasPermission(Manifest.permission.READ_SMS));
    }


    private boolean hasPermission(String perm) {

        return (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(MainActivity.this, perm));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case INITIAL_REQUEST:
                if (canAccessMessages()) {
                    new CreateSmsTable().execute();
                } else {
                    Toast.makeText(MainActivity.this, "Please enable sms permission", Toast.LENGTH_SHORT).show();
                }

                break;


        }
    }

    private class CreateSmsTable extends AsyncTask<Void, Void, Void> {

        private ProgressDialog pd = null;


        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, ArrayList<SmsModel>> sms = getSmsFromList();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (pd != null)
                pd.dismiss();

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFrag(new PromotionsFragment().newInstance("Promotion"), "Promotion");
            adapter.addFrag(new PromotionsFragment().newInstance("Transaction"), "Transaction");
            adapter.addFrag(new PromotionsFragment().newInstance("All"), "All");
            viewPager.setOffscreenPageLimit(3);
            viewPager.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("loading");
            pd.show();
        }
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
                smsModel.setDirection("inbox");
            } else {
                smsModel.setDirection("sent");
            }

            if (DateTimeUtils.isValidMobile(from)) {
                smsModel.setType("Transaction");
            } else {
                smsModel.setType("Promotion");
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
}
