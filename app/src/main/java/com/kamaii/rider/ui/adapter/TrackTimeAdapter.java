package com.kamaii.rider.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.rider.R;
import com.kamaii.rider.databinding.TrackTimeRecyclerLayoutBinding;

import java.util.List;

public class TrackTimeAdapter extends RecyclerView.Adapter<TrackTimeAdapter.TimeViewHolder> {

    Context context;
    List<String> arrayList;

    public TrackTimeAdapter(Context context, List<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public TimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TrackTimeRecyclerLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.track_time_recycler_layout,parent,false);
        return new TimeViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeViewHolder holder, int position) {

        holder.binding.time.setText(arrayList.get(position));


        if (position == arrayList.size()-1){
            holder.binding.line.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class TimeViewHolder extends RecyclerView.ViewHolder {
        TrackTimeRecyclerLayoutBinding binding;
        public TimeViewHolder(@NonNull TrackTimeRecyclerLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
