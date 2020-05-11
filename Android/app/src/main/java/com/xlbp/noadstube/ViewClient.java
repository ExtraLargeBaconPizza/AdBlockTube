package com.xlbp.noadstube;

import android.webkit.WebView;
import android.webkit.WebViewClient;

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

    MainActivity _mainActivity;
}
