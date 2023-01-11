package com.kamaii.rider.ui.adapter;


import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;

import java.util.ArrayList;


public class AdapterInvoiceService extends RecyclerView.Adapter<AdapterInvoiceService.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private Context context;
    private ArrayList<ProductDTO> productDTOArrayList,productDTOArrayListtemp;
    String locationstatus="";
    Boolean isCheck= true;

    public AdapterInvoiceService(Context context, ArrayList<ProductDTO> productDTOArrayList, String locationstatus) {
        this.context = context;
        this.productDTOArrayList = productDTOArrayList;
        this.locationstatus = locationstatus;
        productDTOArrayListtemp=isused();
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder,  int position) {

        if(productDTOArrayListtemp.get(position).getVehicle_number().equalsIgnoreCase("")){
            holder.tvvehiclenumber.setVisibility(View.GONE);
        }else {
            holder.tvvehiclenumber.setVisibility(View.VISIBLE);
            holder.tvvehiclenumber.setText(productDTOArrayListtemp.get(position).getVehicle_number());
        }
        if(!productDTOArrayListtemp.get(position).getQuantitydays().equalsIgnoreCase("")){

            if(productDTOArrayListtemp.get(position).getQuantitydays().equalsIgnoreCase("0")){
                holder.tvquantitydays.setText("Qty");
            }else {
                holder.tvquantitydays.setText("KM");
            }

        }
        if (position == productDTOArrayListtemp.size()-1){
            holder.tmp_line_view.setVisibility(View.GONE);
        }
        if(!productDTOArrayListtemp.get(position).getMaxmiumvalue().equalsIgnoreCase("")){
            holder.tvquantity.setText(productDTOArrayListtemp.get(position).getMaxmiumvalue());
        }
            if(!productDTOArrayListtemp.get(position).getChange_price().equalsIgnoreCase("")){
                holder.tvPrice.setText(productDTOArrayListtemp.get(position).getChange_price() +" "+ productDTOArrayListtemp.get(position).getRate());
            }

            if(!productDTOArrayListtemp.get(position).getProduct_name().equalsIgnoreCase("")){
                holder.tvProductName.setText(productDTOArrayListtemp.get(position).getProduct_name());
            }

        if (productDTOArrayListtemp.get(position).getCategory_id().equalsIgnoreCase("85")) {
            if (!productDTOArrayListtemp.get(position).getService_charge().equalsIgnoreCase("")) {
                holder.tvService_charge.setVisibility(View.VISIBLE);
                holder.tvService_charge.setText("Driver Allowance:" + " " + productDTOArrayListtemp.get(position).getService_charge()+" "+ productDTOArrayListtemp.get(position).getRate());
                // holder.tvService_charge.setText(productDTOList.get(pos).getService_charge()  +" "+ productDTOList.get(pos).getRate());
            }
        } else {
            if (!productDTOArrayListtemp.get(position).getService_charge().equalsIgnoreCase("")) {
                holder.tvService_charge.setVisibility(View.VISIBLE);
                holder.tvPrices.setText(productDTOArrayListtemp.get(position).getQuantitydays());
                holder.tvService_charge.setText("Service Charge:" + " " + productDTOArrayListtemp.get(position).getService_charge()+" "+ productDTOArrayListtemp.get(position).getRate());
                //  holder.tvService_charge.setText(productDTOList.get(pos).getService_charge()  +" "+ productDTOList.get(pos).getRate());
            }
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
        return productDTOArrayListtemp.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        public LinearLayout rel_view,lay_service_charge;
        public CustomTextViewBold tvProductName, tvPrice,tvService_charge,tvquantity,tvquantitydays,tvbdetails,tvvehiclenumber;
        CustomTextView tvdesc,tvPrices;
        TextView tmp_line_view;

        public MyViewHolder(View view) {
            super(view);


            tvProductName = (CustomTextViewBold) view.findViewById(R.id.tvProductName);
            tvPrice = (CustomTextViewBold) view.findViewById(R.id.tvPrice);
            tvPrices = (CustomTextView) view.findViewById(R.id.tvPrices);
            tmp_line_view =  view.findViewById(R.id.tmp_line_view);
            rel_view =  view.findViewById(R.id.rel_view);
            tvService_charge =  view.findViewById(R.id.tvService_charge);
            lay_service_charge =  view.findViewById(R.id.lay_service_charge);
            tvquantity =  view.findViewById(R.id.tvquantity);
            tvquantitydays =  view.findViewById(R.id.tvquantitydays);
            tvbdetails =  view.findViewById(R.id.tvbdetails);
            tvvehiclenumber =  view.findViewById(R.id.tvvehiclenumber);
            tvdesc =  view.findViewById(R.id.tvdesc);



        }
    }

    private ArrayList<ProductDTO> isused(){
      ArrayList<ProductDTO> arrayList=new ArrayList<>();
        for(int i=0;i<productDTOArrayList.size();i++){
            if(productDTOArrayList.get(i).getIs_used().equalsIgnoreCase("1")){
                arrayList.add(productDTOArrayList.get(i));
            }
        }
        return arrayList;
    }


}
