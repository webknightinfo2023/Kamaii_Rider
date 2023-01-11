package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kamaii.rider.R;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.ui.activity.BaseActivity;


public class SafetyFragment extends Fragment {


    private View view;
    // CustomTextView tvupdate;
    private BaseActivity baseActivity;
    // int version= -1;
    private WebView mWebView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_safety, container, false);
        baseActivity.headerNameTV.setText("Covid-19");
        mWebView = (WebView) view.findViewById(R.id.safetyWebView);
        mWebView.setWebViewClient(new MyBrowser123());
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(Consts.COVID_URL);

        return view;
    }

    private class MyBrowser123 extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        baseActivity = ((BaseActivity)activity);
    }
}