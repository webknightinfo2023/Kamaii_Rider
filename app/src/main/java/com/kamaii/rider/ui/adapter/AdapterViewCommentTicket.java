package com.kamaii.rider.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kamaii.rider.DTO.TicketCommentDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.ProjectUtils;

import java.util.ArrayList;



public class AdapterViewCommentTicket extends BaseAdapter {
    private Context mContext;
    private ArrayList<TicketCommentDTO> ticketCommentDTOSList;
    private UserDTO userDTO;

    public AdapterViewCommentTicket(Context mContext, ArrayList<TicketCommentDTO> ticketCommentDTOSList, UserDTO userDTO) {
        this.mContext = mContext;
        this.ticketCommentDTOSList = ticketCommentDTOSList;
        this.userDTO = userDTO;

    }

    @Override
    public int getCount() {
        return ticketCommentDTOSList.size();
    }

    @Override
    public Object getItem(int postion) {
        return ticketCommentDTOSList.get(postion);
    }

    @Override
    public long getItemId(int postion) {
        return postion;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        //ViewHolder holder = new ViewHolder();
        if (ticketCommentDTOSList.get(position).getRole() != userDTO.getRole()) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_view_comment, parent, false);

        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.adapter_view_comment_my, parent, false);

        }

        CustomTextView textViewMessage = (CustomTextView) view.findViewById(R.id.textViewMessage);
        CustomTextView textViewTime = (CustomTextView) view.findViewById(R.id.textViewTime);
        CustomTextView tvName = (CustomTextView) view.findViewById(R.id.tvName);

        textViewMessage.setText(ticketCommentDTOSList.get(position).getComment());
        tvName.setText(ticketCommentDTOSList.get(position).getUserName());
        try {

            textViewTime.setText(ProjectUtils.convertTimestampDateToTime(ProjectUtils.correctTimestamp(Long.parseLong(ticketCommentDTOSList.get(position).getCreated_at()))));

        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

}
