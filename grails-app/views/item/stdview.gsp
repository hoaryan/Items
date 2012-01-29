<html>
  <head>
    <meta name="layout" content="main" />  
    <!--<link type="text/css" href="css/ui-lightness/jquery-ui-1.8.17.custom.css" rel="Stylesheet" />-->
    <!--<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>-->
    <!--<script type="text/javascript" src="js/jquery-ui-1.8.17.custom.min.js"></script>-->
    
    <link rel="stylesheet" href="${resource(dir:'css/ui-lightness',file:'jquery-ui-1.8.17.custom.css')}" />
    <g:javascript src="jquery-1.7.1.min.js" />
    <g:javascript src="jquery-ui-1.8.17.custom.min.js" />
    
    <g:javascript>
      <g:if test="${itemInstance}">
        window.releaseEvents(Event.KEYPRESS);
        window.onkeydown = null;
                  
      </g:if>
      <g:else><!--Java-Script damit focus bei tastendruck auf dem suchfeld liegt-->
        window.captureEvents(Event.KEYPRESS);
        window.onkeydown = focusSearchFieldonKeyPressed;
      </g:else>
      
        var myScroll;
        var a = 0;
        function loaded() {
                setHeight();	// Set the wrapper height. Not strictly needed, see setHeight() function below.

                // Please note that the following is the only line needed by iScroll to work. Everything else here is to make this demo fancier.
                myScroll = new iScroll('scroller', {desktopCompatibility:true});
        }

        // Change wrapper height based on device orientation. Not strictly needed by iScroll, you may also use pure CSS techniques.
        function setHeight() {
                var headerH = document.getElementById('header').offsetHeight,
                        footerH = document.getElementById('footer').offsetHeight,
                        wrapperH = window.innerHeight - headerH - footerH;
                document.getElementById('wrapper').style.height = wrapperH + 'px';
        }

        // Check screen size on orientation change
        window.addEventListener('onorientationchange' in window ? 'orientationchange' : 'resize', setHeight, false);

        // Prevent the whole screen to scroll when dragging elements outside of the scroller (ie:header/footer).
        // If you want to use iScroll in a portion of the screen and still be able to use the native scrolling, do *not* preventDefault on touchmove.
        document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

        // Load iScroll when DOM content is ready.
        document.addEventListener('DOMContentLoaded', loaded, false);
                         

        $(document).ready(function() {
            $.datepicker.regional['de'] = {
		closeText: 'schließen',
		prevText: '&#x3c;zurück',
		nextText: 'Vor&#x3e;',
		currentText: 'heute',
		monthNames: ['Januar','Februar','März','April','Mai','Juni',
		'Juli','August','September','Oktober','November','Dezember'],
		monthNamesShort: ['Jan','Feb','Mär','Apr','Mai','Jun',
		'Jul','Aug','Sep','Okt','Nov','Dez'],
		dayNames: ['Sonntag','Montag','Dienstag','Mittwoch','Donnerstag','Freitag','Samstag'],
		dayNamesShort: ['So','Mo','Di','Mi','Do','Fr','Sa'],
		dayNamesMin: ['So','Mo','Di','Mi','Do','Fr','Sa'],
		weekHeader: 'Wo',
		dateFormat: 'dd.mm.yy',
		firstDay: 1,
		isRTL: false,
		showMonthAfterYear: false,
		yearSuffix: ''};
          $.datepicker.setDefaults($.datepicker.regional['de']);
          $('#datepicker').datepicker();
          
        });
        
    </g:javascript>        
  </head>

  <body>
    
      <div id="header">
          <div id="toptoolbar">
            <div class="left" style="width: 90%"> 
              
                <g:link class="itemnametop" action="stdview" id="${toBeViewedItem.id}">${toBeViewedItem.name}</g:link>
                
                <!--
                <g:if test="${toBeViewedItem?.children.size() != 0}">
                  <div class="childcountTop">
                    ${toBeViewedItem?.children.size()}
                  </div>
                </g:if>
                -->

                <div style="margin-top: 0px; margin-left: 6px; margin-bottom: 4px">
                    <g:each in="${toBeViewedItem?.tags}" var="tag">
                        <g:link class="taglinktop" action="stdview" id="${tag.id}" target="_self">${tag.name},</g:link>
                    </g:each>
                </div>
                
             </div>
              <div class="right">
                <a href="#" title="Edit Item.." onclick="Modalbox.show($('itemEditDialog'), {title: this.title, width: 250, slideDownDuration: 0.10, slideUpDuration: 0.10, overlayDuration: 0.2, resizeDuration: 0.01}); return false" class="editItemButton" ></a>                                 
              </div>
          </div>
        
          <div id="additionaltoolarea" style="display:none">
            <div>
              <div class="left">
                   <g:render template="search"/>
              </div>
              <div class="right">
                <g:if test="${toBeViewedItem.itemType==Item.ITEM_NORMAL}"><a href="#" class="addButton" onClick="javascript:openCreate()" ></a>
                    <g:link class="tagButton" action="addTags" id="${toBeViewedItem.id}"></g:link>
                  <g:form style="margin-left: -3px">                 
                      <items:remoteAddDateBtn />
                      <g:hiddenField  style="display:none" name="isNew" value="true"/>
                      <g:hiddenField style="display:none" name="itemId" value="${toBeViewedItem.id}" />
                  </g:form>
                  
                  <g:link class="editButton" action="edit" id="${toBeViewedItem.id}"></g:link>

                </g:if>
                <g:elseif test="${toBeViewedItem.itemType==Item.ITEM_INBOX}">
                  <a href="#" class="addButton" style="margin-right: 3px" onClick="javascript:openCreate()"></a>
                </g:elseif>
              </div>
              <div style="clear:left"> </div>
            </div>
          </div></div>
        </div><!-- div header --> 
        

        
