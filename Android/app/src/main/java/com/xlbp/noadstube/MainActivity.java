package com.xlbp.noadstube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import java.net.URI;
import java.net.URISyntaxException;

// TODO
// go back before addShareButtonEventListener is successful, so it keeps looping
// set my app as default? deep linking
// if video fails (listen for playback unplayable event from youtube?) or just search inner text
// onPause  / onResume / re-hydration testing
// null check on exit fullscreen?
// full screen error? stays same size as portrait
// test on tablets to make sure it goes to the mobile site

// TODO Pro aka v2
// dark mode
// casting
// mini video (swipe to lower etc)
// end of video, whilst fullscreen
// play audio in background / meh
// fullscreen double tap. look in the website script, change player_doubletap_to_seek=true to false;

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

    // TODO - move to helpers
//    public void simulateClick(float x, float y)
//    {
//        x = Helpers.dpToPixels(x);
//        y = Helpers.dpToPixels(y);
//
//        if (_isFullScreen)
//        {
//            y += _safeInset;
//        }
//
//        // TODO - refactor this?
//        long downTime = SystemClock.uptimeMillis();
//        long eventTime = SystemClock.uptimeMillis();
//        MotionEvent.PointerProperties[] properties = new MotionEvent.PointerProperties[1];
//        MotionEvent.PointerProperties pp1 = new MotionEvent.PointerProperties();
//        pp1.id = 0;
//        pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
//        properties[0] = pp1;
//        MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[1];
//        MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();
//        pc1.x = x;
//        pc1.y = y;
//        pc1.pressure = 1;
//        pc1.size = 1;
//        pointerCoords[0] = pc1;
//
//        MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime,
//                MotionEvent.ACTION_DOWN, 1, properties,
//                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
//        dispatchTouchEvent(motionEvent);
//
//        motionEvent = MotionEvent.obtain(downTime, eventTime,
//                MotionEvent.ACTION_UP, 1, properties,
//                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
//        dispatchTouchEvent(motionEvent);
//    }

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
        initWebView();

        initJavascript();

        initOrientationEventListener();
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

    private void initOrientationEventListener()
    {
        _chromeClient.setOrientationToPortrait();

        if (IsPremium)
        {
            _orientationListener = new OrientationListener(this, _javaScript);
        }
    }

    private void handleNewUrl(String url)
    {
        if (Helpers.SafeInsetTop == 0)
        {
            Helpers.initSafeInsetTop(this);
        }

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
    }


    private WebView _webView;
    private ChromeClient _chromeClient;
    private JavaScript _javaScript;
    private OrientationListener _orientationListener;

    private String _currentUrlDomain;
    private boolean _isFullScreen;
}
