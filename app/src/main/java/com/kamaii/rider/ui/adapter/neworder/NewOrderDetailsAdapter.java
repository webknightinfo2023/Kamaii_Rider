package com.kamaii.rider.ui.adapter.neworder;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.rider.DTO.ArtistBooking;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.OrderDetailsNewRvLayoutBinding;

import java.util.List;

public class NewOrderDetailsAdapter extends RecyclerView.Adapter<NewOrderDetailsAdapter.OrderViewHolder> {


    Context context;
    List<ArtistBooking> arrayList;


    public NewOrderDetailsAdapter(Context context, List<ArtistBooking> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderDetailsNewRvLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.order_details_new_rv_layout,parent,false);
        return new OrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

//        holder.binding.

        if (arrayList.get(position).getBooking_flag().equalsIgnoreCase("4")){
            holder.binding.clock123First.setImageResource(R.drawable.ic_payment_order_details_green);
            holder.binding.orderStatusFirst.setText("Order Completed");
            holder.binding.tempView1First.setBackground(context.getResources().getDrawable(R.color.green));
            holder.binding.timerRelativeFirst.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_green_bg));
            holder.binding.orderMainRelative.setBackground(context.getResources().getDrawable(R.drawable.green_border_bg));
        }else if(arrayList.get(position).getBooking_flag().equalsIgnoreCase("2")){
            holder.binding.clock123First.setImageResource(R.drawable.cancel);
            holder.binding.orderStatusFirst.setText("Order Cancel");
            holder.binding.tempView1First.setBackground(context.getResources().getDrawable(R.color.red));
            holder.binding.timerRelativeFirst.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_red_bg));
            holder.binding.orderMainRelative.setBackground(context.getResources().getDrawable(R.drawable.red_border_bg));
        }

        holder.binding.orderId.setText(arrayList.get(position).getId());
        holder.binding.orderDetailsTime.setText(arrayList.get(position).getOrder_date());
        holder.binding.deliveryDateTime.setText(arrayList.get(position).getDelivery_date());
        holder.binding.totalPaymentFirst.setText(Html.fromHtml("&#8377;")+arrayList.get(position).getRider_charges());

        holder.binding.orderDetailsCartRvFirst.setLayoutManager(new LinearLayoutManager(context));

        AdapterOrderDetailsCart cart = new AdapterOrderDetailsCart(context,arrayList.get(position).getProduct());
        holder.binding.orderDetailsCartRvFirst.setAdapter(cart);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class OrderViewHolder extends RecyclerView.ViewHolder {
        OrderDetailsNewRvLayoutBinding binding;
        public OrderViewHolder(@NonNull OrderDetailsNewRvLayoutBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
