package com.xlbp.adblocktube;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ViewClient extends WebViewClient
{
    public ViewClient(MainActivity mainActivity)
    {
        _mainActivity = mainActivity;
    }

    // this is needed to redirect after user login
    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url)
    {
        if (url.contains("myaccount"))
        {
            url = "https://m.youtube.com/";
        }

        webView.loadUrl(url);
        return false;
    }

    @Override
    public void doUpdateVisitedHistory(WebView webView, String url, boolean isReload)
    {
        super.doUpdateVisitedHistory(webView, url, isReload);

        _mainActivity.doUpdateVisitedHistory(url);
    }


    MainActivity _mainActivity;
}
