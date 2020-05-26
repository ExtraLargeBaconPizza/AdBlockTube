// this is the sudo constructor
(function ()
{
    // The point of the app
    initAdsMutationObserver();

    // Menu/Account
    initMenuButtonMutationObserver();

    initAccountMenuSkip();

    initSignInMutationObserver();

    // Watch screen specific
    initFullScreenMutationObserver();

    initShareButtonMutationObserver();

    initVideoEndedMutationObserver();

    // Hide blue click
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

                if (node.nodeName == "YTM-TOPBAR-MENU-BUTTON-RENDERER")
                {
                    if(!node.innerHTML.includes("ytm-profile-icon"))
                    {
                        var accountButton = document.querySelectorAll(".topbar-menu-button-avatar-button")[1];

                        accountButton.setAttribute("onclick", "window.location.href = 'https://accounts.google.com/ServiceLogin?service=youtube&uilel=3&passive=true&continue=https%3A%2F%2Fwww.youtube.com%2Fsignin%3Faction_handle_signin%3Dtrue%26app%3Dm%26hl%3Den%26next%3Dhttps%253A%252F%252Fm.youtube.com%252F%253Fnoapp%253D1&hl=en'");
                    }
                }
            }
        }
    });

    // container always needs to be document.documentElement because it will never be null
    var container = document.documentElement;
    var config = { childList: true, subtree: true };

    menuButtonMutationObserver.observe(container, config);
}

// We cant use a mutation observer because we're skipping a screen
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

                    // This is needed to fix a m.youtube bug. User could not scroll after pressing the 'x' button
                    // in the accounts screen. This was caused because the 'modal-open-body' attribute is not being removed
                    // from the body element. So we need to do it manually
                    document.querySelector("#simple-menu-header-close-button").addEventListener("click", function(e)
                    {
                        document.body.removeAttribute("modal-open-body");
                    });

                    clearInterval(checkExist);
                }
            }, 10);
        }
    });
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

                    // this is a different login flow for some reason
                    if (node.nodeName == 'UL' && node.id == "footer-list")
                    {
                        node.parentNode.parentNode.style.display = "none";
                    }

                    // TODO - handle alternate sign in route
                    if (node.textContent.includes("Find my account") || node.textContent.includes("Create account") )
                    {
//                        node.style.display = "none";
                    }
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { childList: true, subtree: true };

    signInMutationObserver.observe(container, config);
}

