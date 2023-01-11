package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamaii.rider.DTO.AppointmentDTO;
import com.kamaii.rider.DTO.UserDTO;
import com.kamaii.rider.R;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.network.NetworkManager;
import com.kamaii.rider.preferences.SharedPrefrence;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.AdapterAppointmnet;
import com.kamaii.rider.utils.CustomTextViewBold;
import com.kamaii.rider.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppointmentFrag extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private View view;
    private String TAG = HistoryFragment.class.getSimpleName();
    private RecyclerView RVhistorylist;
    private AdapterAppointmnet adapterAppointmnet;
    private ArrayList<AppointmentDTO> appointmentDTOSList;
    private LinearLayoutManager mLayoutManager;
    private SharedPrefrence prefrence;
    private UserDTO userDTO;
    private CustomTextViewBold tvNo;
    private BaseActivity baseActivity;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_appointment, container, false);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.my_appoin));
        prefrence = SharedPrefrence.getInstance(getActivity());
        userDTO = prefrence.getParentUser(Consts.USER_DTO);

        setUiAction(view);
        return view;
    }

    public void setUiAction(View v) {
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh_layout);
        tvNo = v.findViewById(R.id.tvNo);
        RVhistorylist = v.findViewById(R.id.RVhistorylist);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        RVhistorylist.setLayoutManager(mLayoutManager);

                                        Log.e("Runnable", "FIRST");
                                        if (NetworkManager.isConnectToInternet(getActivity())) {
                                            swipeRefreshLayout.setRefreshing(true);
                                            getHistroy();

                                        } else {
                                       //     ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
                                        }
                                    }





    public void getHistroy() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_APPOINTMENT_API, getparm(), getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();

                if (flag) {
                    tvNo.setVisibility(View.GONE);
                    RVhistorylist.setVisibility(View.VISIBLE);
                    try {
                        appointmentDTOSList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<AppointmentDTO>>() {
                        }.getType();
                        appointmentDTOSList = (ArrayList<AppointmentDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    tvNo.setVisibility(View.VISIBLE);
                    RVhistorylist.setVisibility(View.GONE);
                }
            }
        });
    }

    public HashMap<String, String> getparm() {
        HashMap<String, String> parms = new HashMap<>();
        parms.put(Consts.USER_ID, userDTO.getUser_id());
        parms.put(Consts.ROLE, "1");
        return parms;
    }

    public void showData() {
        adapterAppointmnet = new AdapterAppointmnet(AppointmentFrag.this, appointmentDTOSList,userDTO);
        RVhistorylist.setAdapter(adapterAppointmnet);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }


    @Override
    public void onRefresh() {
        Log.e("ONREFREST_Firls", "FIRS");
        getHistroy();
    }
}
