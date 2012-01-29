

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />
        <calendar:resources lang="en" theme="tiger"/>
    </head>
    <body>

    <g:form method="post" name="wrapperForm">
    <div id="toptoolbar">
        <div class="left">
          <div id="dialogfont" style="margin: 3px">Edit Item:</div>
          <div style="margin: 3px">

            <input type="text" style="color: #333"
                    <g:if test="${itemInstance.errors.hasFieldErrors('name')}">
                     class="inputedit_topbar_error"
                    </g:if>
                   <g:else>
                     class="inputedit_topbar" 
                   </g:else>
               id="name" name="name" value="${fieldValue(bean:itemInstance,field:'name')}"/>
            <br>
            <g:hasErrors bean="${itemInstance}" field="name">
              <g:eachError bean="${itemInstance}" field="name">
                <div class="errormessage" style="color:red"><g:message error="${it}" /></div>
              </g:eachError>
            </g:hasErrors>
          </div>
        </div>
        <div class="right">
            <a href="javascript:history.back()" class="backButton" ></a>
            <g:link class="homeButton" controller="item" action="homeview"></g:link>
        </div>

        <div class="clearfloat"></div>

    </div>

    <g:render template="../common/message" />
    
    <!--<g:hasErrors bean="${itemInstance}">
       <div id="messagebox" style="display: none">
          <div class="errors">
            <g:renderErrors bean="${itemInstance}" as="list" />
          </div>
       </div>
       
    </g:hasErrors>-->      

    <div id="listarea">
      <input type="hidden" name="id" value="${itemInstance?.id}" />
      <input type="hidden" name="version" value="${itemInstance?.version}" />
      <div class="groupseparator">
        Add Note:
      </div>
      <div valign="top">
          <textarea rows="5" cols="10" name="notes" style="width: 95%; margin: 3px; padding: 3px; resize:none">${fieldValue(bean:itemInstance, field:'notes')}</textarea>
          <br>
          <g:hasErrors bean="${itemInstance}" field="notes">
            <g:eachError bean="${itemInstance}" field="notes">
              <p style="color:red;"><g:message error="${it}" /></p>
            </g:eachError>
          </g:hasErrors>

          <div style="padding: 3px; padding-bottom: 6px; display: block; text-align: right">

            <g:actionSubmit class="button" value="Update" action="handleEdit"/>
            <g:link class="button" style="border: 1px solid #CCC; padding: 3px" controller="item" action="stdview" id="${itemInstance.id}">Cancel</g:link>


       <!--     <input class="button" type="submit" value="Create" style="padding: 3px"/>
            <input class="button" type="button" value="Cancel" style="padding: 3px" onClick="javascript:closeCreate()"/>
            style="border: 1px solid #CCC; padding: 3px"
            -->

          </div>

      </div>
    </div>

      
    <div id="bottomtoolbar">

    </div>
    </g:form>

    <g:javascript>
      document.wrapperForm.name.focus();
    </g:javascript>

    </body>
</html>
