<g:form method="post" name="createForm" action="handleCreate" controller="item" >
  <fieldset class="fieldnoborder"><!--<form method="post" id="nested-form-bug"></form>-->
  <label class="dialogfont" style="padding: 3px; padding-top: 6px">New Item:</label>
    <g:if test="${itemInstance?.errors?.hasFieldErrors('name')}">
      <div class="inputedit_createitem_error" >
        <input type="text" id="name" name="name" class="itemname" style="padding: 3px; padding-top: 6px" value="" />
      </div>
    </g:if>
    <g:else>
        <div class="inputedit_createitem">
           <input type="text" id="name" name="name" class="itemname" style="padding: 3px; padding-top: 6px" value="" />
        </div>
    </g:else>
                   
    <g:if test="${itemInstance}">
        <g:hasErrors bean="${itemInstance}" field="name">
            <g:eachError bean="${itemInstance}" field="name">
            <div id="errormessage" class="errormessage"><g:message error="${it}" /></div>
            </g:eachError>
        </g:hasErrors>
    </g:if>
    <input type="hidden" name="parentId" value="${parentId}" />
    <label class="dialogfont" for="notes" style="padding: 3px; padding-top: 3px">Notes:</label><br/>

    <textarea rows="4" id="notes" name="notes" style="margin-left: 6px; resize:none; padding-bottom: 3px"></textarea>

    <a href="#" id="toggleDateLink" onClick="javascript: toggleDate()">add Date</a>
    <input id="existDate" type="hidden" name="existDate" value="false" />
    <div id="dateArea" style="display: none">
      <g:render template="singleDateEdit" model="['denyInnerForm':true]"/>
    </div>

    <div style="padding: 3px; padding-bottom: 6px; display: block; text-align: right">     
      <input class="button" type="submit" value="Create" style="padding: 3px"/>
      <g:actionSubmit class="button" value="Create +Tags" action="handleCreateAndRedir" />
      <input class="button" type="button" value="Cancel" style="padding: 3px" onClick="javascript:closeCreate()"/>
    </div>
</fieldset>
</g:form>