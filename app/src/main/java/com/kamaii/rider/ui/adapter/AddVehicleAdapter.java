package com.kamaii.rider.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kamaii.rider.DTO.CategoryDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.ui.models.VehicleModel;
import com.kamaii.rider.utils.CustomTextView;
import com.kamaii.rider.utils.CustomTextViewBold;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class AddVehicleAdapter extends BaseAdapter {

    Context context;
    List<VehicleModel> arrayList;
    int pos = -1;
    private HashMap<String, String> parmsCategory2 = new HashMap<>();
    String user_id = "";

    public AddVehicleAdapter(Context context, List<VehicleModel> arrayList, String user_id) {
        this.context = context;
        this.arrayList = arrayList;
        this.user_id = user_id;
        Log.e("VEHICLELIST", "" + arrayList.size());
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(R.layout.add_vehicle_recycle_layout, null);

        ImageView v_img = view.findViewById(R.id.vehicle_recycle_image);
        CustomTextViewBold v_name = view.findViewById(R.id.vehicle_recycle_name);
        CustomTextViewBold v_num = view.findViewById(R.id.vehicle_recycle_number);
        CustomTextViewBold rider_charges = view.findViewById(R.id.rider_charges);
        CustomTextView v_status = view.findViewById(R.id.vahicle_status);
        Switch aSwitch = view.findViewById(R.id.vahicle_status_switch);

        Log.e("rider_charges",""+arrayList.get(position).getRider_charges());
        rider_charges.setText(arrayList.get(position).getRider_charges());
        if (arrayList.get(position).getVstatus().contains("1")) {

            pos = position;
            arrayList.get(position).setVstatus("0");
            notifyDataSetChanged();
        }
        if (arrayList.get(position).getApprove_status().equalsIgnoreCase("0")) {
            v_status.setText("Requested");
            aSwitch.setEnabled(false);
        } else {
            if (arrayList.get(position).getVstatus().equals("1")) {
                v_status.setText("Approved");
                aSwitch.setChecked(true);
                aSwitch.setEnabled(true);
            } else {
                v_status.setText("Approved");
                aSwitch.setEnabled(true);
                aSwitch.setChecked(false);
            }
        }


        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Toast.makeText(context, "ghchcghcnb"+arrayList.get(position).getVehicle_id(), Toast.LENGTH_SHORT).show();
                if (isChecked) {

                    pos = position;
                    sendVehicleDetails(arrayList.get(position).getVehicle_id(),"1");
                    notifyDataSetChanged();
                }
                else {
                   // Toast.makeText(context, "ghchcghcnb", Toast.LENGTH_SHORT).show();
                    sendVehicleDetails(arrayList.get(position).getVehicle_id(),"0");
                }
            }
        });

        if (pos == position) {

            aSwitch.setChecked(true);
        } else {
            aSwitch.setChecked(false);
        }
        //Log.e(TAG, "getView 999: " + arrayList.get(position).getVehicle_no());
        Glide.with(context).load(arrayList.get(position).getImage()).into(v_img);
        v_name.setText(arrayList.get(position).getVehicle_name());
        v_num.setText("Vehicle No :" + arrayList.get(position).getVehicle_no());
        //v_name.setText(arrayList.get(position).getVehiclename());
        return view;
    }

    private void sendVehicleDetails(String vehicle_id, String v_status) {

        parmsCategory2.put("user_id", user_id);
        parmsCategory2.put("vehicle_id", vehicle_id);
        parmsCategory2.put("vehicle_status", v_status);

        new HttpsRequest(Consts.SEND_VEHICLE_STATUS_API, parmsCategory2, context).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {

                        //Log.e("DIALOG_CAT", "" + response.toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
