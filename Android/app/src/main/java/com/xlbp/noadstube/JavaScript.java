package com.xlbp.noadstube;

import android.content.Intent;
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

    public void enterFullScreen()
    {
        evaluate("enterFullScreen();");
    }

    public void exitFullScreen()
    {
        evaluate("exitFullScreen();");
    }

    public void tapFullScreenButton()
    {
        evaluate("tapFullScreenButton();");
    }

    public void addShareButtonEventListener()
    {
        evaluate("addShareButtonEventListener();");
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
            Log.e("JavaScript", "Code eval failed. Calling again");

            evaluate(code);
        }
    }

    private class JavaScriptInterface
    {
        @JavascriptInterface
        public void simulateTap(float x, float y)
        {
            Helpers.simulateTap(_mainActivity, x, y);
        }

        @JavascriptInterface
        public void enterFullScreen()
        {
            _mainActivity.setIsFullScreen(true);

            Helpers.setOrientationToLandScape(_mainActivity);
        }

        @JavascriptInterface
        public void exitFullScreen()
        {
            _mainActivity.setIsFullScreen(false);

            Helpers.setOrientationToPortrait(_mainActivity);
        }

        @JavascriptInterface
        public void shareClicked(String url)
        {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, url);
            sendIntent.setType("text/plain");

            _mainActivity.startActivity(Intent.createChooser(sendIntent, "Share This Video"));
        }
    }


    private MainActivity _mainActivity;
    private WebView _webView;
}
