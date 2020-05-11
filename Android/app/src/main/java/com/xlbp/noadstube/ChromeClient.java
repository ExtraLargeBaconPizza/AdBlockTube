package com.xlbp.noadstube;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class ChromeClient extends WebChromeClient
{
    public ChromeClient(WebView webView)
    {
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);


        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);

        // perf increases?
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.setDomStorageEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    public void onConsoleMessage(String message, int lineNumber, String sourceID)
    {
        Log.e("console.log", message);
    }

    // These two functions are needed to allow video to go full screen
    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
    {
        callback.onCustomViewHidden();
    }

    @Override
    public void onHideCustomView()
    {
        super.onHideCustomView();
    }

    // getVideoLoadingProgressView()

//    // loading image?
//    @Override
//    public Bitmap getDefaultVideoPoster()
//    {
//        super.getDefaultVideoPoster();
//
//        Log.e("ChromeClient","play");
//        return null;
//    }
}
