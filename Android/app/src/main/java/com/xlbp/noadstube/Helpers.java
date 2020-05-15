package com.xlbp.noadstube;

import android.app.Application;
import android.content.Context;

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

//    private String readTextFromResource(int resourceID)
//    {
//        // Parse javascript.js into a string then evaluate it
//        BufferedReader reader = null;
//
//        String text = "";
//
//        try
//        {
//            reader = new BufferedReader(new InputStreamReader(_mainActivity.getResources().openRawResource(R.raw.javascript)));
//
//            String mLine;
//
//            while ((mLine = reader.readLine()) != null)
//            {
//                text += mLine + '\n';
//            }
//        }
//        catch (IOException e)
//        {
//            Log.e("Javascript", "IOException 1: " + e);
//        }
//        finally
//        {
//            if (reader != null)
//            {
//                try
//                {
//                    reader.close();
//                }
//                catch (IOException e)
//                {
//                    Log.e("Javascript", "IOException 2: " + e);
//                }
//            }
//        }
//
//        return text;
//    }
}
