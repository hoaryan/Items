

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
    </head>
    <body>      
            <h1>Admin Panel</h1>
            <g:render template="../common/message" />
            
            <div class="left">
              <div>
                Hallo [${userInstance.firstName}]
              </div>
              <div class="menu_link">
                  <g:link controller="user" action="admin" >Alle User</g:link>
              </div>
              <div class="menu_link">
                  <g:link controller="user" action="register" >Neu Registrierung</g:link>
              </div>
            </div>

            <div class="left" id="mainarea">
               <g:render template="showAllUser" />
            </div>
    </body>
</html>
