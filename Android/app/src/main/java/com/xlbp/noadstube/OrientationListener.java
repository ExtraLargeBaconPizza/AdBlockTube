package com.xlbp.noadstube;

import android.content.Context;
import android.view.OrientationEventListener;

public class OrientationListener extends OrientationEventListener
{
    public OrientationListener(MainActivity mainActivity, JavaScript javaScript)
    {
        super(mainActivity);

        _mainActivity = mainActivity;
        _javaScript = javaScript;

        // Need to enable the event listener
        this.enable();
    }

    @Override
    public void onOrientationChanged(int orientation)
    {
        if (MainActivity.IsPremium)
        {
            int epsilon = 10;
            int portrait = 0;
            int leftLandscape = 90;
            int rightLandscape = 270;

            if (epsilonCheck(orientation, portrait, epsilon))
            {
                if (_isLandScape)
                {
                    _isLandScape = false;

                    if (_mainActivity.getIsFullScreen())
                    {
                        _mainActivity.setIsFullScreen(false);

                        _javaScript.tapFullScreenButton();
                    }
                }
            }

            if (epsilonCheck(orientation, leftLandscape, epsilon) || epsilonCheck(orientation, rightLandscape, epsilon))
            {
                if (!_isLandScape)
                {
                    _isLandScape = true;

                    if (!_mainActivity.getIsFullScreen())
                    {
                        _mainActivity.setIsFullScreen(true);

                        _javaScript.tapFullScreenButton();
                    }
                }
            }
        }
    }

    private boolean epsilonCheck(int a, int b, int epsilon)
    {
        return a > b - epsilon && a < b + epsilon;
    }


    private MainActivity _mainActivity;
    private JavaScript _javaScript;

    private boolean _isLandScape;
}
