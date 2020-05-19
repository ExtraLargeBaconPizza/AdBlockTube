package com.xlbp.noadstube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;

// TODO
// skip menu for login / logout
// set my app as default?
// end of video, whilst fullscreen
// sharing button
// icon
// removeMenuButton might not always be needed
// if video fails (listen for playback unplayable event from youtube?) or just search inner text
// onPause  / onResume / re-hydration testing

// TODO Pro aka v2
// dark mode
// casting
// mini video (swipe to lower etc)

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

    // TODO - move to helpers
    public void simulateClick(float x, float y)
    {
        x = Helpers.dpToPixels(x);
        y = Helpers.dpToPixels(y);

        if (_orientationListener.getIsFullScreen())
        {
            y += _safeInset;
        }

        // TODO - refactor this?
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent.PointerProperties[] properties = new MotionEvent.PointerProperties[1];
        MotionEvent.PointerProperties pp1 = new MotionEvent.PointerProperties();
        pp1.id = 0;
        pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
        properties[0] = pp1;
        MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[1];
        MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();
        pc1.x = x;
        pc1.y = y;
        pc1.pressure = 1;
        pc1.size = 1;
        pointerCoords[0] = pc1;

        MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, 1, properties,
                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
        dispatchTouchEvent(motionEvent);

        motionEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, 1, properties,
                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
        dispatchTouchEvent(motionEvent);
    }

    public void setFullScreen(boolean isFullScreen)
    {
        _orientationListener.setFullScreen(isFullScreen);
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

        _orientationListener = new OrientationListener(this, _javaScript);
    }

    private void handleNewUrl(String url)
    {
        if (!_javaScriptInjected)
        {
            _javaScriptInjected = true;

            _javaScript.init();

            _javaScript.initMutationObserver();

            _javaScript.initTapHighlightColor();

            // TODO - lazy, this should be on resume with a init bool
            _safeInset = Helpers.getSafeInsetTop(this);
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
    private boolean _javaScriptInjected;
    private OrientationListener _orientationListener;

    private int _safeInset;
}
