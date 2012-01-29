

            <div class="dialog">
                <table>
                    <tbody>
                        <tr class="prop">
                            <td valign="top" class="name">Login/Email:</td>                        
                            <td valign="top" class="name">First Name:</td>                        
                            <td valign="top" class="name">Last Name:</td>                        
                            <td valign="top" class="name">Admin:</td>
                            <td valign="top" class="name"></td>
                            <td valign="top" class="name"></td>
                        </tr>                        

                        <g:if test="${allUser}">
                          <g:render template="show_user" var="user" collection="${allUser}" />
                        </g:if>
                        <g:else>
                          <tr class="prop">
                            <td valign="top" class="value" colspan="4">no existing user...</td>
                          </tr>
                        </g:else>

                    </tbody>
                </table>
            </div>
           