package com.kamaii.rider.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.kamaii.rider.R;
import com.kamaii.rider.interfacess.Consts;

public class Terms extends AppCompatActivity {
    private WebView mWebView;
    private RelativeLayout rlclose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        mWebView = (WebView) findViewById(R.id.mWebView);
        rlclose = (RelativeLayout) findViewById(R.id.rlclose);
        rlclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWebView.setWebViewClient(new MyBrowser());
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(Consts.TERMS_URL);

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}