
<g:form method="post" name="editDateForm" controller="itemDate" action="handleCreateOrEditDate">
<fieldset>

    <!--<g:hasErrors bean="${itemDate}">
       <div id="messagebox" style="display: none">
          <div class="errors">
            <g:renderErrors bean="${itemDate}" as="list" />
          </div>
       </div>
       <g:javascript>
            showMessage();
       </g:javascript>
    </g:hasErrors>-->

      <div id="dateEditArea">     
      <input type="hidden" name="__isNew" value="${isNew}" />
      <input type="hidden" name="__id" value="${itemDate!=null?itemDate.id:null}" />
      <input type="hidden" name="__version" value="${itemDate!=null?itemDate.version:null}" />
      <input type="hidden" name="__itemId" value="${itemId}" />
      
      <div class="groupseparator">
        ${isNew? 'Create Date':'Update Date'}
      </div>

      <div class="dialog">
        <div class="dialogfont" style="margin: 3px">
            Startdate/DueDate: <input type="text" name="datepicker" id="datepicker" />
        </div>
        <br>
        <div class="dialogfont" style="margin: 3px; font-weight: normal">
          Date:

              <br>
              <g:if test="${itemDate!=null}">
                <g:hasErrors bean="${itemDate}" field="startDate">
                  <g:eachError bean="${itemDate}" field="startDate">
                    <div class="errormessage" style="color:red;padding-left:3em"><g:message error="${it}" /></div>
                  </g:eachError>
                </g:hasErrors>
              </g:if>
            <div id="divStartTime">
              
              Time:
              <g:if test="${!isNew && itemDate!=null && itemDate?.startDate!=null}">
                  <items:timepicker id="starttime" name="starttime" defaultHour="${itemDate?.startDate?.getHours()}" defaultMinute="${itemDate?.startDate.getMinutes()}" disabled="${itemDate?.isGanztaegig? 'true':'false'}"/>
              </g:if>
              <g:else>
                  <items:timepicker id="starttime" name="starttime" defaultHour="0" defaultMinute="0" disabled="true" />
              </g:else>
              <g:if test="${!isNew && itemDate!=null}">
                <g:checkBox id="timeactive" name="timeactive"  onChange="javascript:toggleTimeactive()" value="${!itemDate?.isGanztaegig}" />
              </g:if>
              <g:else>
                <g:checkBox id="timeactive" name="timeactive"  onChange="javascript:toggleTimeactive()" value="${false}" />
              </g:else>
            </div>
        </div>

           <div id="divZeitspanne" style="margin: 3px; border-top: 1px solid #E0E0E0; border-bottom: 1px solid #E0E0E0; background-color: ${(itemDate!=null) && (itemDate.isPeriod())? '#FFF':'#CCC'}">

             <div class="dialogfont" style="margin: 3px">Enddate:</div>


             <g:if test="${!isNew && itemDate!=null}">
                <g:checkBox name="zeitspanne" onChange="javascript:toggleZeitspanne()" value="${itemDate?.isPeriod()}"/>
              </g:if>
              <g:else>                
                <g:checkBox name="zeitspanne" onChange="javascript:toggleZeitspanne()" value="${false}"/>
              </g:else>
             
              <br>
            
              <div class="dialogfont" style="margin: 3px; font-weight: normal">
              Date:
              
              <div id="divEndTime">
                Time:
                <g:if test="${!isNew && itemDate!=null && itemDate?.endDate!=null}">
                  <items:timepicker id="endtime" name="endtime" defaultHour="${itemDate?.endDate.getHours()}" defaultMinute="${itemDate?.endDate.getMinutes()}" disabled="${itemDate?.isGanztaegig? 'true':'false'}"/>
                </g:if>
                <g:else>
                  <items:timepicker id="endtime" name="endtime"  defaultHour="0" defaultMinute="0" disabled="true" />
                </g:else>
              </div>
              </div>
          </div>

          <div style="margin-left: 3px; display: block">
            Repetition:
            <g:select name='repeat_type' from='${TimeTaglibUtil.getRepeatTypeList()}' optionValue="value" optionKey="key" value="${isNew? ItemDate.TYPE_ONE_TIME : itemDate?.repeatType}"></g:select>
          </div>
      </div>
      <div style="height: 30px">
        <div id="" style="float: right; margin-right: 3px; display: block">
          <g:actionSubmit class="button" value="${isNew? 'Create':'Update'}" controller="itemDate" action="handleCreateOrEditDate"/>
          <g:link class="button" style="padding: 3px; border: 1px solid #E0E0E0" controller="item" action="stdview" id="${itemId}">Cancel</g:link>
        </div>
      </div>
    </div>

    </fieldset>
    </g:form>


    
      <g:javascript>
        document.editDateForm.startDate_value.focus();
      </g:javascript>
    