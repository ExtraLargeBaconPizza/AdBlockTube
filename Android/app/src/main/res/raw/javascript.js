// initMutationObserver
function initMutationObserver()
{
    var observer = new MutationObserver(function(mutations)
    {
        for (var mutation of mutations)
        {
            for (var node of mutation.addedNodes)
            {
                if (node.nodeName == 'YTM-PROMOTED-VIDEO-RENDERER')
                {
                    console.log('removed PROMO');
                    node.parentNode.removeChild(node);
                }

                if (node.nodeName == 'YTM-COMPANION-SLOT')
                {
                    console.log('removed COMPANION AD');
                    node.parentNode.removeChild(node);
                }

                if (node.nodeName == 'YTM-WATCH-METADATA-APP-PROMO-RENDERER')
                {
                    console.log('removed KIDS AD');
                    node.parentNode.removeChild(node);
                }

                if (node.nodeName == 'YTM-PROMOTED-SPARKLES-WEB-RENDERER')
                {
                    console.log('removed SPARKLES AD');
                    node.parentNode.parentNode.parentNode.removeChild(node.parentNode.parentNode);
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { attributes: true, attributeFilter: ['class'], childList: true, subtree: true  };

    observer.observe(container, config);

    return 'success initMutationObserver';
}

// preroll ad skip
function skipPreRollAd()
{
    // We need to run a timer loop because we can't access event like video.onplaying.
    // they're blocked by the api
    var timeoutCounter = 0;

    var timeout = setInterval(() => 
    {
        var ad = document.querySelector('.ad-showing');

        if (ad !== null && ad !== undefined) 
        {
            var video = document.querySelector('video');

            if (ad !== null && ad !== undefined) 
            { 
                if (isFinite(video.duration))
                {
                  console.log('Pre roll ad skipped');
                  video.currentTime = video.duration;
                }
            }
        }

        timeoutCounter++;

        if(timeoutCounter > 100)
        {
            console.log('Pre roll timer stopped');
            clearTimeout(timeout);
        }
    }, 100);

    return 'success skipPreRollAd';
}

// clickFullScreen
function clickFullScreen()
{
    // we need to manually click the fullscreen because we can't call requestFullscreen.
    // The API can only be initiated by a user gesture.
    document.querySelector('.fullscreen-icon').click();

    return 'success clickFullScreen';
}

// removeMenuButton
function removeMenuButton()
{
    var elem = document.getElementsByTagName('ytm-menu');

    if (elem.length > 0)
    {
        elem[0].style.display = 'none';
    }

    return 'success removeMenuButton';
}

// skipMenu
function skipMenu()
{
    document.getElementsByClassName('active-account-name')[0].click();

    return 'success skipMenu';
}

// fullScreenChangedListener
function initFullScreenChangedListener()
{
    document.onfullscreenchange = function ()
    {
        window.androidWebViewClient.fullScreenChanged(document.fullscreenElement == document.querySelector('#player-container-id'));
    };

    return 'success fullScreenChangedListener';
}

// this needs to be at the end to indicate successful initialization
(function ()
{
    return 'success loaded javascript.js';
})();