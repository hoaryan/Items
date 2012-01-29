<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN">
<html>
    <head>
        <title>Items Prototyp Index Seite</title>

        <g:javascript>
          function openMobileBrowserWindow (urlString) {
               fenster = window.open(urlString, "Mobile Browser Sized Window", "width=340,height=480,resizable=no,menubar=no,screenX=500,screenY=200,dependent=yes,scrollbars=yes");
               fenster.focus();
          }

          var browsername="mobile";
          var startURL="web/user/login";
          <!--alert("Ihr Browser" + navigator.userAgent);-->
          var agent = navigator.userAgent.toLowerCase();
          if (agent.indexOf(browsername.toLowerCase())>-1) {
            location.replace(startURL)
          }
          else {
             <!--openMobileBrowserWindow(startURL)-->
             <!--location.replace(startURL)-->
          }
        </g:javascript>
    
    </head>

    <body style="background-color: #EEE; padding: 0; margin: 0">
      <div style="height: 100px; bottom: 50%; background-color: #FFF; position: absolute; width: 100%">         
        <div style="float: right; font-family: Arial; font-size: 16px; font-weight: bold; display: inline-block; margin: 10px">
          <a href="#" style="text-decoration: none">Login:</a>
          <a style="text-decoration: none" href="javascript:openMobileBrowserWindow('web/user/login')">Mobile</a>,
          <a style="text-decoration: none" href="javascript:location.replace(startURL)">Desktop</a>
        </div>
        <div style="float: left">
          <a style="text-decoration: none" href="javascript:openMobileBrowserWindow('web/user/login')">
            <img src="images/itemitlogo.png" alt="Logo itemit"/>
          </a>
        </div>
      </div>
    </body>
    
</html>