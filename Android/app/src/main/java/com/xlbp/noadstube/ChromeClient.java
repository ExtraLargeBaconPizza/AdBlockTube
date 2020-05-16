package com.xlbp.noadstube;

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

    // Fullscreen
    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback)
    {
        // Hide Navigation and Status Bar
        _mainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        _mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        _fullScreenFrameLayout.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onHideCustomView()
    {
        super.onHideCustomView();

        // Show Navigation and Status Bar
        _mainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

        _mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        _fullScreenFrameLayout.removeAllViews();
    }

    @Override
    public Bitmap getDefaultVideoPoster()
    {
        return Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
    }

    public void onConsoleMessage(String message, int lineNumber, String sourceID)
    {
        Log.e("console.log", message);
    }

    private MainActivity _mainActivity;
    private FrameLayout _fullScreenFrameLayout;
}
