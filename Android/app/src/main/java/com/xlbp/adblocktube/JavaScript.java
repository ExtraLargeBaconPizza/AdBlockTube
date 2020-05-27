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
        // check to see if custom JavaScript code has already been loaded
        _webView.evaluateJavascript("(function(){ return typeof init === 'function'; })();", (customJavaScriptLoaded) ->
        {
            if (!customJavaScriptLoaded.contains("true"))
            {
                loadCustomJavaScript();
            }
        });
    }

    public void setCurrentScreen(String url)
    {
        String currentScreen = "";

        if (url.contains("watch"))
        {
            currentScreen = "watch";
        }

        if (url.contains("menu"))
        {
            currentScreen = "menu";
        }

        if (url.contains("accounts"))
        {
            currentScreen = "accounts";
        }

        evaluate("SetCurrentScreen('" + currentScreen + "');");
    }

    public void enterFullScreen(boolean forceLandscape)
    {
        evaluate("EnterFullScreen(" + forceLandscape + ");");
    }

    public void exitFullScreen(boolean forcePortrait)
    {
        evaluate("ExitFullScreen(" + forcePortrait + ");");
    }

    private void loadCustomJavaScript()
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
        if (!evalResult.contains("success"))
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
