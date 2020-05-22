package com.xlbp.noadstube;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.SystemClock;
import android.view.DisplayCutout;
import android.view.MotionEvent;

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

    public static int SafeInsetTop;

    public static void initSafeInsetTop(Context context)
    {
        int safeInsetTop = 0;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P)
        {
            DisplayCutout displayCutout =
                    ((Activity) context)
                            .getWindow()
                            .getDecorView()
                            .getRootWindowInsets()
                            .getDisplayCutout();

            assert displayCutout != null;
            safeInsetTop = displayCutout.getSafeInsetTop();
        }
        else
        {
            safeInsetTop = Helpers.dpToPixels(24);
        }

        SafeInsetTop = safeInsetTop;
    }

    public static void simulateTap(MainActivity mainActivity, float x, float y)
    {
        x = Helpers.dpToPixels(x);
        y = Helpers.dpToPixels(y);

        if (mainActivity.getIsFullScreen())
        {
            y += SafeInsetTop;
        }

        // todo - refactor this
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent.PointerProperties[] properties = new MotionEvent.PointerProperties[1];
        MotionEvent.PointerProperties pp1 = new MotionEvent.PointerProperties();
        pp1.id = 0;
        pp1.toolType = MotionEvent.TOOL_TYPE_FINGER;
        properties[0] = pp1;
        MotionEvent.PointerCoords[] pointerCoords = new MotionEvent.PointerCoords[1];
        MotionEvent.PointerCoords pc1 = new MotionEvent.PointerCoords();
        pc1.x = x;
        pc1.y = y;
        pc1.pressure = 1;
        pc1.size = 1;
        pointerCoords[0] = pc1;

        MotionEvent motionEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_DOWN, 1, properties,
                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
        mainActivity.dispatchTouchEvent(motionEvent);

        motionEvent = MotionEvent.obtain(downTime, eventTime,
                MotionEvent.ACTION_UP, 1, properties,
                pointerCoords, 0, 0, 1, 1, 0, 0, 0, 0);
        mainActivity.dispatchTouchEvent(motionEvent);
    }
}
