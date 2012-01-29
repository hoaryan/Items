<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN">
<html>
    <head>
        <title><g:layoutTitle default="Items Project 2009 Prototyp" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'grailscss.css')}" />
        <link rel="stylesheet" href="${resource(dir:'css',file:'items.css')}" />
        <!--<link rel="stylesheet" href="includes/modalbox.css" type="text/css" media="screen" />-->
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />

        <!-- IPHONE META STUFF START -->
        <meta name="viewport" content="width=device-width; initial-scale=1.0; maximum-scale=1.0; user-scalable=0;"/>
        <meta name="apple-touch-fullscreen" content="YES" />

        <!-- Wegscrollen von Browserleiste im iphone  -->
        <!--
        <script type="application/x-javascript">
              window.addEventListener('load', function(){
                    setTimeout(scrollTo, 0, 0, 1);
                 }, false);
        </script>
                 -->
        <!-- IPHONE META STUFF END -->

        <g:javascript library="application" />
        <g:javascript library="scriptaculous" />
        <modalbox:modalIncludes />
        <script type="text/javascript" src="includes/prototype.js"></script>
        
        <g:javascript src="items.js" />
        <g:javascript src="iscroll-min.js?v3.7.1" />

        <g:layoutHead />
                
    </head>

    <body style="background-color: #222">
      <div style="background-color: #FFF">
        <g:layoutBody />
      </div>
    </body>
</html>

