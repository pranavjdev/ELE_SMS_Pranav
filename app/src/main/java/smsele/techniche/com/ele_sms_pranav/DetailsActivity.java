package smsele.techniche.com.ele_sms_pranav;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;

import smsele.techniche.com.ele_sms_pranav.adapter.DetailsAdapter;
import smsele.techniche.com.ele_sms_pranav.model.SmsModel;
import smsele.techniche.com.ele_sms_pranav.utils.DataBaseHelper;

/**
 * @author Pranav J.Dev E-mail : pranav@techniche.co
 *         Date : 17/5/17
 *         Module : ELE_SMS_Pranav.
 */

public class DetailsActivity extends AppCompatActivity {

    private RecyclerView smsList = null;
    private DataBaseHelper dataBaseHelper = null;
private TextView fromMessage = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        smsList = (RecyclerView) findViewById(R.id.smsList);
        fromMessage = (TextView) findViewById(R.id.fromMessage);
        smsList.setLayoutManager(new LinearLayoutManager(DetailsActivity.this));
        dataBaseHelper = new DataBaseHelper(DetailsActivity.this);

        String from = null;
        try {
            from = getIntent().getExtras().getString("from");
        } catch (Exception ex) {

        }
        if(!TextUtils.isEmpty(from)){
            fromMessage.setText(from);
            ArrayList<SmsModel> list =  dataBaseHelper.getAllMessges(from);
            DetailsAdapter detailsAdapter = new DetailsAdapter(list,DetailsActivity.this);
            smsList.setAdapter(detailsAdapter);
        }
    }
}
