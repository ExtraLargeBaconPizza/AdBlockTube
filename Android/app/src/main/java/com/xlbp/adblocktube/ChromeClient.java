package com.xlbp.adblocktube;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebChromeClient;

public class ChromeClient extends WebChromeClient
{
    public ChromeClient()
    {
    }

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
}
