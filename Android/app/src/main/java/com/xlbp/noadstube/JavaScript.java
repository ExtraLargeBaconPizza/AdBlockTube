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

    public void tapFullScreenButton()
    {
        evaluate("tapFullScreenButton();");
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
            Log.e("JavaScript", evalResult);
        }
        else
        {
            Log.e("JavaScript", "Code eval failed. Calling again: " + code);

            evaluate(code);
        }
    }

    private class JavaScriptInterface
    {
        @JavascriptInterface
        public void simulateClick(float x, float y)
        {
            _mainActivity.simulateClick(x, y);
        }
    }


    private MainActivity _mainActivity;
    private WebView _webView;
}
