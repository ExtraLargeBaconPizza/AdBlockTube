package com.xlbp.noadstube;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

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


    public void doUpdateVisitedHistory(String url)
    {
        Log.e("doUpdateVisitedHistory", url);

        if (url.contains("watch"))
        {
            _pageFinished = false;

            turnOffAds();
        }

        if (url.contains("menu"))
        {
            skipMenu();
        }

        removeMenuButton();
    }

    public void onPageFinished()
    {
        _pageFinished = true;
    }

    private void init()
    {
        initWebView();
    }

    private void initWebView()
    {
        _webView = findViewById(R.id.webView);

        _webView.getSettings().setJavaScriptEnabled(true);
        _webView.setWebViewClient(new XlbpWebViewClient(this));
        _webView.setWebChromeClient(new WebChromeClient()
        {
            public void onConsoleMessage(String message, int lineNumber, String sourceID)
            {
                Log.e("MyApplication", message + " -- From line "
                        + lineNumber + " of "
                        + sourceID);
            }
        });

        _webView.loadUrl("https://www.youtube.com/");
    }

    private void turnOffAds()
    {
        removeHeader();
        removeAds();
        adjustPlayerTop();
        adjustAppPaddingTop();
    }

    private void removeHeader()
    {
        String javaScript = "document.getElementById('header-bar').style.display = 'none';";

        _webView.evaluateJavascript(javaScript, this::headerRemoved);
    }

    private void headerRemoved(String headerRemoved)
    {
        if (!headerRemoved.contains("none"))
        {
            new Handler().postDelayed(() ->
            {
                if (!_pageFinished)
                {
                    removeHeader();
                }
            }, 5);
        }
    }

    // TODO - sometimes promoted ads arent added?
    private void removeAds()
    {
        String javaScript = "document.getElementById('YtSparklesVisibilityIdentifier').style.display = 'none';";

        _webView.evaluateJavascript(javaScript, this::adsRemoved);
    }

    private void adsRemoved(String adsRemoved)
    {
        if (!adsRemoved.contains("none") && !_pageFinished)
        {
            new Handler().postDelayed(() ->
            {
                removeAds();
            }, 5);
        }
    }

    private void adjustPlayerTop()
    {
        String javaScript = "document.getElementById('player-container-id').style.top = '0px';";

        _webView.evaluateJavascript(javaScript, this::playerTopAdjusted);
    }

    private void playerTopAdjusted(String playerTopAdjusted)
    {
        if (!playerTopAdjusted.contains("0px"))
        {
            new Handler().postDelayed(() ->
            {
                adjustPlayerTop();
            }, 5);
        }
    }

    private void adjustAppPaddingTop()
    {
        String javaScript = "document.getElementById('app').style.paddingTop = '0px';";

        _webView.evaluateJavascript(javaScript, this::appPaddingTopAdjusted);
    }

    private void appPaddingTopAdjusted(String appPaddingTopAdjusted)
    {
        if (!appPaddingTopAdjusted.contains("0px"))
        {
            new Handler().postDelayed(() ->
            {
                adjustAppPaddingTop();
            }, 5);
        }
    }

    private void skipMenu()
    {
        _webView.setAlpha(0);

        String javaScript = "javascript : (function(){ document.getElementsByClassName('active-account-name')[0].click(); })();";

        _webView.loadUrl(javaScript);
    }

    private void menuSkipped(String menuSkipped)
    {
        _webView.setAlpha(1);
    }

    private void removeMenuButton()
    {
        String javaScript = "javascript : (function() { var elem = document.getElementsByTagName('ytm-menu'); elem[0].style.display = 'none'; })();";

        _webView.loadUrl(javaScript);
    }


    private WebView _webView;
    private boolean _pageFinished;
}
