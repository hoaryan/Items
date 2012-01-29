

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
    </head>

    <body>

            <div id="toptoolbar"sssssss>
              <div class="left">
                <div class="itemname">Update User Data:</div>
              </div>
              <div class="right">
                <a href="javascript:history.back()" class="backButton" ></a>
                <g:link class="homeButton" controller="item" action="homeview"></g:link>
              </div>
            </div>

            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>

            <g:hasErrors bean="${userInstance}">
            <div class="errors">
                <g:renderErrors bean="${userInstance}" as="list" />
            </div>
              
            </g:hasErrors>
            <g:form action="handleEdit">
        <input type="hidden" name="id" value="${userInstance?.id}" />
                <input type="hidden" name="version" value="${userInstance?.version}" />
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
                                    <label for="oldPassword">Old password:</label>
                                </td>
                                <td valign="top" class="">
                                    <input type="password" id="oldPassword" name="oldPassword" value=""/>
                                </td>
                            </tr>


                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="password">New password:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:userInstance,field:'password','errors')}">
                                    <input type="password" id="password" name="password" value=""/>
                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="confirmPassword">Confirm new password:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean:userInstance,field:'confirmPassword','errors')}">
                                    <input type="password" name="confirmPassword" id="confirmPassword" value="" />
                                </td>
                            </tr>

                        </tbody>
                    </table>
                </div>


                <div class="right" style="margin-top: 3px, margin-right: 3px; margin-bottom: 5px">
                    <g:actionSubmit class="button" style="margin-top:5px" value="Update" action="handleEdit"/>
                    <g:actionSubmit class="button" onclick="return confirm('Are you sure?');" value="Delete" action="delete"/>
                    <a href="javascript:history.back()" class="button" style="border: 1px solid #CCC; padding: 3px; margin-right: 3px">Cancel</a>
                </div>

                 <div id="bottomtoolbar" >

                 </div>

            </g:form>
         
    </body>
</html>
