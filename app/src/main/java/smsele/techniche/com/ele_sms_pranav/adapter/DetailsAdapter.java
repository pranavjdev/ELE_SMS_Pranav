package smsele.techniche.com.ele_sms_pranav.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import smsele.techniche.com.ele_sms_pranav.DetailsActivity;
import smsele.techniche.com.ele_sms_pranav.R;
import smsele.techniche.com.ele_sms_pranav.model.SmsModel;
import smsele.techniche.com.ele_sms_pranav.utils.DateTimeUtils;

/**
 * @author Pranav J.Dev E-mail : pranav@techniche.co
 *         Date : 16/5/17
 *         Module : ELE_SMS_Pranav.
 */

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    private Context context = null;
    private ArrayList<SmsModel> smsModels = null;
private String s = null;

    public DetailsAdapter(ArrayList<SmsModel> smsModels, Context context){
        this.context = context;
        this.smsModels = smsModels;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.message_details_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SmsModel smsModel = smsModels.get(position);
      //  holder.from.setText(smsModel.getFrom());
        holder.message.setText(smsModel.getMessage());
        String type = smsModel.getType();
        if(!TextUtils.isEmpty(type) && type.equalsIgnoreCase("sent")) {

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.linearLayout.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.linearLayout.setBackgroundResource(R.drawable.recive_background);
            holder.linearLayout.setLayoutParams(params);

        }else{
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)holder.linearLayout.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.linearLayout.setBackgroundResource(R.drawable.sendbackground);
            holder.linearLayout.setLayoutParams(params);
        }
        if(!TextUtils.isEmpty(smsModel.getDate()))
            holder.date.setText(DateTimeUtils.convertDate(smsModel.getDate(),"dd/MM/yyyy hh:mm:ss"));

    }



    @Override
    public int getItemCount() {
        return smsModels == null ? 0 : smsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //TextView from;
        TextView message;
        TextView date;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
          //  from = (TextView) itemView.findViewById(R.id.from);
            message = (TextView) itemView.findViewById(R.id.message);
            date = (TextView) itemView.findViewById(R.id.date);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}
