package com.xlbp.noadstube;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.view.DisplayCutout;

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

    public static int getSafeInsetTop(Context context)
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

        return safeInsetTop;
    }

}
