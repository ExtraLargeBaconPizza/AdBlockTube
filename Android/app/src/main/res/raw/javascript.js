// initMutationObserver
function initMutationObserver()
{
    let observer = new MutationObserver(function(mutations)
    {
        for (let mutation of mutations)
        {
            for (let node of mutation.addedNodes)
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

    let container = document.documentElement;
    let config = { attributes: true, attributeFilter: ['class'], childList: true, subtree: true  };

    observer.observe(container, config);

    return 'successfully called initMutationObserver()';
}

// preroll ad skip
function skipPreRollAd()
{
    // We need to run a timer loop because we can't access event like video.onplaying.
    // they're blocked by the api
    let timeoutCounter = 0;

    let timeout = setInterval(() => 
    {
        let ad = document.querySelector('.ad-showing');

        if (ad !== null && ad !== undefined) 
        {
            let video = document.querySelector('video');

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

    return 'successfully called skipPreRollAd()';
}

// clickFullScreen
// we need to manually click the fullscreen because we can't call requestFullscreen.
// The API can only be initiated by a user gesture.
function clickFullScreen()
{
     document.querySelector('.fullscreen-icon').click();

//    document.querySelectorAll('c3-icon')[4].click();

    return 'successfully called clickFullScreen()';
}

function exitFullScreen()
{
    if (document.webkitCancelFullScreen)
    {
        console.log("document.webkitCancelFullScreen");
        document.webkitCancelFullScreen();
    }
    else if (document.webkitExitFullscreen)
    {
        console.log("document.webkitExitFullscreen");
        document.webkitExitFullscreen();
    }
    else
    {
        console.log("can't exit fullscreen");
    }

    return 'successfully called exitFullScreen()';
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

// initFullScreenChangedListener
function initFullScreenChangedListener()
{
    document.onwebkitfullscreenchange = function ()
    {
        window.androidWebViewClient.fullScreenChanged(document.webkitFullscreenElement == document.querySelector('#player-container-id'));
    };

    return 'successfully called fullScreenChangedListener()';
}

// initFullScreenIconClickedListener
 function initFullScreenIconClickedListener()
 {
     document.addEventListener('click', function (event)
     {
         if (event.target.matches('.fullscreen-icon') ||
             event.target.parentNode.matches('.fullscreen-icon') ||
             event.target.parentNode.parentNode.matches('.fullscreen-icon') ||
             event.target.parentNode.parentNode.parentNode.matches('.fullscreen-icon'))
         {
             console.log("fullScreenClicked " +  document.webkitFullscreenElement);
             console.log(document.webkitFullscreenElement == document.querySelector('#player-container-id'));
         }
     }, false);

     return 'successfully called initFullScreenIconClickedListener()';
 }

// function just to test things
function test()
{
//    let element = document.querySelector("video");
//
//    element.onplaying = function ()
//    {
//        console.log("onplaying");
//    }
//
//    element.addEventListener("playing", function()
//    {
//        console.log("playing");
//
//        if (element.requestFullscreen)
//        {
//            element.requestFullscreen();
//        }
//        else if (element.webkitRequestFullscreen)
//        {
//            element.webkitRequestFullscreen(Element.ALLOW_KEYBOARD_INPUT);
//        }
//    }, false);

    return 'successfully called test()';
}

// function removeAd()
// {
//     let ad = document.querySelector('.ad-showing');

//     if (ad !== null && ad !== undefined) 
//     {
//         let video = document.querySelector('video');

//         if (ad !== null && ad !== undefined) 
//         { 
//             if (isFinite(video.duration))
//             {
//                 console.log('Pre roll ad skipped recursive');
//                 video.currentTime = video.duration;

//                 // recursively call this to super skip
//                 removeAd();
//             }
//         }
//     }
// }

// this needs to be at the end to indicate successful initialization
(function ()
{
    return 'successfully loaded javascript.js';
})();