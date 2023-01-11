package com.kamaii.rider.ui.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.rider.DTO.HistoryDTO;
import com.kamaii.rider.Glob;
import com.kamaii.rider.R;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.ViewInvoice;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ProjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class PaidAdapter extends RecyclerView.Adapter<PaidAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<HistoryDTO> objects =null;
    ArrayList<HistoryDTO> historyDTOList;
    private SharedPrefrence prefrence;
    private LayoutInflater inflater;

    public PaidAdapter(Context mContext, ArrayList<HistoryDTO> objects, LayoutInflater inflater) {
        this.mContext = mContext;
        this.objects = objects;
        this.historyDTOList = new ArrayList<HistoryDTO>();
        this.historyDTOList.addAll(objects);
        this.inflater = inflater;
        prefrence = SharedPrefrence.getInstance(mContext);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater
                .inflate(R.layout.adapter_paid, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.CTVBservice.setText(mContext.getResources().getString(R.string.service)+" " + objects.get(position).getInvoice_id());
        try {
            holder.CTVdate.setText(ProjectUtils.convertTimestampDateToTime(ProjectUtils.correctTimestamp(Long.parseLong(objects.get(position).getCreated_at()))));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.CTVprice.setText(objects.get(position).getCurrency_type() + objects.get(position).getFinal_amount());
        holder.CTVServicetype.setText(objects.get(position).getCategoryName());
       // holder.CTVwork.setText(objects.get(position).getProduct());
        holder.CTVname.setText(ProjectUtils.getFirstLetterCapital(objects.get(position).getUserName()));

        Glide.with(mContext).
                load(objects.get(position).getUserImage())
                .placeholder(R.drawable.dummyuser_image)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.IVprofile);
        if (objects.get(position).getFlag().equalsIgnoreCase("0")) {
            holder.tvStatus.setText(mContext.getResources().getString(R.string.unpaid));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_red));
        } else if (objects.get(position).getFlag().equalsIgnoreCase("1")) {
            holder.tvStatus.setText(mContext.getResources().getString(R.string.paid));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_green));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("mm.ss");

        try {
            Date dt = sdf.parse(objects.get(position).getWorking_min());
            sdf = new SimpleDateFormat("HH:mm:ss");

            holder.CTVTime.setText(mContext.getResources().getString(R.string.duration)+" " + sdf.format(dt));

        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.tvViewpaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glob.BUBBLE_VALUE ="1";
                Intent in = new Intent(mContext, ViewInvoice.class);
                in.putExtra(Consts.BOOKING_ID, objects.get(position).getInvoice_id());
                in.putExtra(Consts.HISTORY_DTO, objects.get(position));
                mContext.startActivity(in);

            }
        });

    }

    @Override
    public int getItemCount() {

        return objects.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextViewBold CTVprice,CTVBservice,CTVname,tvViewpaid,CTVTime,tvStatus,CTVdate;
        public CustomTextView  CTVServicetype, CTVwork;
        public ImageView IVprofile;
        public LinearLayout llStatus;

        public MyViewHolder(View view) {
            super(view);

            CTVBservice = view.findViewById(R.id.CTVBservice);
            CTVdate = view.findViewById(R.id.CTVdate);
            CTVprice = view.findViewById(R.id.CTVprice);
            CTVServicetype = view.findViewById(R.id.CTVServicetype);
            CTVwork = view.findViewById(R.id.CTVwork);
            CTVname = view.findViewById(R.id.CTVname);
            IVprofile = view.findViewById(R.id.IVprofile);
            CTVTime = view.findViewById(R.id.CTVTime);
            llStatus = view.findViewById(R.id.llStatus);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvViewpaid = view.findViewById(R.id.tvViewpaid);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        objects.clear();
        if (charText.length() == 0) {
            objects.addAll(historyDTOList);
        } else {
            for (HistoryDTO historyDTO : historyDTOList) {
                if (historyDTO.getInvoice_id().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    objects.add(historyDTO);
                }
            }
        }
        notifyDataSetChanged();
    }

}