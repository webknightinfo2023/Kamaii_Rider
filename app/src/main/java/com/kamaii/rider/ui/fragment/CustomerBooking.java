package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.ArtistBooking;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.AdapterCustomerBooking;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerBooking extends Fragment implements View.OnClickListener {

    private String TAG = CustomerBooking.class.getSimpleName();
    private View view;
    private CustomTextViewBold tvNo;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private HashMap<String, String> paramsGetBooking = new HashMap<>();
    private HashMap<String, String> paramsBookingOp;
    private HashMap<String, String> paramsDecline;
    private BaseActivity baseActivity;
    private ArrayList<ArtistBooking> artistBookingsList;
    RecyclerView rvBookingcustomer;
    AdapterCustomerBooking adapterCustomerBooking;
    private LinearLayoutManager mLayoutManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_customer_booking, container, false);

        baseActivity.headerNameTV.setText(getResources().getString(R.string.customer_booking));
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);
        paramsGetBooking.put(Consts.ARTIST_ID, userDTO.getUser_id());
        tvNo=view.findViewById(R.id.tvNo);
        rvBookingcustomer=view.findViewById(R.id.rvBookingcustomer);
        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvBookingcustomer.setLayoutManager(mLayoutManager);



        if (NetworkManager.isConnectToInternet(getActivity())) {
            paramsGetBooking.put(Consts.BOOKING_FLAG, "3");
            getBooking();
        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAccept:
                if (NetworkManager.isConnectToInternet(getActivity())) {

                  //  booking("1");
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
                break;
            case R.id.llDecline:
                ProjectUtils.showDialog(getActivity(), getResources().getString(R.string.dec_cpas), getResources().getString(R.string.decline_msg), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, false);

                break;
            case R.id.llStart:
                if (NetworkManager.isConnectToInternet(getActivity())) {

                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
                break;
            case R.id.llCancel:
                ProjectUtils.showDialog(getActivity(), getResources().getString(R.string.dec_cpas), getResources().getString(R.string.decline_msg), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, false);
                break;
            case R.id.llFinishJob:
                if (NetworkManager.isConnectToInternet(getActivity())) {
                } else {
                    ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                }
                break;
        }

    }


    public void getBooking() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_ALL_BOOKING_ARTIST_API, paramsGetBooking, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    rvBookingcustomer.setVisibility(View.VISIBLE);
                    try {
                        artistBookingsList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ArtistBooking>>() {
                        }.getType();
                        artistBookingsList = (ArrayList<ArtistBooking>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {

                    tvNo.setVisibility(View.VISIBLE);
                    rvBookingcustomer.setVisibility(View.GONE);
                }


            }
        });
    }

    public void showData() {
        adapterCustomerBooking = new AdapterCustomerBooking(CustomerBooking.this, artistBookingsList, userDTO);
        rvBookingcustomer.setAdapter(adapterCustomerBooking);



    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}
