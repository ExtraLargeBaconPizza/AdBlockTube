
// this is the sudo constructor
(function ()
{
    initAdsMutationObserver();

    initMenuButtonMutationObserver();

    initSignInMutationObserver();

    initAccountMenuSkip();

    initTapHighlightColor();

    return 'successfully loaded javascript.js';
})();

// initAdsMutationObserver
function initAdsMutationObserver()
{
    var adsMutationObserver = new MutationObserver(function(mutations)
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
            else if (mutation.type  == "childList")
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
    var config = { attributes: true, attributeFilter: ['class'], childList: true, subtree: true };

    adsMutationObserver.observe(container, config);
}

function initMenuButtonMutationObserver()
{
    var menuButtonMutationObserver = new MutationObserver(function(mutations)
    {
        for (var mutation of mutations)
        {
            for (var node of mutation.addedNodes)
            {
                if (node.nodeName == 'YTM-MENU')
                {
                    node.style.display = "none";
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { childList: true, subtree: true };

    menuButtonMutationObserver.observe(container, config);
}

// initSignInMutationObserver
function initSignInMutationObserver()
{
    var signInMutationObserver = new MutationObserver(function(mutations)
    {
        if (window.location.href.includes("accounts"))
        {
            for (var mutation of mutations)
            {
                for (var node of mutation.addedNodes)
                {
                    if (node.nodeName == 'FOOTER')
                    {
                        node.style.display = "none";
                    }

                    if (node.textContent.includes('Forgot') || node.textContent.includes('Learn')  || node.textContent.includes('Create'))
                    {
                        removeUnwantedSignInElements();
                    }
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { childList: true, subtree: true };

    signInMutationObserver.observe(container, config);
}

function initAccountMenuSkip()
{
    window.addEventListener('popstate', function (event)
    {
        if (window.location.href.includes("menu"))
        {
            document.querySelector("#menu").style.display = "none";

            var isSignedIn = false;

            var checkExist = setInterval(function()
            {
                // already signed in
                if (document.querySelector('.active-account-name') != null)
                {
                    isSignedIn = true;

                    document.querySelector('.active-account-name').click();
                }

                if (document.querySelector('#simple-menu-header-title').innerText.includes("Accounts"))
                {
                    document.querySelector("#menu").style.display = "block";
                    clearInterval(checkExist);
                }

                // not signed in
                if (document.querySelector(".compact-link-metadata") != null && !isSignedIn)
                {
                    // Hide all the choices that can mess up the app
                    document.querySelector(".multi-page-menu-system-link-list").style.display = "none";

                    // Hide footer
                    document.querySelector("ytm-privacy-tos-footer-renderer").style.display = "none";

                    document.querySelector("#menu").style.display = "block";
                    clearInterval(checkExist);
                }
            }, 10);
        }
    });
}

function initTapHighlightColor()
{
    document.documentElement.style.webkitTapHighlightColor = "#00000000";
}

//////////////////////////////////////////////////////////////////////////
// public functions
// must return 'success';
//////////////////////////////////////////////////////////////////////////

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
        playerContainer.style.display = "none";

        // Need to delay first click so that the container has time to set its opacity to 0
        setTimeout(function()
        {
            window.androidWebViewClient.simulateClick(x, y);
        }, 50);

        // Need to delay the second click so its not a double click
        setTimeout(function()
        {
            window.androidWebViewClient.simulateClick(x, y);

            playerContainer.style.display = "block";
        }, 650);
    }
    else
    {
        window.androidWebViewClient.simulateClick(x, y);
    }

    return 'successfully called tapFullScreenButton()';
}

//////////////////////////////////////////////////////////////////////////
// private functions
//////////////////////////////////////////////////////////////////////////

function skipVideoAd()
{
    var videoAdsElement = document.querySelector('.video-ads');

    if (videoAdsElement != null && videoAdsElement.parentNode != null)
    {
        console.log('removed video-ads element skipVideoAd');

        videoAdsElement.parentNode.removeChild(videoAdsElement);
    }

    // Find any video ad that is playing and remove its src
    var ad = document.querySelector('.ad-showing');

    if (ad !== null && ad !== undefined)
    {
        var video = document.querySelector('video');

        if (video !== null && video !== undefined)
        {
            video.src = "";
        }
    }
}

function removeUnwantedSignInElements()
{
    var buttons = document.querySelectorAll('button');

    for (var button of buttons)
    {
        if (button.innerText.includes("Forgot email?"))
        {
            button.parentNode.style.display = "none";
        }
    }

    var links = document.querySelectorAll('a');

    for (var link of links)
    {
        if (link.innerText.includes("Learn more"))
        {
            link.parentNode.style.display = "none";
        }
    }

    var spans = document.querySelectorAll('span');

    for (var span of spans)
    {
        if (span.innerText.includes("Create account"))
        {
            span.parentNode.parentNode.style.display = "none";
        }

        if (span.innerText.includes("Forgot password?"))
        {
            span.style.display = "none";
        }
    }
}