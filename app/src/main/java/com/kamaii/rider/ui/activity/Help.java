package com.kamaii.rider.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.kamaii.rider.interfacess.Consts;
import com.kamaii.rider.R;

public class Help extends AppCompatActivity {

    private WebView mWebView;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        linearLayout = findViewById(R.id.llBack);
        mWebView = (WebView) findViewById(R.id.mWebView);
        mWebView.setWebViewClient(new MyBrowser2());
        mWebView.getSettings().setLoadsImagesAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(Consts.Help_URL);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Help.this, DocumentUploadTwoActivity.class);
                startActivity(intent1);
                finish();
            }
        });
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Help.this, DocumentUploadTwoActivity.class);
                startActivity(intent1);
                finish();
                overridePendingTransition(R.anim.stay, R.anim.slide_down);
            }
        });

    }

    private class MyBrowser2 extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}