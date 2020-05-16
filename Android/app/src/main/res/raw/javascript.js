// initMutationObserver
function initMutationObserver()
{
    let observer = new MutationObserver(function(mutations)
    {
        for (let mutation of mutations)
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
                for (let node of mutation.addedNodes)
                {
                    if (node.parentNode != null)
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

                        if (node.nodeName == 'YTM-PROMOTED-SPARKLES-WEB-RENDERER' && node.parentNode.parentNode.parentNode != null)
                        {
                            console.log('removed SPARKLES AD');
                            node.parentNode.parentNode.parentNode.removeChild(node.parentNode.parentNode);
                        }

                        if (node.classList != null && node.classList.contains("video-ads"))
                        {
                            console.log('removed video-ads element initMutationObserver');
                            node.parentNode.removeChild(node);
                        }
                    }
                }
            }
        }
    });

    let container = document.documentElement;
    let config = { attributes: true, attributeFilter: ['class'], childList: true, subtree: true  };

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

         let timeout = setInterval(() =>
         {
            // Remove backup video ads element
            var videoAdsElement = document.querySelector('.video-ads');

            if (videoAdsElement != null && videoAdsElement.parentNode != null)
            {
                console.log('removed video-ads element skipVideoAd');

                videoAdsElement.parentNode.removeChild(videoAdsElement);
            }
            
            // Find any video ad that is playing and skip to the end
            let ad = document.querySelector('.ad-showing');

            if (ad !== null && ad !== undefined)
            {
                let video = document.querySelector('video');

                if (video !== null && video !== undefined)
                {
                    console.log('removed VIDEO AD SRC');

                    video.src = "";
//                    if (isFinite(video.duration))
//                    {
//                        console.log('Video ad skipped');
//                        video.currentTime = video.duration;
//                    }
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

// removeMenuButton
function removeMenuButton()
{
    let elem = document.getElementsByTagName('ytm-menu');

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

// function just to test things
function test()
{

    return 'successfully called test()';
}

// this needs to be at the end to indicate successful initialization
(function ()
{
    return 'successfully loaded javascript.js';
})();