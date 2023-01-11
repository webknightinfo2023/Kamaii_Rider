package com.kamaii.rider.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kamaii.rider.R;
import com.kamaii.rider.databinding.FragmentRouteTrackBinding;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.TrackTimeAdapter;

import java.util.ArrayList;
import java.util.List;

public class RouteTrackFragment extends Fragment {

    List<String> arrayList;
    BaseActivity baseActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentRouteTrackBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_route_track, container, false);

        baseActivity.headerNameTV.setText("Time Route");

        baseActivity.ivLogo.setVisibility(View.GONE);
//        baseActivity.ivEditPersonal.setVisibility(View.GONE);
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);

        binding.list.setLayoutManager(new LinearLayoutManager(getContext()));

        arrayList = new ArrayList<>();

        arrayList.add("10:00");
        arrayList.add("11:00");
        arrayList.add("12:00");

        TrackTimeAdapter adapter = new TrackTimeAdapter(getContext(),arrayList);
        binding.list.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }
}
