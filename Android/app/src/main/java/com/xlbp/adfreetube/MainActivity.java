package com.xlbp.adfreetube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.net.URI;
import java.net.URISyntaxException;

// TODO FREE
// logo and design bullshit
// set my app as default? deep linking
// handle not connected to internet
// check how often epsilonCheck is called
// onresume after video ends, but autoplay isnt on
//
// TODO Testing
// if video fails (listen for playback unplayable event from youtube?) or just search inner text (only seems to happen on chrome)
// onPause  / onResume / re-hydration testing
// test on tablets to make sure it goes to the mobile site
//
// TODO Pro aka v2 not gunna charge
// casting
// mini video (swipe to lower etc) document.querySelector('video').requestPictureInPicture();
// full zoom for two finger zoom gesture

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
    public void onResume()
    {
        super.onResume();

        if (_isFullScreen)
        {
            Helpers.hideNavigationAndStatusBars(this);

            Helpers.setOrientationToSensor(this);
        }
    }

    @Override
    public void onBackPressed()
    {
        if (_isFullScreen)
        {
            _javaScript.exitFullScreen(true);
        }
        else if (_webView.canGoBack())
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

        Log.e("MainActivity", "onConfigChange Landscape " + (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE));

        if (_isWatchingVideo)
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

        _webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);

        _webView.restoreState(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);

        // This is needed for when the Quality Settings radio button dialog is displayed. If the video is in
        // fullscreen, the navigation and status bar will be displayed and stay.
        if (!hasFocus && _isFullScreen)
        {
            Helpers.setOrientationToLandScape(this);
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
            initWebView();

            initJavascript();

            initOrientationEventListener();
        }
        else
        {
            showNoInternet();
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

        return false;
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

        _webView.loadUrl("https://m.youtube.com/");
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
        // We need to check if we've navigated to a new domain, if so we need to re-inject javascript
        // because it will have been erased. We only need to to check for new domains because m.youtube
        // and accounts.google don't completely reload when navigating to new urls.
        String urlDomain = "";

        try
        {
            URI uri = new URI(url);
            urlDomain = uri.getHost();
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }

        if (!urlDomain.equals(_currentUrlDomain))
        {
            _currentUrlDomain = urlDomain;

            _javaScript.init();
        }

        _isWatchingVideo = url.contains("watch");

        _orientationListener.setIsWatchUrl(_isWatchingVideo);

        if (_isWatchingVideo)
        {
            Helpers.setOrientationToSensor(this);
        }
    }

    private void showNoInternet()
    {
        findViewById(R.id.noInternetTextView).setVisibility(View.VISIBLE);
    }

    private WebView _webView;
    private JavaScript _javaScript;
    private OrientationListener _orientationListener;

    private String _currentUrlDomain;
    private boolean _isWatchingVideo;
    private boolean _isFullScreen;
}
