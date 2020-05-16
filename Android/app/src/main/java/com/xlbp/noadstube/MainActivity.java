package com.xlbp.noadstube;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

// TODO
// skip menu for login / logout
// set my app as default ?
// end of video, whilst fullscreen

// TODO Pro aka v2
// dark mode
// casting
// mini video
// rotate fullscreen and back

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void onBackPressed()
    {
        if (_webView.canGoBack())
        {
            _webView.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        _webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        _webView.restoreState(savedInstanceState);
    }

    public void doUpdateVisitedHistory(String url)
    {
        handleNewUrl(url);
    }

    private void init()
    {
        initWebView();

        initJavascript();

        setOrientationPortrait();
    }

    private void initWebView()
    {
        _webView = findViewById(R.id.webView);

        _chromeClient = new ChromeClient(this);
        _webView.setWebChromeClient(_chromeClient);

        _webView.setWebViewClient(new ViewClient(this));

        _webView.getSettings().setJavaScriptEnabled(true);
        _webView.getSettings().setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            _webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        _webView.loadUrl("https://m.youtube.com/");
    }

    private void initJavascript()
    {
        _javaScript = new JavaScript(this, _webView);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void setOrientationPortrait()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    private void handleNewUrl(String url)
    {
        if (!_javaScriptInitialized)
        {
            _javaScriptInitialized = true;

            _javaScript.init();

            _javaScript.initMutationObserver();

            _javaScript.initTapHighlightColor();
        }

//        if (url.contains("menu"))
//        {
//            skipMenu();
//        }

        if (url.contains("watch"))
        {
            _javaScript.skipVideoAd();
        }

        _javaScript.removeMenuButton();
    }


    private WebView _webView;
    private ChromeClient _chromeClient;
    private JavaScript _javaScript;
    private boolean _javaScriptInitialized;
}
