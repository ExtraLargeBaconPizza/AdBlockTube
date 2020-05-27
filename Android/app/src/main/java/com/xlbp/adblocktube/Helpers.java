package com.xlbp.adblocktube;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Helpers
{
    public static String readTextFromResource(Context context, int resourceID)
    {
        InputStream raw = context.getResources().openRawResource(resourceID);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int i;

        try
        {
            i = raw.read();
            while (i != -1)
            {
                stream.write(i);
                i = raw.read();
            }
            raw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return stream.toString();
    }

    public static int dpToPixels(float dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density + 0.5f);
    }

    public static void hideNavigationAndStatusBars(MainActivity mainActivity)
    {
        mainActivity.runOnUiThread(() ->
        {
            mainActivity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        });
    }

    public static void showNavigationAndStatusBars(MainActivity mainActivity)
    {
        mainActivity.runOnUiThread(() ->
        {
            mainActivity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        });
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public static void setOrientationToLandScape(MainActivity mainActivity)
    {
        mainActivity.runOnUiThread(() ->
        {
            mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        });
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public static void setOrientationToPortrait(MainActivity mainActivity)
    {
        mainActivity.runOnUiThread(() ->
        {
            mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        });
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public static void setOrientationToSensor(MainActivity mainActivity)
    {
        mainActivity.runOnUiThread(() ->
        {
            mainActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        });
    }
}