// initFullScreenMutationObserver
// I need to have all this nonsense so that entering/exiting fullscreen is consistent.
// This is done by removing all of youtube fullscreen click event listeners and manually doing everything
function initFullScreenMutationObserver()
{
    var fullScreenMutationObserver = new MutationObserver(function(mutations)
    {
        for (var mutation of mutations)
        {
            for (var node of mutation.addedNodes)
            {
                if(node.nodeName == "BUTTON" && node.classList.contains("fullscreen-icon"))
                {
                    // need to null out youtubes onclick listener
                    document.querySelector(".fullscreen-icon").onclick = null;

                    document.querySelector(".fullscreen-icon").addEventListener("click", function(e)
                    {
                        // need to null out the onclick listener again because it will reattach
                        document.querySelector(".fullscreen-icon").onclick = null;

                        var isFullscreen = document.body.getAttribute("faux-fullscreen");

                        if (isFullscreen != null)
                        {
                            exitFullScreen(true);
                        }
                        else
                        {
                            enterFullScreen(true);
                        }
                    });
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { childList: true, subtree: true };

    fullScreenMutationObserver.observe(container, config);
}

function initShareButtonMutationObserver()
{
    // The Share buttons onclick event listener gets re-initialized any time a c3-material-button-button is clicked.
    // We need to use addEventListener to not override the other buttons onclick eventlisteners.
    var shareButtonMutationObserver = new MutationObserver(function(mutations)
    {
        for (var mutation of mutations)
        {
            for (var node of mutation.addedNodes)
            {
                if(node.nodeName == "BUTTON" && node.classList.contains("c3-material-button-button"))
                {
                    if (node.innerText.includes("Share"))
                    {
                        // remove onclick event listener at the start
                        document.querySelector('[aria-label="Share"]').onclick = null;

                        node.addEventListener("click", function()
                        {
                            // remove onclick whenever the share button is clicked
                            // can't use node because it will not persists
                            document.querySelector('[aria-label="Share"]').onclick = null;

                            window.androidWebViewClient.shareClicked(window.location.href);
                        });
                    }
                    else
                    {
                        node.addEventListener("click", function(e)
                        {
                            // remove share onclick event any time a different c3-material-button-button is clicked
                            document.querySelector('[aria-label="Share"]').onclick = null;
                        });
                    }
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { childList: true, subtree: true };

    shareButtonMutationObserver.observe(container, config);
}

// initVideoEndedMutationObserver
function initVideoEndedMutationObserver()
{
    var videoEndedMutationObserver = new MutationObserver(function(mutations)
    {
        for (var mutation of mutations)
        {
            if (mutation.target.classList.contains('html5-endscreen') && mutation.attributeName == "style")
            {
                // need to determine if the end screen is showing
                if(!mutation.target.style.display.includes('none'))
                {
                    // Need to do some quick visual adjustment that bugs me.  Endscreen buttons don't look vertically centered
                    if (document.querySelector('[aria-label="Previous Video"]') != null)
                    {
                        document.querySelector('[aria-label="Previous Video"]').style.top = "35%";
                    }

                    if (document.querySelector('[aria-label="Replay Video"]') != null)
                    {
                        document.querySelector('[aria-label="Replay Video"]').style.top = "35%";
                    }

                    if (document.querySelector('[aria-label="Next Video"]') != null)
                    {
                        document.querySelector('[aria-label="Next Video"]').style.top = "35%";
                    }
                    // end visual adjustment

                    let isAutoPlayEnabled = document.querySelector('[aria-label="Autoplay"]').getAttribute("aria-pressed");

                    if (isAutoPlayEnabled.includes("false"))
                    {
                        exitFullScreen(true);

                        // if the video is a replay, make sure the endscreen contents are shown
                        document.querySelector('.ytp-mweb-endscreen-contents').style.display = "block";
                    }
                    else
                    {
                        // Need to ensure endscreen contents are not shown. This covers for a youtube bug where if we replayed a video
                        // and autoplay is on, when the video ends both autoplay and endscreen will be visible. Yikes
                        if (document.querySelector('.ytp-mweb-endscreen-contents') != null)
                        {
                            document.querySelector('.ytp-mweb-endscreen-contents').style.display = "none";
                        }

                        // add event listener to "cancel autoplay" button to exit fullscreen and display endscreen
                        // need null check so the event is only added once
                        document.querySelector('[aria-label="Cancel autoplay"]').addEventListener("click", function(e)
                        {
                            exitFullScreen(true);

                            document.querySelector('.ytp-mweb-endscreen-contents').style.display = "block";
                        });
                    }
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { attributes: true, childList: true, subtree: true };

    videoEndedMutationObserver.observe(container, config);
}

function initTapHighlightColor()
{
    document.documentElement.style.webkitTapHighlightColor = "#00000000";
}

//////////////////////////////////////////////////////////////////////////
// public functions
// must return 'success';
//////////////////////////////////////////////////////////////////////////

function enterFullScreen(forceOrientationChange)
{
    document.body.setAttribute("faux-fullscreen", true);

    window.androidWebViewClient.onEnterFullScreen(forceOrientationChange);

    return 'successfully called enterFullScreen()';
}

function exitFullScreen(forceOrientationChange)
{
    document.body.removeAttribute("faux-fullscreen");

    window.androidWebViewClient.onExitFullScreen(forceOrientationChange);

    return 'successfully called exitFullScreen()';
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