<!-- Listarea ------------------------------------------------------------------------------------------->
    
        <div id="wrapper">             
          <div id="scroller">  
            
          <div id="editDateArea">
              <g:if test="${isNew!=null && (isNew==true || (isNew==false && itemDate!=null) )}">
                <g:render template="../itemDate/formEditSingleDate" model="['isNew':isNew,'itemId':toBeViewedItem.id, 'itemDate':itemDate]"/>
              </g:if>
          </div>

          <g:render template="../common/message" style="display:none" />          

          <g:if test="${itemInstance}">
            <div id="createArea" style="display:inline">
          </g:if>
          <g:else>
            <div id="createArea" style="display:none">
          </g:else>
            <g:render template="../item/create" model="['parentId':toBeViewedItem.id]"/>
          </div>
            
          <g:if test="${toBeViewedItem?.notes}">
              <div id="notesarea">
                    ${StringHelper.FormatNotes(toBeViewedItem.notes)}    
              </div>
          </g:if>  
    
          
            <div id="dateListArea">
              <g:each in="${toBeViewedItem?.itemDates}" var="date">

                <div class="datechild">
                   <div class="left">

                      <g:link class="itemname" action="calendarlist" controller="itemDate" id="${date.id}">${date}</g:link>

                  </div>
                  <div class="right" style="margin-top: 3px">
                    <g:form controller="itemDate" action="showDateEditDialog" name="editDateButtonForm">
                    <g:hiddenField name="isNew" value="false" />
                    <g:hiddenField name="dateId" value="${date.id}" />
                    <g:hiddenField name="itemId" value="${toBeViewedItem.id}" />

                      
                      <items:remoteEditDateBtn />
                    
                      <a href="#" title="Please Confirm..." onclick="
                        Modalbox.show($('deleteDateDialog_${date.id}'), {title: this.title, width: 250}); return false;
                        " class="delDateButton">
                      </a>      
                    
                    

                    </g:form>
                  </div>

                  <div class="clearfloat"></div>
                </div>

                <div id="deleteDateDialog_${date.id}" style="display:none">
                  <span class="dialogfont" style="padding: 5px">Really want to delete Date?</span>
                    <br>
                    <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
                      <tr>
                        <td style="width: 1%"><g:link class="delDateButton" controller="itemDate" action="deleteDate" params="[id:date.id, itemId:toBeViewedItem.id]"></g:link></td><td  style="vertical-align: middle"><g:link controller="itemDate" action="deleteDate" params="[id:date.id, itemId:toBeViewedItem.id]">Yes</g:link></td>
                      </tr>
                      <tr>
                        <td style="width: 1%"><a href="#" class="cancelButton" onClick="javascript:Modalbox.hide()"></a></td><td  style="vertical-align: middle"><a href="#" onClick="javascript:Modalbox.hide()" class="dialogfont">Cancel</a></td>
                      </tr>
                      <tr>
                        <td style="width: 1%"><items:remoteEditDateBtn /></td><td  style="vertical-align: middle"><a href="#" class="dialogfont">Change Date</a></td>
                      </tr>
                    </table>                   
                </div>
              </g:each>
            </div>
           
            <div id="listarea">
                <g:if test="${toBeViewedItem.itemType==Item.ITEM_LOGBOOK}">
                  <g:render template="../item/childItemLogbook" var="child" collection="${deletedItems}" />
                  <g:if test="${deletedItems?.size()==0}">
                    <div class="listinfo">
                        No items to display in this list...
                    </div>
                  </g:if>
                </g:if>
                <g:else>
                  <g:if test="${toBeViewedItem?.children}">
                    <g:render template="../item/childItem" var="child" collection="${toBeViewedItem?.children.sort{ a,b -> b.children.size() <=> a.children.size() }}" />
                  </g:if>
                  <!--<g:if test="${toBeViewedItem?.children?.size()==0}">
                    <div class="listinfo">
                        No items to display in this list...
                    </div>
                  </g:if>-->
                </g:else>            
            </div> <!-- div listarea -->    
            
            <div id="restoflist">

            </div>

          </div> <!-- div scroller -->
        </div> <!-- div wrapper -->
            
