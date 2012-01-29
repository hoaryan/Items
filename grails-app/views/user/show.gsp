

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
    </head>
    <body>      
            <h1>Show User</h1>
            <g:render template="../common/message" />
            <div class="dialog">
                <table>
                    <tbody>

                        <tr class="prop">
                            <td valign="top" class="name">Login/Email:</td>

                            <td valign="top" class="value">${fieldValue(bean:userInstance, field:'email')}</td>

                        </tr>


                        <tr class="prop">
                            <td valign="top" class="name">First Name:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:userInstance, field:'firstName')}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name">Last Name:</td>
                            
                            <td valign="top" class="value">${fieldValue(bean:userInstance, field:'lastName')}</td>
                            
                        </tr>
                    
                    
     
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form method="post">
                    <input type="hidden" name="id" value="${userInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" value="Edit" action="edit" /></span>
                    <a href="javascript:history.back()">CANCEL</a>
                </g:form>
            </div>
    </body>
</html>
