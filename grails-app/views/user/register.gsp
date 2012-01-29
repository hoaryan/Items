
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
    </head>
    <body>       
     
            <div id="toptoolbar">
              <div class="itemname" style="font-size: 20px">Registrierung</div>
            </div>

            <g:render template="../common/message" />
            <g:hasErrors bean="${userInstance}">
            <div class="errors">
                <g:renderErrors bean="${userInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="handleRegistration" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="email">Login/Email:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:userInstance,field:'email','errors')}">
                                    <input type="text" id="email" name="email" value="${fieldValue(bean:userInstance,field:'email')}"/>
                                </td>
                            </tr>


                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="firstName">First Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:userInstance,field:'firstName','errors')}">
                                    <input type="text" id="firstName" name="firstName" value="${fieldValue(bean:userInstance,field:'firstName')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lastName">Last Name:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:userInstance,field:'lastName','errors')}">
                                    <input type="text" id="lastName" name="lastName" value="${fieldValue(bean:userInstance,field:'lastName')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="password">Password:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:userInstance,field:'password','errors')}">
                                    <input type="password" id="password" name="password" value="${fieldValue(bean:userInstance,field:'password')}"/>
                                </td>
                            </tr> 
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="confirmPassword">Confirm Password:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:userInstance,field:'confirmPassword','errors')}">
                                    <input type="password" name="confirmPassword" id="confirmPassword" value="${fieldValue(bean:userInstance,field:'confirmPassword')}" />
                                </td>
                            </tr> 
                            
                            <g:hiddenField name="captchaSolved" value="${userInstance?.captchaSolved}" />
                            <g:if test="${userInstance?.captchaSolved == false || userInstance == null}">

                            <tr class="prop">
                              <td></td>
                              <td>
                                 <jcaptcha:jpeg name="imageCaptcha" height="35px" width="140px" />
                              </td>
                            </tr>                          
                              <tr class="prop">
                                  <td valign="top" class="name">
                                      <label for="captchaChallenge">Captcha Challenge:</label>
                                  </td>
                                  <td valign="top">
                                      <input type="text" name="captchaResponse" id="captchaResponse"  />
                                  </td>
                              </tr>
                            </g:if>                           
                        </tbody>
                    </table>
                     
                </div>

              <div class="right" style="margin: 3px">
                    <input class="button" type="submit" value="Register" action="handleRegistration" />
                    <g:link class="button" style="border: 1px solid #CCC; padding: 3px" href="javascript:history.back()">Cancel</g:link>
              </div>
                    
            </g:form>

            <div id="bottomtoolbar" ></div>
    </body>
</html>
