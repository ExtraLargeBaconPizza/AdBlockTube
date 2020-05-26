package com.xlbp.adfreetube;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.FrameLayout;

public class ChromeClient extends WebChromeClient
{
    public ChromeClient(MainActivity mainActivity)
    {
        _mainActivity = mainActivity;

        _fullScreenFrameLayout = _mainActivity.findViewById(R.id.fullScreenFrameLayout);
    }

//    // Enter FullScreen
//    @Override
//    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback customViewCallback)
//    {
//        _mainActivity.setIsFullScreen(true);
//
//        _fullScreenFrameLayout.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//    }
//
//    // Exit FullScreen
//    @Override
//    public void onHideCustomView()
//    {
//        super.onHideCustomView();
//
//        _mainActivity.setIsFullScreen(false);
//
//        _fullScreenFrameLayout.removeAllViews();
//    }

    @Override
    public Bitmap getDefaultVideoPoster()
    {
        // this hides an ugly loading play icon
        return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
    }

    public void onConsoleMessage(String message, int lineNumber, String sourceID)
    {
        Log.e("console.log", message);
    }


    private MainActivity _mainActivity;
    private FrameLayout _fullScreenFrameLayout;
}
