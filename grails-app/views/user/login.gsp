<html>
	<head>
		<meta name="layout" content="main" />

                <g:javascript src="login.js" />
                <g:javascript>

                    <g:if test="${GrailsUtil.environment=='development'}" >
                      window.captureEvents(Event.KEYPRESS);
                      window.onkeydown = debugShortcuts;
                    </g:if>
                </g:javascript>
	</head>
	<body>
           
            <g:render template="../common/message" />
            <div id="toptoolbar">
              <div class="itemname" style="font-size: 20px">item!t</div><br/>
              <div class="itemname" style="font-size: 13px">Testclient, Pre-Alpha</div>
            </div>

            <g:form controller="user" action="handleLogin">
              

              <div class="dialog">
                  <table>
                      <tbody>
                          <tr class='prop'>
                            <div style="padding:3px"
                            <span class="dialogfont" style="font-weight: normal">
                              Ankündigungen:<br/>
                              Check out our new Webpage <a href="http://www.itemit.com" target="_new">www.itemit.com</a>

                            </span>
                            <br/><br/>
                            <span class="dialogfont" style="font-weight: bold">Login-Screen:</span>
                            </div>
                          </tr>

                          <tr class='prop'>
                          <td valign='top' class='nameClear'>
                             <label for="email">Login (email):</label>
                          </td>
                          <td valign='top' class='valueClear ${hasErrors(bean:user,field:'email','errors')}'>
                             <input type="text" id="email" name="email" value="" />
                          </td>
                       </tr>
                       <tr class='prop'>
                          <td valign='top' class='nameClear'>
                             <label for="password">Password:</label>
                          </td>
                          <td valign='top' class='valueClear ${hasErrors(bean:user,field:'password','errors')}'>
                             <input type="password" id="password" name="password" value="${GrailsUtil.environment=='development'?'test':''}" />
                          </td>
                       </tr>
                      </tbody>
                   </table>
              </div>
             
              <div class="right" style="margin: 3px">
                <span><input class="button" type="submit" value="Login"></span>
              </div>
              <div class="left" style="margin-left:9px; margin-top: 3px">
                forgot your password?
              </div>

              <br><br><br>
            
            <!--<div class="left">
              Dann registrieren Sie sich doch einfach hier: <br/>
              <g:link controller="user" action="register" >Registrierung</g:link>
            </div>-->
                  <div id="bottomtoolbar" >                  
                  </div>

              </g:form>
          <!--    <div class="left">
              </div>
              <div class="right">
                
              </div>-->
            

     
	</body>
</html>
