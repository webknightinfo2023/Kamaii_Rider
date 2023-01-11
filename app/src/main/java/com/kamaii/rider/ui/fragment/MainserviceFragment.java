package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.kamaii.rider.R;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.utils.CustomTextView;

public class MainserviceFragment extends Fragment implements View.OnClickListener {
    private View view;
    private BaseActivity baseActivity;
    private Services servicesfrag = new Services();
    private ServiceShippingFragment serviceShippingFragment = new ServiceShippingFragment();
    private FragmentManager fragmentManager;
    private CustomTextView tvPaid, tvUnPaid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_mainservice, container, false);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.invoice));
        fragmentManager = getChildFragmentManager();
        tvUnPaid = (CustomTextView) view.findViewById(R.id.tvUnPaid);
        tvPaid = (CustomTextView) view.findViewById(R.id.tvPaid);

        tvPaid.setOnClickListener(this);
        tvUnPaid.setOnClickListener(this);

        fragmentManager.beginTransaction().add(R.id.frame, servicesfrag).commit();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvPaid:
                setSelected(true, false);
                fragmentManager.beginTransaction().replace(R.id.frame, servicesfrag).commit();
                break;
            case R.id.tvUnPaid:
                setSelected(false, true);
                fragmentManager.beginTransaction().replace(R.id.frame, serviceShippingFragment).commit();
                break;
        }

    }

    public void setSelected(boolean firstBTN, boolean secondBTN) {
        if (firstBTN) {
            tvPaid.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tvPaid.setTextColor(getResources().getColor(R.color.white));
            tvUnPaid.setBackgroundColor(getResources().getColor(R.color.white));
            tvUnPaid.setTextColor(getResources().getColor(R.color.gray));
        }
        if (secondBTN) {
            tvPaid.setBackgroundColor(getResources().getColor(R.color.white));
            tvPaid.setTextColor(getResources().getColor(R.color.gray));
            tvUnPaid.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            tvUnPaid.setTextColor(getResources().getColor(R.color.white));


        }
        tvPaid.setSelected(firstBTN);
        tvUnPaid.setSelected(secondBTN);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }
}
