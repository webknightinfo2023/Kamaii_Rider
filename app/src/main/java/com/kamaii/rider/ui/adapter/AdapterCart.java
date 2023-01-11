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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.ui.fragment.NewBookings;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;

import java.util.ArrayList;

import static com.kamaii.rider.utils.ProjectUtils.TAG;


public class AdapterCart extends RecyclerView.Adapter<AdapterCart.MyViewHolder> {

    private LayoutInflater mLayoutInflater;
    private NewBookings newBookings;
    private Context context;
    private ArrayList<ProductDTO> productDTOArrayList,productDTOArrayListtemp;
    String locationstatus="";
    Boolean isCheck= true;
    public AdapterCart(NewBookings newBookings, ArrayList<ProductDTO> productDTOArrayList, String locationstatus) {
        this.newBookings = newBookings;
        this.productDTOArrayList = productDTOArrayList;
        this.locationstatus = locationstatus;
        context = newBookings.getActivity();
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
            holder.shortdesc.setText(Html.fromHtml(productDTOArrayListtemp.get(position).getShort_description()));

            Log.e(TAG, "onBindViewHolder: dhaval " +  productDTOArrayListtemp.get(position).getProduct_image() );
            Glide.with(context).load(productDTOArrayListtemp.get(position).getProduct_image()).placeholder(R.drawable.dafault_product).into(holder.ProductImg);






            if(!productDTOArrayListtemp.get(position).getMaxmiumvalue().equalsIgnoreCase("")){
                holder.tvquantity.setText(productDTOArrayListtemp.get(position).getMaxmiumvalue());
            }
                if(!productDTOArrayListtemp.get(position).getChange_price().equalsIgnoreCase("")){
                    holder.tvPrice.setText(productDTOArrayListtemp.get(position).getChange_price() +" "+ "Rs");
                }

                if(!productDTOArrayListtemp.get(position).getProduct_name().equalsIgnoreCase("")){
                    holder.tvProductName.setText(productDTOArrayListtemp.get(position).getProduct_name());
                }

            if(locationstatus.equalsIgnoreCase("0")){

            }else if(locationstatus.equalsIgnoreCase("1")) {

                if (productDTOArrayListtemp.get(position).getCategory_id().equalsIgnoreCase("85")) {
                    if (!productDTOArrayListtemp.get(position).getService_charge().equalsIgnoreCase("")) {
                        holder.tvService_charge.setVisibility(View.VISIBLE);
                        holder.tvService_charge.setText("Driver Allowance:" + " " + productDTOArrayListtemp.get(position).getService_charge());
                        // holder.tvService_charge.setText(productDTOList.get(pos).getService_charge()  +" "+ productDTOList.get(pos).getRate());
                    }
                } else {
                    if (!productDTOArrayListtemp.get(position).getService_charge().equalsIgnoreCase("")) {
                        holder.tvService_charge.setVisibility(View.VISIBLE);
                        holder.tvPrices.setText(productDTOArrayListtemp.get(position).getQuantitydays());

                        holder.tvService_charge.setText("Service Charge:" + " " + productDTOArrayListtemp.get(position).getService_charge());
                        //  holder.tvService_charge.setText(productDTOList.get(pos).getService_charge()  +" "+ productDTOList.get(pos).getRate());
                    }
                }
            }

//            if (!productDTOArrayListtemp.get(position).getDescription().equalsIgnoreCase(" ")){

               // holder.tvdesc.setText(Html.fromHtml(productDTOArrayListtemp.get(position).getDescription()));
  //          }

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
        public CustomTextViewBold tvProductName,tvPrices, tvPrice,tvService_charge,tvquantity,tvquantitydays,tvbdetails,tvvehiclenumber;
        CustomTextView shortdesc,tvdesc;
        ImageView ProductImg;
        TextView tmp_line_view;

        public MyViewHolder(View view) {
            super(view);


            tvProductName = (CustomTextViewBold) view.findViewById(R.id.tvProductName);
            ProductImg = view.findViewById(R.id.ProductImg);
            tvPrice = (CustomTextViewBold) view.findViewById(R.id.tvPrice);
            tvPrices =  view.findViewById(R.id.tvPrices);
            tmp_line_view =  view.findViewById(R.id.tmp_line_view);

            rel_view =  view.findViewById(R.id.rel_view);
            tvService_charge =  view.findViewById(R.id.tvService_charge);
            shortdesc =  view.findViewById(R.id.shortdesc);
            lay_service_charge =  view.findViewById(R.id.lay_service_charge);
            tvquantity =  view.findViewById(R.id.tvquantity);
            tvquantitydays =  view.findViewById(R.id.tvquantitydays);
            tvbdetails =  view.findViewById(R.id.tvbdetails);

            tvdesc =  view.findViewById(R.id.tvdesc);
            tvvehiclenumber =  view.findViewById(R.id.tvvehiclenumber);



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
