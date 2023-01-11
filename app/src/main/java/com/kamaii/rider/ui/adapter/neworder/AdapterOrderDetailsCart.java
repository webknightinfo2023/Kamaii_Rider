package com.kamaii.rider.ui.adapter.neworder;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.CartNewBookingLayoutBinding;
import com.kamaii.rider.ui.models.neworder.Product;

import java.util.List;

public class AdapterOrderDetailsCart extends RecyclerView.Adapter<AdapterOrderDetailsCart.DetailViewHolder> {

    Context context;
    List<ProductDTO> arrayList;

    public AdapterOrderDetailsCart(Context context, List<ProductDTO> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public DetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartNewBookingLayoutBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.cart_new_booking_layout,parent,false);
        return new DetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailViewHolder holder, int position) {

        holder.binding.newBookingCartPrdName.setText(arrayList.get(position).getProduct_name()+" x ");
        holder.binding.newBookingCartPrdQty.setText(arrayList.get(position).getQty());
     //   holder.binding.newBookingCartPrice.setText(Html.fromHtml("&#8377;"+arrayList.get(position).getPrice()));

        Log.e("cooking_inst", "onBindViewHolder: position  "+arrayList.get(position).getCooking_instruction() );
        if (arrayList.get(position).getCooking_instruction().equalsIgnoreCase("null")){
            holder.binding.newCartMain.setBackground(null);
            holder.binding.instructionRel.setVisibility(View.GONE);
        }else {
            //holder.binding.newCartMain.setBackground(context.getResources().getDrawable(R.drawable.custom_card_relative_background));
            holder.binding.newCartMain.setBackgroundTintList(context.getResources().getColorStateList(R.color.light_blue));
            holder.binding.cookingInstructionTxt.setText(arrayList.get(position).getCooking_instruction());
            holder.binding.instructionRel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class DetailViewHolder extends RecyclerView.ViewHolder {
        CartNewBookingLayoutBinding binding;
        public DetailViewHolder(@NonNull CartNewBookingLayoutBinding binding) {
            super(binding.getRoot());
            this.binding=binding;
        }
    }
}
