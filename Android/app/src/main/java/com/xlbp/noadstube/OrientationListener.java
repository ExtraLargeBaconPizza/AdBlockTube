package com.xlbp.noadstube;

import android.content.Context;
import android.view.OrientationEventListener;

public class OrientationListener extends OrientationEventListener
{
    public OrientationListener(Context context, JavaScript javaScript)
    {
        super(context);

        _javaScript = javaScript;

        // Need to enable the event listener
        this.enable();
    }

    @Override
    public void onOrientationChanged(int orientation)
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

                if (_isFullScreen)
                {
                    _isFullScreen = false;

                    _javaScript.tapFullScreenButton();
                }
            }
        }

        if (epsilonCheck(orientation, leftLandscape, epsilon) || epsilonCheck(orientation, rightLandscape, epsilon))
        {
            if (!_isLandScape)
            {
                _isLandScape = true;

                if (!_isFullScreen)
                {
                    _isFullScreen = true;

                    _javaScript.tapFullScreenButton();
                }
            }
        }
    }

    public boolean getIsFullScreen()
    {
        return _isFullScreen;
    }

    public void setFullScreen(boolean isFullScreen)
    {
        _isFullScreen = isFullScreen;
    }

    private boolean epsilonCheck(int a, int b, int epsilon)
    {
        return a > b - epsilon && a < b + epsilon;
    }

    private JavaScript _javaScript;

    private boolean _isFullScreen;
    private boolean _isLandScape;
}
