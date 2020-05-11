// initMutationObserver
(function() 
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

                if (node.nodeName == 'YTM-PROMOTED-SPARKLES-TEXT-SEARCH-RENDERER')
                { 
                    console.log('removed SEARCH AD'); 
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
    var config = { childList: true, subtree: true  };

    observer.observe(container, config);

    return 'initialized';
})();

// old preroll add remove
(function ()
{
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

        if(timeoutCounter > 50)
        {
            console.log('Pre roll timer stopped');
            clearTimeout(timeout);
        }
    }, 100);

    return 'initialized';
})();

// video playing approach
(function ()
{
    document.querySelector('video').onplaying = function() 
    { 
      console.log('play');
    };
});

// clickFullScreen
(function()
{ 
    document.querySelector('.fullscreen-icon').click(); 
})();


// removeMenuButton
(function() 
{ 
    var elem = document.getElementsByTagName('ytm-menu'); 

    if (elem.length > 0)
    { 
        elem[0].style.display = 'none'; 
    } 
})();


// init listener for full screen video change
(function () 
{
    document.onfullscreenchange = function () 
    {   
        window.androidWebViewClient.fullScreenChanged(document.fullscreenElement == document.querySelector('#player-container-id'))
    };

    return 'initialized';
})();

// fullScreenClickListener
(function() 
{ 
    document.addEventListener('click', function (event) 
    {
        if (event.target.matches('.fullscreen-icon') || 
            event.target.parentNode.matches('.fullscreen-icon') || 
            event.target.parentNode.parentNode.matches('.fullscreen-icon') || 
            event.target.parentNode.parentNode.parentNode.matches('.fullscreen-icon')) 
        {
            window.androidWebViewClient.fullScreenClicked(document.fullscreenElement);
        }
    }, false);
})();

// pre roll add skip
(function()
{
  var timeout = setInterval(() => 
  {
      var ad = document.querySelectorAll('.ad-showing')[0];

      if (ad !== null && ad !== undefined) 
      {
          var video = document.querySelector('video');

          if (video !== null && video !== undefined) 
          {
              video.currentTime = video.duration; 
              console.log("skipped ad");
          }
      }
  }, 500);

  return function() 
  {
      clearTimeout(timeout);
  }
})();
