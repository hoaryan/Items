

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />        
    </head>
    <body>
      
    
    <div id="toptoolbar">
        <div class="left">
          <div id="editheader" >Edit Date</div>
          <br/>
          <div id="itemname_edit" >
            ${fieldValue(bean:toBeViewedItem,field:'name')}
          </div>
        </div>
        <div class="right">
         
          <a href="javascript:history.back()" class="backButton" ></a>         
          
          <g:link class="homeButton" controller="item" action="homeview"></g:link>
          
        </div>
        <div class="clearfloat"></div>

    </div>
    <div id="additionaltoolarea">
            <div>
              <div class="left">

              </div>
              <div class="right">
                
              </div>
              <div class="right">
                  <img id="spinner_6D84A2" style="display:none; vertical-align:top; margin-top: 7px; margin-right: 20px"
                  src="<g:resource dir='/images' file='spinner_6D84A2.gif' />" />
                  <g:form>
                    
                    <g:submitToRemote
                        value="Add Date"
                        url="[action: 'showDateEditDialog', isNewCreateNew: 'true']"
                        update="editDateArea"
                        onLoading="showSpinner_2(true, '6D84A2')"
                        onLoaded="showSpinner_2(false, '6D84A2')"
                        onComplete="showSpinner_2(false, '6D84A2')"
                        onFailure="showSpinner_2(false, '6D84A2')"
                      />                    
                    <g:hiddenField  style="display:none" name="isNew" value="true" />
                    <g:hiddenField style="display:none" name="itemId" value="${toBeViewedItem.id}" />
                  </g:form>
              </div>

            </div>
    </div>


    <g:render template="../common/message" />

    <g:hasErrors bean="${toBeViewedItem}">
       <div id="messagebox" style="display: none">
          <div class="errors">
            <g:renderErrors bean="${toBeViewedItem}" as="list" />
          </div>
       </div>
       <g:javascript>
            showMessage();
       </g:javascript>
    </g:hasErrors>

    <!--<span>isNew!=null? ${isNew!=null}  isNew: ${isNew} /pitemDate!=null?: ${itemDate!=null}</span>-->
    <div id="listarea">      
      <div id="editDateArea">
        <g:if test="${isNew!=null && (isNew==true || (isNew==false && itemDate!=null) )}">
          <g:render template="../itemDate/formEditSingleDate" model="['isNew':isNew,'itemId':toBeViewedItem.id, 'itemDate':itemDate]"/>
        </g:if>
      </div>

      <div class="groupseparator">Dates:</div>
      <div id="dateListArea">
        <g:each in="${toBeViewedItem?.itemDates}" var="date">
          
          <div class="datechild">
             <div class="left">
              <div class="itemname" onClick="" style="vertical-align:middle; margin: 4px 4px">
                ${date}
              </div>
               
            </div>
            <div class="right">
              <g:form action="showDateEditDialog" name="editDateButtonForm">
              <g:hiddenField name="isNew" value="false" />
              <g:hiddenField name="dateId" value="${date.id}" />
              <g:hiddenField name="itemId" value="${toBeViewedItem.id}" />
              
              <g:submitToRemote
                value="Edit"
                url="[action: 'showDateEditDialog', isNewEdit: 'false']"
                update="editDateArea"
                onLoading="showSpinner_2(true, '6D84A2')"
                onLoaded="showSpinner_2(false, '6D84A2')"
                onComplete="showSpinner_2(false, '6D84A2')"
                onFailure="showSpinner_2(false, '6D84A2')"
                 />
              
                <span class="button"><g:link controller="itemDate" action="deleteDate" params="[id:date.id, itemId:toBeViewedItem.id]">Delete</g:link></span>
              
              </g:form>
            </div>

            <div class="clearfloat"></div>
          </div>          
        </g:each>
      </div>
    </div>

      <br />
    <div id="bottomtoolbar">
      <div class="left" style="vertical-align: middle">
        <span class="button"><g:link controller="item" action="stdview" id="${toBeViewedItem.id}">BACK</g:link></span>
      </div>
      <div class="right">
      </div>
    </div>
    
    <g:javascript>
      
    </g:javascript>

    </body>
</html>
