package com.xlbp.noadstube;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class XlbpWebViewClient extends WebViewClient
{
    public XlbpWebViewClient(MainActivity mainActivity)
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
    public void onPageFinished(WebView webView, String url)
    {
        super.onPageFinished(webView, url);

        _mainActivity.onPageFinished();
    }

    MainActivity _mainActivity;
}
