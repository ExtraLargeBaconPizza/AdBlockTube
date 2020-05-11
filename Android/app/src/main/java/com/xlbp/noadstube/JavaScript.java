package com.xlbp.noadstube;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public void initFullScreenChangedListener()
    {
        evaluate("initFullScreenChangedListener();");
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

    private void initJavaScript()
    {
        // Parse javascript.js into a string then evaluate it
        BufferedReader reader = null;

        String javaScript = "";

        try
        {
            reader = new BufferedReader(new InputStreamReader(_mainActivity.getResources().openRawResource(R.raw.javascript)));

            String mLine;

            while ((mLine = reader.readLine()) != null)
            {
                javaScript += mLine + '\n';
            }
        }
        catch (IOException e)
        {
            Log.e("Javascript", "IOException 1: " + e);
        }
        finally
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                }
                catch (IOException e)
                {
                    Log.e("Javascript", "IOException 2: " + e);
                }
            }
        }

        evaluate(javaScript);
    }

    private class JavaScriptInterface
    {
        @JavascriptInterface
        public void fullScreenChanged(String isFullScreen)
        {
            Log.e("isFullScreen", "- " + isFullScreen);

            _mainActivity.onFullScreenChanged(isFullScreen.contains("true"));
        }
    }


    private MainActivity _mainActivity;
    private WebView _webView;
}
