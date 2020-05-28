
// pseudo constructor
(function ()
{
    init();

    return 'successfully loaded javascript.js';
})();

function init()
{
    // The point of the app
    initAdsMutationObserver();

    // Menu/Account
    initHeaderMutationObserver();

    initSkipAccountScreenMutationObserver();

    initUnwantedSignInElementsMutationObserver();

    // Watch screen specific
    initFullScreenMutationObserver();

    initShareButtonMutationObserver();

    initVideoEndedMutationObserver();

    initSettingsPopupMutationObserver();

    // Hide blue click
    initTapHighlightColor();
}

// detect video ad class then remove the vrc src
// detect ad elements and remove them
function initAdsMutationObserver()
{
    var adsMutationObserver = new MutationObserver(function(mutations)
    {
        if (_currentScreen != "accounts")
        {
            for (var mutation of mutations)
            {
                if(mutation.attributeName == "class")
                {
                    if (mutation.target.classList.contains('ad-showing'))
                    {
                        removeVideoAdSrc();
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
        }
    });

    var container = document.documentElement;
    var config = { attributes: true, attributeFilter: ['class'], childList: true, subtree: true };

    adsMutationObserver.observe(container, config);
}

// remove the three dot settings button in the header
// if the user is not logged in, change the profile icon onclick to navigate directly to accounts.google.com
function initHeaderMutationObserver()
{
    var headerMutationObserver = new MutationObserver(function(mutations)
    {
        if (_currentScreen == "")
        {
            for (var mutation of mutations)
            {
                if (mutation.type  == "childList")
                {
                    for (var node of mutation.addedNodes)
                    {
                        if (node.nodeName == 'YTM-MENU' && node.parentNode.classList.contains("mobile-topbar-header-content"))
                        {
                            node.style.display = "none";
                        }

                        if (node.nodeName == "YTM-TOPBAR-MENU-BUTTON-RENDERER")
                        {
                            attachAccountButtonOnClickEvent();
                        }

                        // this is for the big blue sign in button when watching a video
                        if (node.classList != null && node.classList.contains("mobile-topbar-header-sign-in-button"))
                        {
                            // make signInButton take you directly to google login
                            var signInButton = document.querySelector(".mobile-topbar-header-sign-in-button");

                            signInButton.onclick = function ()
                            {
                                document.documentElement.innerHTML = "Please Wait..."

                                window.location.href = 'https://accounts.google.com';
                            };
                        }
                    }
                }
                else if (mutation.attributeName == "class")
                {
                    // when the sticky header hides/shows, it re-attaches its old onclick. So, we need to re-attach ours
                    if (mutation.target.classList.contains('sticky-player') && mutation.target.classList.contains('in'))
                    {
                        attachAccountButtonOnClickEvent();
                    }
                }
            }
        }
    });

    // container always needs to be document.documentElement because it will never be null
    var container = document.documentElement;
    var config = { attributes: true, attributeFilter: ['class'], childList: true, subtree: true };

    headerMutationObserver.observe(container, config);
}

// if the user is signed in, skip the Account screen and go directly to the Accounts screen. (difference is the 's')
function initSkipAccountScreenMutationObserver()
{
    var skipAccountScreenMutationObserver = new MutationObserver(function(mutations)
    {
        if (_currentScreen == "menu")
        {
            for (var mutation of mutations)
            {
                // detect when account screen is showing then hides and skips it
                for (var node of mutation.addedNodes)
                {
                    if (node.id == 'simple-menu-header-title')
                    {
                        document.querySelector('.active-account-name').click();

                        document.querySelector("#menu").style.display = "none";
                    }
                }

                // detect when accounts screen is showing then shows it
                if (mutation.type === 'characterData')
                {
                    if (mutation.target.textContent.includes("Add account"))
                    {
                        document.querySelector("#menu").style.display = "block";

                        // make add account button take you directly to google login. Added AddSession to the url intentionally
                        var addAccountButton = mutation.target.parentNode.parentNode.parentNode;

                        addAccountButton.onclick = function ()
                        {
                            document.documentElement.innerHTML = "Please Wait..."

                            window.location.href = 'https://accounts.google.com/AddSession';
                        };

                        // This is needed to fix a m.youtube bug. User could not scroll after pressing the 'x' button
                        // in the accounts screen. This was caused because the 'modal-open-body' attribute is not being removed
                        // from the body element. So we need to do it manually
                        document.querySelector("#simple-menu-header-close-button").addEventListener("click", function(e)
                        {
                            document.body.removeAttribute("modal-open-body");
                        });
                    }
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { characterData: true, childList: true, subtree: true };

    skipAccountScreenMutationObserver.observe(container, config);
}

// removes all unwanted sign in elements
function initUnwantedSignInElementsMutationObserver()
{
    var unwantedSignInElementsMutationObserver = new MutationObserver(function(mutations)
    {
        if (_currentScreen == "accounts")
        {
            for (var mutation of mutations)
            {
                for (var node of mutation.addedNodes)
                {
                    // V2 login flow
                    if (node.textContent.includes('Forgot') ||
                        node.textContent.includes('Learn')  ||
                        node.textContent.includes('Create') ||
                        node.textContent.includes('Remove an account'))
                    {
                        removeUnwantedSignInElements();
                    }

                    // Old Login flow
                    if (node.textContent.includes("One Google Account") ||
                        node.textContent.includes("Add account"))
                    {
                        removeOldUnwantedSignInElements();
                    }
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { childList: true, subtree: true };

    unwantedSignInElementsMutationObserver.observe(container, config);
}

// I need to have all this nonsense so that entering/exiting fullscreen is consistent.
// This is done by removing all of youtube fullscreen click event listeners and manually doing everything
function initFullScreenMutationObserver()
{
    var fullScreenMutationObserver = new MutationObserver(function(mutations)
    {
        if (_currentScreen == "watch")
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
                                ExitFullScreen(true);
                            }
                            else
                            {
                                EnterFullScreen(true);
                            }
                        });
                    }
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { childList: true, subtree: true };

    fullScreenMutationObserver.observe(container, config);
}

// override youtube's share button functionality with Android style sharing
function initShareButtonMutationObserver()
{
    // The Share buttons onclick event listener gets re-initialized any time a c3-material-button-button is clicked.
    // We need to use addEventListener to not override the other buttons onclick eventlisteners.
    var shareButtonMutationObserver = new MutationObserver(function(mutations)
    {
        if (_currentScreen == "watch")
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
        }
    });

    var container = document.documentElement;
    var config = { childList: true, subtree: true };

    shareButtonMutationObserver.observe(container, config);
}

// handle when an video ends
function initVideoEndedMutationObserver()
{
    var videoEndedMutationObserver = new MutationObserver(function(mutations)
    {
        if (_currentScreen == "watch")
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
                            ExitFullScreen(true);

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
                                ExitFullScreen(true);

                                document.querySelector('.ytp-mweb-endscreen-contents').style.display = "block";
                            });
                        }
                    }
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { attributes: true, childList: true, subtree: true };

    videoEndedMutationObserver.observe(container, config);
}

function initSettingsPopupMutationObserver()
{
    var settingsPopupMutationObserver = new MutationObserver(function(mutations)
    {
        if (_currentScreen == "watch")
        {
            for (var mutation of mutations)
            {
                for (var node of mutation.addedNodes)
                {
                    if (node.nodeName == "YTM-MENU-ITEM")
                    {
                        if (node.textContent.includes('Copy Debug Info') || node.textContent.includes('Stats For Nerds'))
                        {
                            node.style.display = "none";
                        }
                    }
                }
            }
        }
    });

    var container = document.documentElement;
    var config = { childList: true, subtree: true };

    settingsPopupMutationObserver.observe(container, config);
}

function initTapHighlightColor()
{
    document.documentElement.style.webkitTapHighlightColor = "#00000000";
}

//////////////////////////////////////////////////////////////////////////
// public functions
// must return 'success';
//////////////////////////////////////////////////////////////////////////

var _currentScreen = "";

function SetCurrentScreen(currentScreen)
{
    _currentScreen = currentScreen;

    return 'successfully called SetCurrentScreen()';
}

function EnterFullScreen(forceLandscape)
{
    document.body.setAttribute("faux-fullscreen", true);

    window.androidWebViewClient.onEnterFullScreen(forceLandscape);

    return 'successfully called EnterFullScreen()';
}

function ExitFullScreen(forcePortrait)
{
    document.body.removeAttribute("faux-fullscreen");

    window.androidWebViewClient.onExitFullScreen(forcePortrait);

    return 'successfully called ExitFullScreen()';
}

//////////////////////////////////////////////////////////////////////////
// private functions
//////////////////////////////////////////////////////////////////////////

function removeVideoAdSrc()
{
    // this is probably not needed
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

function attachAccountButtonOnClickEvent()
{
    let accountButton = document.querySelector("[aria-label='Account']");

    let topBar = document.querySelector("ytm-topbar-menu-button-renderer");

    if (topBar != null)
    {
         let isLoggedIn = topBar.querySelector("ytm-profile-icon") != null;

         if(isLoggedIn)
         {
             // Need to set _currentScreen = "menu", then manually call the accountButton's onclick.
             // This is because the screen will appear before SetCurrentScreen can be called and the
             let accountButtonOnClick = accountButton.onclick;

             accountButton.onclick = function ()
             {
                 _currentScreen = "menu";

                 accountButtonOnClick.call();
             };
         }
         else
         {
             // make account button take you directly to google login
             accountButton.onclick = function ()
             {
                 document.documentElement.innerHTML = "Please Wait..."

                 window.location.href = 'https://accounts.google.com';
             };
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

    var footer = document.querySelector("footer");

    if (footer != null)
    {
        footer.style.display = "none";
    }
}

function removeOldUnwantedSignInElements()
{
    var links = document.querySelectorAll('a');

    for (var link of links)
    {
        if (link.innerText.includes("Sign in with different account"))
        {
            link.parentNode.parentNode.style.display = "none";
        }

        if (link.innerText.includes("Find my account"))
        {
            link.style.display = "none";
        }
    }

    var inputs = document.querySelectorAll('input');

    for (var input of inputs)
    {
        if (input.value.includes("Forgot password?"))
        {
            input.style.display = "none";
        }
    }

    var oneGoogle = document.querySelector(".one-google");

    if (oneGoogle != null)
    {
        oneGoogle.parentNode.style.display = "none";
    }

    var signInWithDifferentAccount = document.querySelector("#account-chooser-link");

    if (signInWithDifferentAccount != null)
    {
        signInWithDifferentAccount.parentNode.parentNode.style.display = "none";
    }

    var footer = document.querySelector(".google-footer-bar");

    if (footer != null)
    {
        footer.style.display = "none";
    }

    var footerList = document.querySelector("#footer-list");

    if (footerList != null)
    {
        footerList.parentNode.parentNode.style.display = "none";
    }
}