<!-- Footer ------------------------------------------------------------------------------------------->

      <div id="footer">
        <div id="bottomtoolbar">
          <div class="left">
            <a href="#" title="Edit List.." onclick="Modalbox.show($('listEditDialog'), {title: this.title, width: 250, slideDownDuration: 0.10, slideUpDuration: 0.10, overlayDuration: 0.2, resizeDuration: 0.01}); return false" class="editListButton">
            </a>  
          </div>
          <div class="right">         
            <a href="#" class="addButton" onClick="javascript:openCreate()" ></a>      
          </div>                
        </div>     
        
        <div id="stagetoolbar">
          <div class="left">
              <a href="javascript:history.back()" class="stageMenuButton_back"></a>     
            </a>  
          </div>
          <div class="right">         
            <g:link class="stageMenuButton_home" controller="item" action="homeview"></g:link>
            <a href="#" class="stageMenuButton_search"></a>  
            <a href="#" class="stageMenuButton_calendar"></a>   
            <a href="#" class="stageMenuButton_user"></a>
          </div>                
        </div>
      </div>

<!-- ModalBoxes ------------------------------------------------------------------------------------------->
             
      <div id="itemEditDialog" style="display:none">
        <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
           <tr>
             <td width="26px"><g:link class="tagButton" action="addTags" id="${toBeViewedItem.id}"></g:link></td>
             <td style="vertical-align: middle">Add / Edit Tags</td>  
           </tr>
        </table> 
        <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
           <tr>
             <td width="26px">
              
               <g:form>
                  <items:remoteAddDateBtn />
                  <g:hiddenField  style="display:none" name="isNew" value="true" />
                  <g:hiddenField style="display:none" name="itemId" value="${toBeViewedItem.id}" />
               </g:form>
             </td>
             <td style="vertical-align: middle">Add/Edit Date(s)</td>  
           </tr>
        </table>
        <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
           <tr>
             <td width="26px"><g:link class="editButton" action="edit" id="${toBeViewedItem.id}"></g:link></td>
             <td style="vertical-align: middle">Add/Edit Title and Note</td>  
           </tr>
        </table> 
      </div>
        
      <div id="listEditDialog" style="display:none">
        <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
           <tr>
             <td width="26px"><a href="#" class="addButton" onClick="javascript:openCreate()" ></a></td>
             <td style="vertical-align: middle">Add Item to this List</td>  
           </tr>
        </table> 

        <g:if test="${toBeViewedItem?.children?.findAll{ item -> item.getIsDeletable() || item.getIsUnchainable()}}">
          <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
           <tr>
             <td width="26px">
               <a href="#" title="Please Confirm..." onclick="Modalbox.show($('deleteAllDialog'), {title: this.title, width: 250, slideDownDuration: 0.10, slideUpDuration: 0.10, overlayDuration: 0.2, resizeDuration: 0.01}); return false;" class="delAllButton"></a> 
             </td>   
             <td style="vertical-align: middle">
               Clean List
             </td>  
           </tr>
          </table>            
        </g:if>
        <g:else>
          <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
           <tr>
             <td width="26px">
               <a href="#" class="delAllButton"></a> 
             </td>   
             <td style="vertical-align: middle">
               no items marked to clean
             </td>  
           </tr>
          </table>       
        </g:else>    
        <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
           <tr>
              <td style="width: 26px"><a href="#" class="cancelButton" onClick="javascript:window.location.reload()"></a></td><td  style="vertical-align: middle"><a href="#" onClick="javascript:Modalbox.hide()" class="dialogfont">Cancel</a></td>
           </tr>
        </table>
      </div>

      <div id="deleteAllDialog" style="display:none">

        <g:if test="${toBeViewedItem?.getHasChildrenByUnchainableANDDeletable(false, true) &&
                      !toBeViewedItem?.getHasChildrenByUnchainableANDDeletable(true,false) &&
                      !toBeViewedItem?.getHasChildrenByUnchainableANDDeletable(true,true)}"><!-- alle deletable -->

                      <g:if test="${toBeViewedItem?.getHasChildrenByUnchainableANDDeletable(false,false)}">
                        <span class="dialogfont" style="padding: 5px">Send all deletable Items from this list to logbook?</span>
                      </g:if>
                      <g:else>
                        <span class="dialogfont" style="padding: 5px">Send all Items from this List to Logbook?</span>
                      </g:else>

                      <br>
                      <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
                        <tr>
                          <td style="width: 1%"><g:link class="delButtonActive" action="handleDeleteList" id="${toBeViewedItem.id}" params="[deleteFrom:Item.REMOVE_FROM_ALL_LISTS]"></g:link></td><td  style="vertical-align: middle"><g:link class="dialogfont" action="handleDeleteList" id="${toBeViewedItem.id}" params="[deleteFrom:Item.REMOVE_FROM_ALL_LISTS]">Yes</g:link></td>
                        </tr>
                        <tr>
                          <td style="width: 1%"><a href="#" class="cancelButton" onClick="javascript:window.location.reload()"></a></td><td  style="vertical-align: middle"><a href="#" onClick="javascript:Modalbox.hide()" class="dialogfont">Cancel</a></td>
                        </tr>
                      </table>
        </g:if>
        <g:elseif test="${!toBeViewedItem?.getHasChildrenByUnchainableANDDeletable(false, true) &&
                      toBeViewedItem?.getHasChildrenByUnchainableANDDeletable(true,false) &&
                      !toBeViewedItem?.getHasChildrenByUnchainableANDDeletable(true,true)}"><!-- alle unchainable -->
                      
                      <g:if test="${toBeViewedItem?.getHasChildrenByUnchainableANDDeletable(false,false)}">
                        <span class="dialogfont" style="padding: 5px">These Items cant be deleted completely because they contain childitems, but (some) can be removed from this list!</span>
                      </g:if>
                      <br>
                      <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
                        <tr>
                          <td style="width: 1%"><g:link class="unchainButtonActive" action="handleDeleteList" id="${toBeViewedItem.id}" params="[deleteFrom:Item.REMOVE_FROM_THIS_LIST]"></g:link></td><td style="vertical-align: middle"><g:link class="dialogfont" action="handleDeleteList" id="${toBeViewedItem.id}" params="[deleteFrom:Item.REMOVE_FROM_THIS_LIST]">Remove from list</g:link></td>
                        </tr>
                        <tr>
                          <td style="width: 1%"><a href="#" class="cancelButton" onClick="javascript:window.location.reload()"></a></td><td  style="vertical-align: middle"><a href="#" onClick="javascript:Modalbox.hide()" class="dialogfont">Cancel</a></td>
                        </tr>
                      </table>
        </g:elseif>
        <g:elseif test="${toBeViewedItem?.getHasChildrenByUnchainableANDDeletable(false,true) &&
                      toBeViewedItem?.getHasChildrenByUnchainableANDDeletable(true,false) &&
                      !toBeViewedItem?.getHasChildrenByUnchainableANDDeletable(true,true)}"><!-- jedes einzelne item entweder nur deletable oder nur unchainable -->
                      <span class="dialogfont" style="padding: 5px">
                        If possible items will be unchained from the list or send to logbook!
                      </span>
                      <br>
                      <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
                        <tr>
                          <td style="width: 1%"><g:link class="delButtonActive" action="handleDeleteList" id="${toBeViewedItem.id}" params="[deleteFrom:Item.REMOVE_FROM_ALL_LISTS]"></g:link></td><td style="vertical-align: middle"><g:link class="dialogfont" action="handleDeleteList" id="${toBeViewedItem.id}" params="[deleteFrom:Item.REMOVE_FROM_ALL_LISTS]">Yes</g:link></td>
                        </tr>
                        <tr>
                          <td style="width: 1%"><a href="#" class="cancelButton" onClick="javascript:window.location.reload(); return false"></a></td><td  style="vertical-align: middle"><a href="#" onClick="javascript:Modalbox.hide()" class="dialogfont">Cancel</a></td>
                        </tr>
                      </table>
        </g:elseif>
        <g:elseif test="${toBeViewedItem?.getHasChildrenByUnchainableANDDeletable(true,true)}">
                      <span class="dialogfont" style="padding: 5px">
                          Some of the items you want to remove are still contained by other lists. Delete those items from all lists and send them to logbook or just from this list?
                      </span>
                      <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
                      <tr>
                        <td style="width: 1%"><g:link class="unchainButtonActive" action="handleDeleteList" id="${toBeViewedItem.id}" params="[deleteFrom:Item.REMOVE_FROM_THIS_LIST]"></g:link></td><td style="vertical-align: middle"><g:link class="dialogfont" action="handleDeleteList" id="${toBeViewedItem.id}" params="[deleteFrom:Item.REMOVE_FROM_THIS_LIST]">this List</g:link></td>
                      </tr>
                      <tr>
                        <td style="width: 1%"><g:link class="delButtonActive" action="handleDeleteList" id="${toBeViewedItem.id}" params="[deleteFrom:Item.REMOVE_FROM_ALL_LISTS]"></g:link></td><td style="vertical-align: middle"><g:link class="dialogfont" action="handleDeleteList" id="${toBeViewedItem.id}" params="[deleteFrom:Item.REMOVE_FROM_ALL_LISTS]">all Lists</g:link></td>
                      </tr>
                      <tr>
                        <td style="width: 1%"><a href="#" class="cancelButton" onClick="javascript:window.location.reload(); return false"></a></td><td  style="vertical-align: middle"><a href="#" onClick="javascript:Modalbox.hide()" class="dialogfont">Cancel</a></td>
                      </tr>
                      </table>
        </g:elseif>
        <g:else>
          kein if :/
        </g:else>
      </div>

      <div id="deleteDialog_${toBeViewedItem?.id}" style="display:none">
        <div class="left">
          <span class="dialogfont" style="padding: 5px">Send item "${toBeViewedItem?.name}" to logbook?</span>
          <br>
          <table style="width: 100%; padding: 0px; margin: 0px; border-width: 0px">
            <tr>
              <td style="width: 1%"><g:link controller="item" action="handleMoveToLogbook" id="${toBeViewedItem?.id}" params="[toBeMovedItemId:toBeViewedItem?.id, toHome:'true']" class="delButtonActive"></g:link></td><td style="vertical-align: middle"><g:link controller="item" action="handleMoveToLogbook" id="${toBeViewedItem?.id}" params="[toBeMovedItemId:child?.id]" class="dialogfont">Yes</g:link></td>
            </tr>
            <tr>
              <td style="width: 1%"><a href="#" class="cancelButton" onClick="javascript:window.location.reload(); return false"></a></td><td style="vertical-align:middle; text-align: left"><a href="#" class="dialogfont" onClick="javascript:Modalbox.hide()">Cancel</a></td>
            </tr>
          </table>
        </div>
      </div>

  <g:javascript>
        <g:if test="${itemInstance}">
            $('createArea').style.display = "inline";
            document.createForm.name.focus();
        </g:if>
  </g:javascript>
  </body>
</html>
