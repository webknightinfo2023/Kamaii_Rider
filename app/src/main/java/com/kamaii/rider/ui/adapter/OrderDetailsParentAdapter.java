package com.kamaii.rider.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.rider.DTO.ArtistBooking;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.OrderDetailsParentRecyclerLayoutBinding;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.neworder.NewOrderDetailsAdapter;
import com.kamaii.rider.ui.fragment.MaprouteFragment;

import java.util.List;

public class OrderDetailsParentAdapter extends RecyclerView.Adapter<OrderDetailsParentAdapter.ParentViewHolder> {

    Context context;
    List<ArtistBooking> arrayList;
    BaseActivity baseActivity;
    public OrderDetailsParentAdapter(Context context, List<ArtistBooking> arrayList, BaseActivity baseActivity) {
        this.context = context;
        this.arrayList = arrayList;
        this.baseActivity = baseActivity;
    }

    @NonNull
    @Override
    public ParentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderDetailsParentRecyclerLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_details_parent_recycler_layout,parent,false);
        return new ParentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentViewHolder holder, int position) {

        holder.binding.orderDetailsParentRv.setLayoutManager(new LinearLayoutManager(context));
        NewOrderDetailsAdapter adapter = new NewOrderDetailsAdapter(context,arrayList);
        holder.binding.orderDetailsParentRv.setAdapter(adapter);

        holder.binding.viewSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.loadHomeFragment(new MaprouteFragment(),"map");
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ParentViewHolder extends RecyclerView.ViewHolder {
        OrderDetailsParentRecyclerLayoutBinding binding;
        public ParentViewHolder(@NonNull OrderDetailsParentRecyclerLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
