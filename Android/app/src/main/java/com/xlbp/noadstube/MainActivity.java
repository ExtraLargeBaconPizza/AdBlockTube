package com.xlbp.noadstube;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

// TODO
// skip menu
// rotate fullscreen and back
// small player thingy
// remove big ugly loading image
// set my app as default

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

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        if (_watchingVideo)
        {
            if (!_fullScreenButtonClicked && !_phoneRotated)
            {
                _fullScreenButtonClicked = true;

                _javaScript.clickFullScreen();
            }
            else if (!_phoneRotated)
            {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }

//            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
//            {
//            }
//
//            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
//            {
//                _javaScript.clickFullScreen();
//            }
        }
    }

    public void doUpdateVisitedHistory(String url)
    {
        handleNewUrl(url);
    }

    public void onFullScreenChanged(boolean fullScreen)
    {
        handleFullScreenChanged(fullScreen);
    }

    private void init()
    {
        initWebView();

        _javaScript = new JavaScript(this, _webView);
    }

    private void initWebView()
    {
        _webView = findViewById(R.id.webView);

        _webView.setWebViewClient(new ViewClient(this));
        _webView.setWebChromeClient(new ChromeClient(_webView));

        _webView.loadUrl("https://m.youtube.com/");
    }

    private void handleNewUrl(String url)
    {
        if (!_javaScriptInitialized)
        {
            _javaScriptInitialized = true;

            _javaScript.init();

            _javaScript.initMutationObserver();

            _javaScript.initFullScreenChangedListener();
        }

//        if (url.contains("menu"))
//        {
//            skipMenu();
//        }

        if (url.contains("watch"))
        {
            _watchingVideo = true;

            _javaScript.skipPreRollAd();
        }
        else
        {
            _watchingVideo = false;
        }

        _javaScript.removeMenuButton();
    }

    private void handleFullScreenChanged(boolean fullScreen)
    {
        _isfullScreen = fullScreen;

        if (_isfullScreen)
        {
            hideNavigation();
        }
        else
        {
            showNavigation();
        }

        if (!_fullScreenButtonClicked)
        {
            _phoneRotated = true;

            if (_isfullScreen)
            {
                rotateToLandscape();
            }
            else
            {
                rotateToPortrait();
            }
        }

    }

    private void hideNavigation()
    {
        runOnUiThread(() ->
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        });
    }

    private void showNavigation()
    {
        runOnUiThread(() ->
        {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        });
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void rotateToLandscape()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private void rotateToPortrait()
    {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
    }


    private WebView _webView;
    private JavaScript _javaScript;
    private boolean _javaScriptInitialized;
    private boolean _watchingVideo;

    private boolean _isfullScreen;
    private boolean _phoneRotated;
    private boolean _fullScreenButtonClicked;
}
