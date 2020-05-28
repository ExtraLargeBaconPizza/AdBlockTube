package com.xlbp.adblocktube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.net.URI;
import java.net.URISyntaxException;

// TODO Free

// TODO Pro aka v2
// casting
// auto rotate fullscreen
//     - orientation stops changing after changing brightness when fullscreen
// mini video (swipe to lower etc) document.querySelector('video').requestPictureInPicture();
// full zoom for two finger zoom gesture

public class MainActivity extends AppCompatActivity
{
    public static final boolean IsPremium = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if (_isFullScreen)
        {
            Helpers.hideNavigationAndStatusBars(this);

            Helpers.setOrientationToLandScape(this);
        }
    }

    @Override
    public void onBackPressed()
    {
        if (_isFullScreen)
        {
            _javaScript.exitFullScreen(true);
        }
        else if (_webView != null && _webView.canGoBack())
        {
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

        if (_isWatchingVideo && IsPremium)
        {
            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
            {
                _javaScript.enterFullScreen(false);
            }

            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
            {
                _javaScript.exitFullScreen(false);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);

        if (_webView != null)
        {
            _webView.saveState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        if (_webView != null)
        {
            _webView.restoreState(savedInstanceState);
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        // This is needed for when the Quality Settings radio button dialog is displayed. If the video is in
        // fullscreen, the navigation and status bar will be displayed and stay.
        if (!hasFocus && _isFullScreen)
        {
            Helpers.hideNavigationAndStatusBars(this);
        }
    }

    public void doUpdateVisitedHistory(String url)
    {
        handleNewUrl(url);
    }

    public boolean getIsFullScreen()
    {
        return _isFullScreen;
    }

    public void setIsFullScreen(boolean isFullScreen)
    {
        _isFullScreen = isFullScreen;
    }

    private void init()
    {
        if (checkIfInternetAvailable())
        {
            initDeepLink();

            initWebView();

            initJavascript();

            initOrientationEventListener();
        }
    }

    private boolean checkIfInternetAvailable()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }

        showNoInternet();

        return false;
    }

    private void initDeepLink()
    {
        Uri deepLinkData = getIntent().getData();

        _deepLink = deepLinkData != null ? deepLinkData.toString() : "";

        if (_deepLink.contains("http://"))
        {
            _deepLink.replace("http://", "https://");
        }
    }

    private void initWebView()
    {
        _webView = findViewById(R.id.webView);

        _webView.setWebChromeClient(new ChromeClient());
        _webView.setWebViewClient(new ViewClient(this));

        _webView.getSettings().setJavaScriptEnabled(true);
        _webView.getSettings().setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            _webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        String urlToLoad = _deepLink != "" ? _deepLink : "https://m.youtube.com/";

        _webView.loadUrl(urlToLoad);
    }

    private void initJavascript()
    {
        _javaScript = new JavaScript(this, _webView);
    }

    private void initOrientationEventListener()
    {
        Helpers.setOrientationToPortrait(this);

        _orientationListener = new OrientationListener(this);
    }

    private void handleNewUrl(String url)
    {
        _javaScript.init();

        _javaScript.setCurrentScreen(url);

        _isWatchingVideo = url.contains("watch");

        _orientationListener.setIsWatchUrl(_isWatchingVideo);

        if (_isWatchingVideo && IsPremium)
        {
            Helpers.setOrientationToSensor(this);
        }
    }

    private void showNoInternet()
    {
        findViewById(R.id.noInternetImageView).setVisibility(View.VISIBLE);
        findViewById(R.id.noInternetTextView).setVisibility(View.VISIBLE);
    }


    private String _deepLink;

    private WebView _webView;
    private JavaScript _javaScript;
    private OrientationListener _orientationListener;

    private boolean _isWatchingVideo;
    private boolean _isFullScreen;
}
