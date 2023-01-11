package com.kamaii.rider.ui.adapter;



import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.rider.DTO.TicketDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.ui.activity.CommentOneByOne;
import com.kamaii.rider.ui.fragment.Tickets;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ProjectUtils;

import java.util.ArrayList;


public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    private Context mContext;
    private Tickets tickets;
    private ArrayList<TicketDTO> ticketDTOSList;
    private UserDTO userDTO;


    public TicketAdapter(Tickets tickets, ArrayList<TicketDTO> ticketDTOSList, UserDTO userDTO) {
        this.tickets = tickets;
        this.mContext = tickets.getActivity();
        this.ticketDTOSList = ticketDTOSList;
        this.userDTO = userDTO;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_ticket, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.tvTicket.setText(ticketDTOSList.get(position).getReason());
        holder.tvinvoice.setText(ticketDTOSList.get(position).getInvoice_id());

        try {

            holder.tvDate.setText(ProjectUtils.convertTimestampDateToTime(ProjectUtils.correctTimestamp(Long.parseLong(ticketDTOSList.get(position).getCraeted_at()))));

        }catch (Exception e){
            e.printStackTrace();
        }

        if (ticketDTOSList.get(position).getStatus().equalsIgnoreCase("0")) {
            holder.tvStatus.setText(mContext.getResources().getString(R.string.pending));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_red));
        } else if (ticketDTOSList.get(position).getStatus().equalsIgnoreCase("1")) {
            holder.tvStatus.setText(mContext.getResources().getString(R.string.solve));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_yellow));
        } else if (ticketDTOSList.get(position).getStatus().equalsIgnoreCase("2")) {
            holder.tvStatus.setText(mContext.getResources().getString(R.string.close));
            holder.llStatus.setBackground(mContext.getResources().getDrawable(R.drawable.rectangle_green));
        }

        holder.rlClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(mContext, CommentOneByOne.class);
                in.putExtra(Consts.TICKET_ID, ticketDTOSList.get(position).getId());
                mContext.startActivity(in);
            }
        });


    }

    @Override
    public int getItemCount() {

        return ticketDTOSList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public CustomTextViewBold tvTicket;
        public CustomTextView tvDate, tvStatus,tvinvoice;
        public LinearLayout llStatus;
        public RelativeLayout rlClick;

        public MyViewHolder(View view) {
            super(view);
            llStatus = view.findViewById(R.id.llStatus);
            tvStatus = view.findViewById(R.id.tvStatus);
            tvTicket = view.findViewById(R.id.tvTicket);
            tvDate = view.findViewById(R.id.tvDate);
            rlClick = view.findViewById(R.id.rlClick);
            tvinvoice = view.findViewById(R.id.tvinvoice);


        }
    }


}