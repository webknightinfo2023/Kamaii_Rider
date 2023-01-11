package com.kamaii.rider.ui.adapter.neworder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SubAdapterCabBooking extends RecyclerView.Adapter<SubAdapterCabBooking.SubViewHolder> {

    Context context;

    @NonNull
    @Override
    public SubViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull SubViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class SubViewHolder extends RecyclerView.ViewHolder {
        public SubViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
