package com.xlbp.adblocktube;

import android.os.Build;
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

    @Override
    public void doUpdateVisitedHistory(WebView webView, String url, boolean isReload)
    {
        super.doUpdateVisitedHistory(webView, url, isReload);

        _mainActivity.doUpdateVisitedHistory(url);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            Toast.makeText(_mainActivity, "Error: Your internet connection may not be active", Toast.LENGTH_LONG).show();
        }
    }

    MainActivity _mainActivity;
}
