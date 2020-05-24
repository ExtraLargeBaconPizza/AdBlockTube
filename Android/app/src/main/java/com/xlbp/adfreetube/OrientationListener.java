package com.xlbp.adfreetube;

import android.view.OrientationEventListener;

public class OrientationListener extends OrientationEventListener
{
    public OrientationListener(MainActivity mainActivity, JavaScript javaScript)
    {
        super(mainActivity);

        _mainActivity = mainActivity;
        _javaScript = javaScript;

        // Need to enable the orientation event listener
        this.enable();
    }

    @Override
    public void onOrientationChanged(int orientation)
    {
        if (MainActivity.IsPremium && _isWatchUrl)
        {
            int epsilon = 15;
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
                        _javaScript.exitFullScreen();
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
                        _javaScript.enterFullScreen();
                    }
                }
            }
        }
    }

    public void setIsWatchUrl(boolean isWatchUrl)
    {
        _isWatchUrl = isWatchUrl;
    }

    private boolean epsilonCheck(int a, int b, int epsilon)
    {
        return a > b - epsilon && a < b + epsilon;
    }


    private MainActivity _mainActivity;
    private JavaScript _javaScript;

    private boolean _isLandScape;
    private boolean _isWatchUrl;
}
