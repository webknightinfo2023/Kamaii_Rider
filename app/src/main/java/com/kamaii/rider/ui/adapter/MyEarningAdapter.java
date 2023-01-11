package com.kamaii.rider.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.rider.R;
import com.kamaii.rider.ui.models.EarningDataModel;
import com.kamaii.rider.utils.CustomTextView;

import org.tensorflow.lite.support.metadata.schema.Content;

import java.util.List;

public class MyEarningAdapter extends RecyclerView.Adapter<MyEarningAdapter.EarningViewHolder> {

    Context context;
    List<EarningDataModel> arraylist;

    public MyEarningAdapter(Context context, List<EarningDataModel> arraylist) {
        this.context = context;
        this.arraylist = arraylist;
    }

    @NonNull
    @Override
    public EarningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.earning_recycle_layout, parent, false);
        return new EarningViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EarningViewHolder holder, int position) {

        holder.earning_recycle_date.setText(Html.fromHtml("<b> Date : </b>"+arraylist.get(position).getDate()));
        holder.online_hrs.setText(arraylist.get(position).getTotalhours());
        holder.order_complete_digit.setText(arraylist.get(position).getTotalcompletedbooking());
        holder.order_cancel_digit.setText(arraylist.get(position).getTotalcancelbooking());
        holder.earning_amount.setText(arraylist.get(position).getTotalearning());
        holder.incentive_amt.setText(arraylist.get(position).getTotal_rider_incentive());
        holder.adjust_amt.setText(arraylist.get(position).getTotaladjust());
        holder.earning_recycle_status.setText(Html.fromHtml("<b> Status : </b>"+arraylist.get(position).getReason()));
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    class EarningViewHolder extends RecyclerView.ViewHolder {

        CustomTextView earning_recycle_status,earning_recycle_date, online_hrs, order_complete_digit, order_cancel_digit, earning_amount, incentive_amt, adjust_amt;

        public EarningViewHolder(@NonNull View itemView) {
            super(itemView);
            earning_recycle_date = itemView.findViewById(R.id.earning_recycle_date);
            online_hrs = itemView.findViewById(R.id.online_hrs);
            order_complete_digit = itemView.findViewById(R.id.order_complete_digit);
            order_cancel_digit = itemView.findViewById(R.id.order_cancel_digit);
            earning_amount = itemView.findViewById(R.id.earning_amount);
            incentive_amt = itemView.findViewById(R.id.incentive_amt);
            adjust_amt = itemView.findViewById(R.id.adjust_amt);
            earning_recycle_status = itemView.findViewById(R.id.earning_recycle_status);

        }
    }
}
