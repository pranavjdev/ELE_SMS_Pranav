package smsele.techniche.com.ele_sms_pranav.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {

    private Context context = null;
    private ArrayList<SmsModel> smsModels = null;


    public MessagesAdapter(ArrayList<SmsModel> smsModels,Context context){
        this.context = context;
        this.smsModels = smsModels;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sms, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SmsModel smsModel = smsModels.get(position);
        String count = smsModel.getCount();
        if(!TextUtils.isEmpty(count) && !count.equalsIgnoreCase("(1)"))
            holder.from.setText(smsModel.getFrom() + count);
        else
        holder.from.setText(smsModel.getFrom());

        holder.message.setText(smsModel.getMessage());
        if(!TextUtils.isEmpty(smsModel.getDate()))
            holder.date.setText(DateTimeUtils.convertDate(smsModel.getDate(),"dd/MM/yyyy hh:mm:ss"));
        holder.cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("from",smsModel.getFrom());
                context.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return smsModels == null ? 0 : smsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView from;
        TextView message;
        TextView date;
        CardView cardLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            from = (TextView) itemView.findViewById(R.id.from);
            message = (TextView) itemView.findViewById(R.id.message);
            date = (TextView) itemView.findViewById(R.id.date);
            cardLayout = (CardView) itemView.findViewById(R.id.cardLayout);
        }
    }
}
