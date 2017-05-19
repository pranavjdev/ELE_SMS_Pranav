package smsele.techniche.com.ele_sms_pranav.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import smsele.techniche.com.ele_sms_pranav.R;
import smsele.techniche.com.ele_sms_pranav.adapter.MessagesAdapter;
import smsele.techniche.com.ele_sms_pranav.model.SmsModel;
import smsele.techniche.com.ele_sms_pranav.utils.DataBaseHelper;
import smsele.techniche.com.ele_sms_pranav.utils.DateTimeUtils;

/**
 * @author Pranav J.Dev E-mail : pranav@techniche.co
 *         Date : 18/5/17
 *         Module : ELE_SMS_Pranav.
 */

public class PromotionsFragment extends Fragment {
    private View view = null;
    private RecyclerView smsList = null;

    private DataBaseHelper dataBaseHelper = null;

    public void setTitle(String title) {
        this.title = title;
    }

    private String title = null;

    public static PromotionsFragment newInstance(String title) {
        PromotionsFragment fragment = new PromotionsFragment();
        fragment.setTitle(title);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.promotion_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        smsList = (RecyclerView) view.findViewById(R.id.smsList);
        smsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        dataBaseHelper = new DataBaseHelper(getActivity());
        ArrayList<SmsModel> list = dataBaseHelper.getAllMessgesFromType(title);

        Set<SmsModel> noDups = new HashSet<SmsModel>();
        noDups.addAll(list);
        list = new ArrayList<SmsModel>(noDups);



        int index = 0;
        for (SmsModel model : list) {
            int count = dataBaseHelper.getMessageCount(model.getFrom());
            if (count > 1) {
                model.setCount("(" + count + ")");
                list.set(index,model);
            }
            index++;
        }
        MessagesAdapter messagesAdapter = new MessagesAdapter(list, getActivity());
        smsList.setAdapter(messagesAdapter);


    }


    private ArrayList<SmsModel> loadSmsFromPhone() {
        ArrayList<SmsModel> sms = new ArrayList();

        Uri uriSms = Uri.parse("content://sms/inbox");
        // Cursor cursor = getContentResolver().query(uriSms, new String[]{"_id", "address", "date", "body"}, null, null, null);
        Cursor cursor = getActivity().getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);

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
        Cursor cursor = getActivity().getContentResolver().query(Uri.parse("content://sms/"), null, null, null, "date desc");
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
