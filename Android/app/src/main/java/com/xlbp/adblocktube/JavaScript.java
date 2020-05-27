package com.xlbp.adblocktube;

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

    public void enterFullScreen(boolean forceLandscape)
    {
        evaluate("enterFullScreen(" + forceLandscape + ");");
    }

    public void exitFullScreen(boolean forcePortrait)
    {
        evaluate("exitFullScreen(" + forcePortrait + ");");
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
        public void onEnterFullScreen(boolean forceLandscape)
        {
            if (!_mainActivity.getIsFullScreen())
            {
                _mainActivity.setIsFullScreen(true);

                Helpers.hideNavigationAndStatusBars(_mainActivity);

                if (forceLandscape)
                {
                    Helpers.setOrientationToLandScape(_mainActivity);
                }
            }
        }

        @JavascriptInterface
        public void onExitFullScreen(boolean forcePortrait)
        {
            if (_mainActivity.getIsFullScreen())
            {
                _mainActivity.setIsFullScreen(false);

                Helpers.showNavigationAndStatusBars(_mainActivity);

                if (forcePortrait)
                {
                    Helpers.setOrientationToPortrait(_mainActivity);
                }
            }
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
