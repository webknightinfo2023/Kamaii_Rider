package com.kamaii.rider.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.preferences.SharedPrefrence;

public class ViewMapActivity extends AppCompatActivity {

    String customer_address="",customer_name="";

    private SharedPrefrence prefrence;
    String customer_longitude,customer_latitude;
    private UserDTO userDTO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_map);
        prefrence = SharedPrefrence.getInstance(ViewMapActivity.this);
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        customer_latitude=getIntent().getStringExtra("customer_latitude");
        customer_longitude=getIntent().getStringExtra("customer_longitude");
        customer_address=getIntent().getStringExtra("customer_address");
        customer_name=getIntent().getStringExtra("customer_name");


    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
