package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.kamaii.rider.R;
import com.kamaii.rider.ui.activity.BaseActivity;


public class CheckInternetFragment extends Fragment {


    private static final String TAG = com.kamaii.rider.ui.fragment.CheckInternetFragment.class.getSimpleName();
    private BaseActivity baseActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_internet, container, false);

        view.findViewById(R.id.retry_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new CabBookingsFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, TAG);
                fragmentTransaction.commitAllowingStateLoss();

            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}