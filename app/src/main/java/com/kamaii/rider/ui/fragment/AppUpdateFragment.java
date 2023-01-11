package com.kamaii.rider.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.Fragment;

import com.kamaii.rider.R;
import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.ui.activity.BaseActivity;


public class AppUpdateFragment extends Fragment {

    private View view;
    private BaseActivity baseActivity;
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_appupdate, container, false);
        baseActivity.headerNameTV.setText(getResources().getString(R.string.app_update));
        setUiAction(view);
        return view;
    }

    public void setUiAction(View v) {
        mWebView = (WebView) view.findViewById(R.id.mWebView);
        mWebView.setWebViewClient(new MyBrowser());
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(Consts.About_URL);


    }


    private class MyBrowser extends WebViewClient {
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
}
