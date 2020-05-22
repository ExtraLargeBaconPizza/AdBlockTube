
// probably useful for finding the double click event
// getEventListeners(document); replace document with video player


// this is the sudo constructor
(function ()
{
    initAdsMutationObserver();

    initMenuButtonMutationObserver();

    initAccountMenuSkip();

    initSignInMutationObserver();

    initSignInMutationObserver();

    initShareButtonMutationObserver();

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

        window.androidWebViewClient.simulateTap(x, y);

        // Need to delay the second click so its not a double click
        setTimeout(function()
        {
            window.androidWebViewClient.simulateTap(x, y);

            playerContainer.style.display = "block";
        }, 600);
    }
    else
    {
        window.androidWebViewClient.simulateTap(x, y);
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