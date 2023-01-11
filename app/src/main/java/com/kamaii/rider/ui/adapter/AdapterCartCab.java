package com.kamaii.rider.ui.adapter;


import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.ui.fragment.CabBookingsFragment;
import com.kamaii.rider.ui.models.neworder.Product;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;

import java.util.ArrayList;
import java.util.List;


public class AdapterCartCab extends RecyclerView.Adapter<AdapterCartCab.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private CabBookingsFragment newBookings;
    private Context context;
    private List<Product> productDTOArrayList;
    String locationstatus = "";
    Boolean isCheck = true;

    public AdapterCartCab(CabBookingsFragment newBookings, List<Product> productDTOArrayList, String locationstatus) {
        this.newBookings = newBookings;
        this.productDTOArrayList = productDTOArrayList;
        this.locationstatus = locationstatus;
        context = newBookings.getActivity();
        // productDTOArrayList=isused();
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        //  if (productDTOArrayList.get(position).getVehicle_number().equalsIgnoreCase("")) {
        holder.tvvehiclenumber.setVisibility(View.GONE);
        // if (productDTOArrayList.get(position).get().equalsIgnoreCase(" ")) {
        holder.tvPrices.setText(productDTOArrayList.get(position).getQty());
        Log.e("QTTTTTTTY", " 1 " + productDTOArrayList.get(position).getQty());
        //   holder.tvPrices_title.setText("QTY:"+productDTOArrayList.get(position).getQty());
        //   } else {
        Log.e("QTTTTTTTY", " 2 :--" + productDTOArrayList.get(position).getQty());

        //holder.tvPrices.setText(productDTOArrayList.get(position).getQty()+" ("+productDTOArrayList.get(position).getVariation()+") ");
        //holder.tvPrices.setText(productDTOArrayList.get(position).get());
        //    holder.tvPrices_title.setText("QTY:"+productDTOArrayList.get(position).getQty());
        // }}

        if (position == productDTOArrayList.size() - 1) {
            holder.tmp_line_view.setVisibility(View.GONE);
        }
        if (!productDTOArrayList.get(position).getQty().equalsIgnoreCase("")) {

            if (productDTOArrayList.get(position).getQty().equalsIgnoreCase("0")) {
                holder.tvquantitydays.setText("Qty");
            } else {
                holder.tvquantitydays.setText("KM");
            }

        }

     /*   Glide.with(context).
                load(Consts.IMAGE_URL + productDTOArrayList.get(position).getProduct_image())
                .placeholder(R.drawable.dafault_product)
                .into(holder.ProductImg);
*/

        if (AdapterCabBookings.pay_type.equalsIgnoreCase("0")) {
            // holder.binding.txtptype.setVisibility(View.GONE);
            holder.payment_mode_txt.setText("Online Payment");

        } else if (AdapterCabBookings.pay_type.equalsIgnoreCase("1")) {
            //holder.binding.txtptype.setVisibility(View.GONE);
            holder.payment_mode_txt.setText("Cash");

        } else if (AdapterCabBookings.pay_type.equalsIgnoreCase("2")) {
            //holder.binding.txtptype.setVisibility(View.GONE);
            holder.payment_mode_txt.setText("Wallet Payment");

        } else {
            holder.payment_mode_txt.setVisibility(View.GONE);
        }

        if (productDTOArrayList.get(position).getCookingInstruction().equalsIgnoreCase("null")) {
            holder.cooking_instruction_card.setVisibility(View.GONE);
        } else {
            //holder.binding.newCartMain.setBackground(context.getResources().getDrawable(R.drawable.custom_card_relative_background));
            holder.cooking_inst.setText(productDTOArrayList.get(position).getCookingInstruction());
            holder.cooking_instruction_card.setVisibility(View.VISIBLE);
        }

       /* if (!productDTOArrayList.get(position).getMaxmiumvalue().equalsIgnoreCase("")) {
            holder.tvquantity.setText(productDTOArrayList.get(position).getMaxmiumvalue());
        }
        if (!productDTOArrayList.get(position).getChange_price().equalsIgnoreCase("")) {
            holder.tvPrice.setText(productDTOArrayList.get(position).getChange_price());
        }*/

        if (!productDTOArrayList.get(position).getProductName().equalsIgnoreCase("")) {
            holder.tvProductName.setText(position + 1 + ". " + productDTOArrayList.get(position).getProductName());
        }


        holder.tvbdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheck) {
                    holder.tvdesc.setVisibility(View.VISIBLE);
                    isCheck = false;
                } else {
                    holder.tvdesc.setVisibility(View.GONE);
                    isCheck = true;
                }
            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return productDTOArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public LinearLayout rel_view, lay_service_charge;
        public CustomTextViewBold tvPrices_title, tvPrices, payment_mode_txt, tvProductName, tvPrice, tvService_charge, tvquantity, tvquantitydays, tvroundtrip, tvbdetails, tvvehiclenumber;
        CustomTextView tvdesc, cooking_inst;
        ImageView ProductImg;
        TextView tmp_line_view;
        CardView cooking_instruction_card;

        public MyViewHolder(View view) {
            super(view);


            tvProductName = (CustomTextViewBold) view.findViewById(R.id.tvProductName);
            tvPrices_title = (CustomTextViewBold) view.findViewById(R.id.tvPrices_title);
            tvPrice = (CustomTextViewBold) view.findViewById(R.id.tvPrice);
            tvPrices = view.findViewById(R.id.tvPrices);
            rel_view = view.findViewById(R.id.rel_view);
            tmp_line_view = view.findViewById(R.id.tmp_line_view);
            tvService_charge = view.findViewById(R.id.tvService_charge);
            lay_service_charge = view.findViewById(R.id.lay_service_charge);
            payment_mode_txt = view.findViewById(R.id.payment_mode_txt);
            cooking_instruction_card = view.findViewById(R.id.cooking_instruction_card);
            cooking_inst = view.findViewById(R.id.cooking_inst);
            tvquantity = view.findViewById(R.id.tvquantity);
            tvquantitydays = view.findViewById(R.id.tvquantitydays);
            tvroundtrip = view.findViewById(R.id.tvroundtrip);
            tvdesc = view.findViewById(R.id.tvdesc);
            tvbdetails = view.findViewById(R.id.tvbdetails);
            tvvehiclenumber = view.findViewById(R.id.tvvehiclenumber);
            ProductImg = view.findViewById(R.id.ProductImg);


        }
    }

    /*private ArrayList<Product> isused(){
      ArrayList<ProductDTO> arrayList=new ArrayList<>();
        for(int i=0;i<productDTOArrayList.size();i++){
            if(productDTOArrayList.get(i).getIs_used().equalsIgnoreCase("1")){
                arrayList.add(Product.get(i));
            }
        }
        return arrayList;
    }*/


}
