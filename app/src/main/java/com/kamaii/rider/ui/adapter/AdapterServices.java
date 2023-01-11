package com.kamaii.rider.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kamaii.rider.DTO.ProductDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.fragment.Services;
import com.kamaii.rider.utils.CustomEditText;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ProjectUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterServices extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Services services;
    LayoutInflater mLayoutInflater;
    private ArrayList<ProductDTO> productDTOList;
    private Context context;
    private HashMap<String, String> parms = new HashMap<>();
    private HashMap<String, String> parmsedit = new HashMap<>();
    private String TAG = AdapterServices.class.getSimpleName();
    private DialogInterface dialog_book;
    Boolean isCheck= true;
    private Dialog dialogedit;
    CustomTextViewBold tveditcancel,tveditservice;
    RadioButton radioseditquantity,radioyeditdays;
    String quantitydays="";
    CheckBox cbshipping;
    CustomEditText etvalue;
    LinearLayout layroundtrip;
    String roundtrip="0";
    String totalvalue="";

    public AdapterServices(Services services, ArrayList<ProductDTO> productDTOList) {
        this.services = services;
        context = services.getActivity();
        this.productDTOList = productDTOList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_services, parent, false);
        return new MyViewHolder(view);
    }
    public boolean checkarss(String catid){

        boolean value=false;
        for(int i = 0; i< BaseActivity.addressModelArrayList.size(); i++) {
            if (BaseActivity.addressModelArrayList.get(i).getCat_id().equalsIgnoreCase(catid)) {
                value= true;
                break;

            } else {
                value= false;

            }

        }
        return value;
    }
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        final int pos = position;

        dialogedit = new Dialog(context,R.style.Theme_Dialog);
        dialogedit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogedit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   Window window = dialog.getWindow();
        dialogedit.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        dialogedit.setContentView(R.layout.dailog_editservice);
        dialogedit.setCancelable(true);




        myViewHolder.CTVproductname.setText(productDTOList.get(pos).getProduct_name());
        myViewHolder.tvdesc.setText(Html.fromHtml(productDTOList.get(pos).getDescription()));
        myViewHolder.CTVproductprice.setText(productDTOList.get(pos).getPrice() + " "+productDTOList.get(pos).getRate());
        myViewHolder.tvbdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheck) {
                    myViewHolder.tvdesc.setVisibility(View.VISIBLE);
                    isCheck = false;
                } else {
                    myViewHolder.tvdesc.setVisibility(View.GONE);
                    isCheck = true;
                }
            }
        });

        if(!productDTOList.get(pos).getVehicle_number().equalsIgnoreCase("")){
            myViewHolder.CTVvechieno.setVisibility(View.VISIBLE);
            myViewHolder.CTVvechieno.setText(productDTOList.get(pos).getVehicle_number());
        }else {
            myViewHolder.CTVvechieno.setVisibility(View.GONE);
        }

        if(!productDTOList.get(pos).getQuantitydays().equalsIgnoreCase("")){

            if(productDTOList.get(pos).getQuantitydays().equalsIgnoreCase("0")){
                myViewHolder.tvquantitydays.setText("Qty");
            }else {
                myViewHolder.tvquantitydays.setText("KM");
            }

        }

        if(!productDTOList.get(pos).getMaxmiumvalue().equalsIgnoreCase("")){
            myViewHolder.tvquantity.setText(productDTOList.get(pos).getMaxmiumvalue());
        }
        if(!productDTOList.get(pos).getService_charge().equalsIgnoreCase("")){
           // myViewHolder.layservicecharge.setVisibility(View.VISIBLE);
           // myViewHolder.tvs_charge.setText(productDTOList.get(pos).getService_charge()+ " "+productDTOList.get(pos).getRate());
        }



        Glide.with(context).
                load(productDTOList.get(pos).getProduct_image())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.dummyuser_image)
                .into(myViewHolder.IVproduct);



        if(productDTOList.get(pos).getIs_visible().equalsIgnoreCase("1")){
            myViewHolder.swOnOff.setChecked(true);
        }else {
            myViewHolder.swOnOff.setChecked(false);
        }

        if(productDTOList.get(pos).getStatus().equalsIgnoreCase("1")){
            myViewHolder.swOnOff.setEnabled(true);
        }else {
            myViewHolder.swOnOff.setEnabled(false);

        }

        //    myViewHolder.swOnOff.setChecked(productDTOList.get(pos).getStatus().equalsIgnoreCase("1") ?true :false);


        myViewHolder.swOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                parms.put(Consts.service_id, productDTOList.get(pos).getId());
                if(b){
                    parms.put(Consts.is_visible, "1");
                    deleteGallery();
                }else {
                    parms.put(Consts.is_visible, "0");
                    deleteGallery();
                }
            }
        });

        myViewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parms.put(Consts.PRODUCT_ID, productDTOList.get(pos).getId());
               // deleteDialog();
            }
        });

        myViewHolder.ivedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tveditcancel=dialogedit.findViewById(R.id.tveditcancel);
                tveditservice=dialogedit.findViewById(R.id.tveditservice);
                radioseditquantity=dialogedit.findViewById(R.id.radioseditquantity);
                radioyeditdays=dialogedit.findViewById(R.id.radioyeditdays);
                cbshipping=dialogedit.findViewById(R.id.cbshipping);
                etvalue=dialogedit.findViewById(R.id.etvalue);
                layroundtrip=dialogedit.findViewById(R.id.layroundtrip);


                if(!productDTOList.get(pos).getQuantitydays().equalsIgnoreCase("")){

                    if(productDTOList.get(pos).getQuantitydays().equalsIgnoreCase("0")){
                        radioseditquantity.setChecked(true);
                    }else {
                        radioyeditdays.setChecked(true);
                    }

                }

                if(!productDTOList.get(pos).getMaxmiumvalue().equalsIgnoreCase("")){
                    etvalue.setText(productDTOList.get(pos).getMaxmiumvalue());
                }
                dialogedit.show();

                if(productDTOList.get(pos).getSub_category_id().equalsIgnoreCase("453") || productDTOList.get(pos).getSub_category_id().equalsIgnoreCase("242")|| productDTOList.get(pos).getSub_category_id().equalsIgnoreCase("455")){
                    layroundtrip.setVisibility(View.GONE);
                }else {

                    if(checkarss(productDTOList.get(pos).getCategory_id())){
                        layroundtrip.setVisibility(View.VISIBLE);
                        if(productDTOList.get(pos).getRoundtrip().equalsIgnoreCase("0")){
                            roundtrip="0";
                            cbshipping.setChecked(false);
                        }else{
                            roundtrip="1";
                            cbshipping.setChecked(true);
                        }
                    }else {
                        layroundtrip.setVisibility(View.GONE);
                    }

                }






                cbshipping.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if(isChecked){
                            roundtrip="1";
                            // Toast.makeText(getActivity(),"1",Toast.LENGTH_LONG).show();
                        }else {
                            roundtrip="0";
                            //  Toast.makeText(getActivity(),"0",Toast.LENGTH_LONG).show();
                        }
                    }
                });



                tveditcancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogedit.dismiss();
                    }
                });

                tveditservice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       int maxvalue= Integer.parseInt(etvalue.getText().toString());
                        if (!validation(etvalue, "Please Enter Value")) {
                            return;
                        }
                         else if (maxvalue<1) {
                            Toast.makeText(context,"Please Select Atleast 1 Value", Toast.LENGTH_LONG).show();
                            return;
                        }else {
                           // Toast.makeText(context,"Ok",Toast.LENGTH_LONG).show();
                            if(radioseditquantity.isChecked()){
                                quantitydays="0";
                            }else {
                                quantitydays="1";
                            }


                            if(roundtrip.equalsIgnoreCase("1")){

                                if(etvalue.getText().toString().equalsIgnoreCase(productDTOList.get(pos).getMaxmiumvalue())){
                                    parmsedit.put(Consts.PRODUCT_ID, productDTOList.get(pos).getId());
                                    parmsedit.put(Consts.QUANTITYDAYS, quantitydays);
                                    parmsedit.put(Consts.MAXIMUMVALUE, etvalue.getText().toString());
                                    parmsedit.put(Consts.ROUNDTRIP, roundtrip);
                                }else {
                                    int maxv= Integer.parseInt(etvalue.getText().toString());
                                    totalvalue= String.valueOf(maxv*2);
                                    parmsedit.put(Consts.PRODUCT_ID, productDTOList.get(pos).getId());
                                    parmsedit.put(Consts.QUANTITYDAYS, quantitydays);
                                    parmsedit.put(Consts.MAXIMUMVALUE, totalvalue);
                                    parmsedit.put(Consts.ROUNDTRIP, roundtrip);
                                }

                            }else {
                                parmsedit.put(Consts.PRODUCT_ID, productDTOList.get(pos).getId());
                                parmsedit.put(Consts.QUANTITYDAYS, quantitydays);
                                parmsedit.put(Consts.MAXIMUMVALUE, etvalue.getText().toString());
                                parmsedit.put(Consts.ROUNDTRIP, roundtrip);
                            }




                           // editproduct();

                            new HttpsRequest(Consts.EDIT_PRODUCT_API, parmsedit, context).stringPost(TAG, new Helper() {
                                @Override
                                public void backResponse(boolean flag, String msg, JSONObject response) {
                                    if (flag) {
                                        if(quantitydays.equalsIgnoreCase("0")){
                                            myViewHolder.tvquantitydays.setText("Qty");
                                            productDTOList.get(pos).setQuantitydays("0");
                                        }else {
                                            productDTOList.get(pos).setQuantitydays("1");
                                            myViewHolder.tvquantitydays.setText("KM");
                                        }


                                        if(roundtrip.equalsIgnoreCase("1")){

                                            productDTOList.get(pos).setRoundtrip(roundtrip);

                                            if(!totalvalue.equalsIgnoreCase("")){
                                                myViewHolder.tvquantity.setText(totalvalue);
                                                productDTOList.get(pos).setMaxmiumvalue(totalvalue);
                                            }else {
                                                myViewHolder.tvquantity.setText(productDTOList.get(pos).getMaxmiumvalue());
                                            }

                                        }else {
                                            productDTOList.get(pos).setRoundtrip(roundtrip);

                                            myViewHolder.tvquantity.setText(productDTOList.get(pos).getMaxmiumvalue());
                                        }

                                        ProjectUtils.showToast(context, msg);
                                        dialogedit.dismiss();
                                        //  services.getrefresh();
                                    } else {
                                        ProjectUtils.showLong(context, msg);
                                    }
                                }
                            });
                        }


                       // Toast.makeText(context,quantitydays,Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    public boolean validation(EditText editText, String msg) {
        if (!ProjectUtils.isEditTextFilled(editText)) {
            ProjectUtils.showLong(context, msg);
            return false;
        } else {
            return true;
        }
    }
    public void editproduct() {

        new HttpsRequest(Consts.EDIT_PRODUCT_API, parmsedit, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {

               //     ProjectUtils.showToast(context, msg);
                    dialogedit.dismiss();
                  //  services.getrefresh();
                } else {
                 //   ProjectUtils.showLong(context, msg);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return productDTOList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivDelete,ivedit;
        public CircleImageView IVproduct;
        public CustomTextViewBold CTVproductname, CTVproductprice,tvs_charge,tvquantity,tvquantitydays,tvbdetails,CTVvechieno;
        public LinearLayout layservicecharge;
        public Switch swOnOff;
        CustomTextView tvdesc;
        public MyViewHolder(View view) {
            super(view);


            IVproduct = itemView.findViewById(R.id.IVproduct);
            CTVproductname = itemView.findViewById(R.id.CTVproductname);
            CTVproductprice = itemView.findViewById(R.id.CTVproductprice);
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);
            tvdesc =  itemView.findViewById(R.id.tvdesc);
            tvs_charge =  itemView.findViewById(R.id.tvs_charge);
            layservicecharge =  itemView.findViewById(R.id.layservicecharge);
            ivedit =  itemView.findViewById(R.id.ivedit);
            tvquantity =  view.findViewById(R.id.tvquantity);
            tvquantitydays =  view.findViewById(R.id.tvquantitydays);
            swOnOff = view.findViewById(R.id.swOnOff);
            tvbdetails = view.findViewById(R.id.more);
            CTVvechieno = view.findViewById(R.id.CTVvechieno);
        }
    }




    @Override
    public int getItemViewType(int position) {
        return  position;

    }
    public void deleteDialog() {
        try {
            new AlertDialog.Builder(context)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle(context.getResources().getString(R.string.delete_service))
                    .setMessage(context.getResources().getString(R.string.delete_service_msg))
                    .setCancelable(false)
                    .setPositiveButton(context.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog_book = dialog;
                            deleteGallery();

                        }
                    })
                    .setNegativeButton(context.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteGallery() {
        new HttpsRequest(Consts.serviceVisiblity, parms, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                   // notifyDataSetChanged();
                    services.getParentData();
                } else {
                    ProjectUtils.showLong(context, msg);
                }
            }
        });
    }

}