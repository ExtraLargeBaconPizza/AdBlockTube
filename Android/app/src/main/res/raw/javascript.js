// initMutationObserver
function initMutationObserver()
{
    var observer = new MutationObserver(function(mutations)
    {
        for (var mutation of mutations)
        {
            if(mutation.attributeName == "class")
            {
                if (mutation.target.classList.contains('ad-showing'))
                {
                    skipVideoAd();
                }
            }
            else
            {
                for (var node of mutation.addedNodes)
                {
                    if (node.parentNode != null)
                    {
                        if (node.nodeName == 'YTM-PROMOTED-VIDEO-RENDERER')
                        {
                            node.parentNode.removeChild(node);
                        }

                        if (node.nodeName == 'YTM-COMPANION-SLOT')
                        {
                            node.parentNode.removeChild(node);
                        }

                        if (node.nodeName == 'YTM-WATCH-METADATA-APP-PROMO-RENDERER')
                        {
                            node.parentNode.removeChild(node);
                        }

                        if (node.nodeName == 'YTM-PROMOTED-SPARKLES-WEB-RENDERER' && node.parentNode.parentNode.parentNode != null)
                        {
                            node.parentNode.parentNode.parentNode.removeChild(node.parentNode.parentNode);
                        }

                        if (node.classList != null && node.classList.contains("video-ads"))
                        {
                            node.parentNode.removeChild(node);
                        }
                    }
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { attributes: true, attributeFilter: ['class'], childList: true, subtree: true  };

    observer.observe(container, config);

    return 'successfully called initMutationObserver()';
}

function initTapHighlightColor()
{
    document.documentElement.style.webkitTapHighlightColor = "#00000000";

    return 'successfully called initTapHighlightColor()';
}

// skipVideoAd
var skipVideoAdRunning;

function skipVideoAd()
{
    var timeoutCounter = 0;

    if (skipVideoAdRunning != true)
    {
        skipVideoAdRunning = true;

         // We need to run a timer loop because we can't access event like video.onplaying.
         // they're blocked by the api

         var timeout = setInterval(() =>
         {
            // Remove backup video ads element
            var videoAdsElement = document.querySelector('.video-ads');

            if (videoAdsElement != null && videoAdsElement.parentNode != null)
            {
                console.log('removed video-ads element skipVideoAd');

                videoAdsElement.parentNode.removeChild(videoAdsElement);
            }
            
            // Find any video ad that is playing and skip to the end
            var ad = document.querySelector('.ad-showing');

            if (ad !== null && ad !== undefined)
            {
                var video = document.querySelector('video');

                if (video !== null && video !== undefined)
                {
                    console.log('removed VIDEO AD');

                    video.src = "";
                }
            }

            timeoutCounter++;

            if(timeoutCounter > 100)
            {
                console.log('Video ad timer stopped');

                skipVideoAdRunning = false;

                clearTimeout(timeout);
            }
         }, 100);
    }

    return 'successfully called skipVideoAd()';
}

function tapFullScreenButton()
{
    var fullScreenIcon = document.querySelector('.fullscreen-icon');
    var fullScreenIconRect = fullScreenIcon.getBoundingClientRect();

    var x = (fullScreenIconRect.left + fullScreenIconRect.right) / 2;
    var y = (fullScreenIconRect.top + fullScreenIconRect.bottom) / 2;

    // If the the control overlay is not showing, the first click will only bring it up.
    // In that case, we need to click a second time
    if (document.querySelector('.fadein') == null)
    {
        // Get the player controls first so they can be hidden ASAP
        var playerContainer = document.querySelector('#player-control-overlay');
        playerContainer.style.opacity = "0";

        // Need to delay first click so that the container has time to set its opacity to 0
        setTimeout(function()
        {
            window.androidWebViewClient.simulateClick(x, y);
        }, 50);

        // Need to delay the second click so its not a double click
        setTimeout(function()
        {
            window.androidWebViewClient.simulateClick(x, y);

            playerContainer.style.opacity = "1";
        }, 650);
    }
    else
    {
        window.androidWebViewClient.simulateClick(x, y);
    }

    return 'success enterFullScreen';
}

// removeMenuButton
function removeMenuButton()
{
    var elem = document.getElementsByTagName('ytm-menu');

    if (elem.length > 0)
    {
        elem[0].style.display = 'none';
    }

    return 'successfully called removeMenuButton()';
}

// skipMenu
function skipMenu()
{
    document.getElementsByClassName('active-account-name')[0].click();

    return 'successfully called skipMenu()';
}

// function just for testing things
function test()
{
    return 'successfully called test()';
}

// this needs to be at the end to indicate successful initialization
(function ()
{
    return 'successfully loaded javascript.js';
})();