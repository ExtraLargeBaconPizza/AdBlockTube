package com.xlbp.adfreetube;

import android.util.Log;
import android.view.OrientationEventListener;

// the whole point of this class is to unlock the screen orientation
public class OrientationListener extends OrientationEventListener
{
    public OrientationListener(MainActivity mainActivity)
    {
        super(mainActivity);

        _mainActivity = mainActivity;

        _epsilon = 10;
        _portrait = 0;
        _leftLandscape = 90;
        _rightLandscape = 270;

        // Need to enable the orientation event listener
        this.enable();
    }

    @Override
    public void onOrientationChanged(int orientation)
    {
        if (_isWatchingVideo && MainActivity.IsPremium)
        {
            if (!_isLandScape && _mainActivity.getIsFullScreen())
            {
                if (epsilonCheck(orientation, _leftLandscape, _epsilon) || epsilonCheck(orientation, _rightLandscape, _epsilon))
                {
                    _isLandScape = true;

                    Helpers.setOrientationToSensor(_mainActivity);
                }
            }

            if (_isLandScape && !_mainActivity.getIsFullScreen())
            {
                if (epsilonCheck(orientation, _portrait, _epsilon))
                {
                    _isLandScape = false;

                    Helpers.setOrientationToSensor(_mainActivity);
                }
            }
        }
    }

    public void setIsWatchUrl(boolean isWatchUrl)
    {
        _isWatchingVideo = isWatchUrl;
    }

    private boolean epsilonCheck(int a, int b, int epsilon)
    {
        return a > b - epsilon && a < b + epsilon;
    }


    private MainActivity _mainActivity;

    private boolean _isLandScape;
    private boolean _isWatchingVideo;

    private int _epsilon;
    private int _portrait;
    private int _leftLandscape;
    private int _rightLandscape;
}
