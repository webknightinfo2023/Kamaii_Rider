package com.kamaii.rider.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.FragmentMaprouteBinding;
import com.kamaii.rider.ui.activity.BaseActivity;

import java.util.ArrayList;

public class MaprouteFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    BaseActivity baseActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        FragmentMaprouteBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_maproute, container, false);

        baseActivity.headerNameTV.setText("Map Route");

        baseActivity.ivLogo.setVisibility(View.GONE);
//        baseActivity.ivEditPersonal.setVisibility(View.GONE);
        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.swOnOff.setVisibility(View.GONE);
        baseActivity.tvOnOff.setVisibility(View.GONE);

        binding.mapview.onCreate(savedInstanceState);
        binding.mapview.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.mapview.getMapAsync(this);

        binding.trackFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseActivity.loadHomeFragment(new RouteTrackFragment(),"route");
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap goo) {

        googleMap = goo;
        // For showing a move to my location button
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

        // For dropping a marker at a point on the Map
       // LatLng sydney = new LatLng(-34, 151);

        ArrayList<LatLng> markersArray = new ArrayList<>();
        markersArray.add(new LatLng(23.0418,72.5518));
        markersArray.add(new LatLng(23.0279,72.5055));

        for(int i = 0 ; i < markersArray.size() ; i++) {

            createMarker(markersArray.get(i).latitude, markersArray.get(i).longitude, "Test Marker", "test str", 0);
        }
       // googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

        // For zooming automatically to the location of the marker
        CameraPosition cameraPosition = new CameraPosition.Builder().target(markersArray.get(0)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {

        return googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;
    }
}