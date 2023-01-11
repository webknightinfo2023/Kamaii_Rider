package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.mapboxsdk.Mapbox;
import com.kamaii.rider.R;
import com.kamaii.rider.databinding.FragmentTvideoBinding;
import com.kamaii.rider.https.HttpsRequest;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.interfacess.Helper;
import com.kamaii.rider.interfacess.OnSelectedItemListener;
import com.kamaii.rider.model.TvideoModel;
import com.kamaii.rider.ui.activity.BaseActivity;
import com.kamaii.rider.ui.adapter.TvideoAdapter;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TVideoFragment extends Fragment {

    private BaseActivity baseActivity;
    FragmentTvideoBinding binding;
    private LinearLayoutManager mLayoutManager;
    TvideoAdapter tvideoAdapter;
    ProgressDialog progressDialog;
    private HashMap<String, String> parms = new HashMap<>();
    private ArrayList<TvideoModel> tvideoModelArrayList=new ArrayList<>();
    private String TAG = TVideoFragment.class.getSimpleName();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tvideo, container, false);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        baseActivity.headerNameTV.setText(getResources().getString(R.string.tvideo));


        baseActivity.ivLogo.setVisibility(View.GONE);
        baseActivity.headerNameTV.setText(getContext().getResources().getString(R.string.tvideo));

        init();

        return binding.getRoot();
    }

    private void init() {

        binding.fragmentYoutubeWebview.setWebViewClient(new MyBrowser2());
        binding.fragmentYoutubeWebview.getSettings().setLoadsImagesAutomatically(true);
        binding.fragmentYoutubeWebview.getSettings().setJavaScriptEnabled(true);
        binding.fragmentYoutubeWebview.loadUrl("https://www.youtube.com/channel/UCD_NolR9RK9FQ6kQtQ0hHcQ/featured");

    }

    private class MyBrowser2 extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        baseActivity = (BaseActivity) activity;
    }


    public void gettvideo() {

        progressDialog.show();
        // ProjectUtils.showProgressDialog(getActivity(), false, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_TVIDEO, parms, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {

                progressDialog.dismiss();
                if (flag) {



                    try {
                        tvideoModelArrayList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<TvideoModel>>() {
                        }.getType();
                        tvideoModelArrayList = (ArrayList<TvideoModel>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);




                        tvideoAdapter=new TvideoAdapter(getActivity(),tvideoModelArrayList,onsubItemListener);




                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {


                }
            }
        });


    }

    private OnSelectedItemListener onsubItemListener=new OnSelectedItemListener() {
        @Override
        public void setOnClick(String selectionString, int position, String url, String shipping, String mylocation) {


            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            try {
                getActivity().startActivity(webIntent);
            } catch (ActivityNotFoundException ex) {
            }

        }
    };

}
