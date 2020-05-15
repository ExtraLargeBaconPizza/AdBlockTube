package com.xlbp.noadstube;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

public class JavaScript
{
    public JavaScript(MainActivity mainActivity, WebView webView)
    {
        _mainActivity = mainActivity;
        _webView = webView;

        _webView.addJavascriptInterface(new JavaScriptInterface(), "androidWebViewClient");
    }

    public void init()
    {
        initJavaScript();
    }


    public void initMutationObserver()
    {
        evaluate("initMutationObserver();");
    }

    public void skipPreRollAd()
    {
        evaluate("skipPreRollAd();");
    }

    public void removeMenuButton()
    {
        evaluate("removeMenuButton();");
    }

    public void clickFullScreen()
    {
        evaluate("clickFullScreen();");
    }

    public void exitFullScreen()
    {
        evaluate("exitFullScreen();");
    }

    public void initFullScreenChangedListener()
    {
        evaluate("initFullScreenChangedListener();");
    }

    public void test()
    {
        evaluate("test();");
    }

    private void initJavaScript()
    {
        String javaScript = Helpers.readTextFromResource(_mainActivity, R.raw.javascript);

        evaluate(javaScript);
    }

    private void evaluate(String code)
    {
        _webView.evaluateJavascript(code, (result) ->
        {
            evalResult(code, result);
        });
    }

    private void evalResult(String code, String evalResult)
    {
        if (evalResult.contains("success"))
        {
            Log.e("JavaScript", "evalResult " + evalResult);
        }
        else
        {
            Log.e("JavaScript", "evalResult Failed. Calling Again: " + code);

            evaluate(code);
        }
    }

    private class JavaScriptInterface
    {
        @JavascriptInterface
        public void fullScreenChanged(String isFullScreen)
        {
            _mainActivity.setIsFullScreen(isFullScreen.contains("true"));
        }
    }


    private MainActivity _mainActivity;
    private WebView _webView;
}
