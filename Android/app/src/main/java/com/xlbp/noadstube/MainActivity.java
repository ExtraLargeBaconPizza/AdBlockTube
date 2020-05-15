package com.xlbp.noadstube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.webkit.WebView;

// TODO
// skip menu
// rotate fullscreen and back
// set my app as default
// hide blue link click
// hide covid news. it can suck my balls

// Pro
// dark mode
// casting
// mini video

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
//            if (_watchingVideo && _isFullScreen)
//            {
//                programmaticallyClickFullScreen();
//
//                _isFullScreen = false;
//            }

            _webView.goBack();
        }
        else
        {
            super.onBackPressed();
        }
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig)
//    {
//        super.onConfigurationChanged(newConfig);
//
//        if (_watchingVideo)
//        {
//            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
//            {
//                _javaScript.clickFullScreen();
//            }
//
//            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
//            {
//                _javaScript.exitFullScreen();
//            }
//        }
//    }

    public void doUpdateVisitedHistory(String url)
    {
        handleNewUrl(url);
    }

    public void setIsFullScreen(boolean isFullScreen)
    {
        _isfullScreen = isFullScreen;
    }

    private void init()
    {
        initWebView();

        _javaScript = new JavaScript(this, _webView);
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

    private void handleNewUrl(String url)
    {
        if (!_javaScriptInitialized)
        {
            _javaScriptInitialized = true;

            _javaScript.init();

            _javaScript.initMutationObserver();
        }

//        if (url.contains("menu"))
//        {
//            skipMenu();
//        }

        if (url.contains("watch"))
        {
            _watchingVideo = true;

            _javaScript.skipPreRollAd();
//            _javaScript.initFullScreenChangedListener();
//            // TODO - move this to a separate file or function
//            // unlocks the screen orientation after its locked into fullscreen
//            OrientationEventListener orientationEventListener = new OrientationEventListener(this)
//            {
//                @Override
//                public void onOrientationChanged(int orientation)
//                {
//                    int epsilon = 10;
//                    int portrait = 0;
//                    int leftLandscape = 90;
//                    int rightLandscape = 270;
//
//                    if (epsilonCheck(orientation, portrait, epsilon) ||
//                            epsilonCheck(orientation, leftLandscape, epsilon) ||
//                            epsilonCheck(orientation, rightLandscape, epsilon))
//                    {
//                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//                    }
//                }
//
//                private boolean epsilonCheck(int a, int b, int epsilon)
//                {
//                    return a > b - epsilon && a < b + epsilon;
//                }
//            };
//
//            orientationEventListener.enable();
        }
        else
        {
            _watchingVideo = false;
        }

//        _javaScript.removeMenuButton();
    }


    private WebView _webView;
    private ChromeClient _chromeClient;
    private JavaScript _javaScript;
    private boolean _javaScriptInitialized;
    private boolean _watchingVideo;

    private boolean _isfullScreen;
    private boolean _phoneRotated;
    private boolean _fullScreenButtonClicked;
}
