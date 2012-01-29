  
  <div class="childitem">
     
    <div class="right">
      <!--<span>unchainable: ${child?.isUnchainable}   deletable: ${child?.isDeletable}</span>-->

      <g:if test="${child.itemType==Item.ITEM_NORMAL}">
        <g:if test="${child?.isDeletable || child?.isUnchainable}">

          <div id="deleteDialog_${child?.id}" style="display:none">

            
                  <g:if test="${child?.isUnchainable && !child?.isDeletable}">
                    <span class="dialogfont" style="padding: 5px">Item "${child?.name}" contains childitems and can't be completely deleted, Item remains in list(s):</span>
                    <g:each in="${child?.tags}" var="tag">
                      <g:if test="${toBeViewedItem?.id != tag?.id}">
                        <span class="dialogfont" style="padding: 5px"><g:link  class="taglinklist" action="stdview" controller="item" id="${tag?.id}">${tag?.name}</g:link></span>
                      </g:if>
                    </g:each>
                    <br>
                    <span class="dialogfont" style="padding: 5px">Remove item "${child?.name}" from:</span>
                  </g:if>
                  <g:if test="${child?.isUnchainable && child?.isDeletable}">
                    <span class="dialogfont" style="padding: 5px">Item "${child?.name}" is also contained in list(s):</span>
                    <div style="padding-left: 3px">
                    <g:each in="${child?.tags}" var="tag">
                      <g:if test="${toBeViewedItem?.id != tag?.id}">
                        <span class="dialogfont" style="padding-left: 3px"><g:link  class="taglinklist" action="stdview" controller="item" id="${tag?.id}">${tag?.name}</g:link></span>
                      </g:if>
                    </g:each>
                    </div>
                    <span class="dialogfont" style="padding: 5px; padding-top: 10px">Delete item "${child?.name}" from:</span>
                  </g:if>


              <div class="left">


                    <g:if test="${!child?.isUnchainable && child?.isDeletable}">
                      <span class="dialogfont" style="padding: 5px">Send item to logbook?</span>
                      <br>
                       <table style="width: 100%; padding: 0px; margin: 0px; border-width: 0px">
                         <tr>
                           <td style="width: 1%"><g:link controller="item" action="handleMoveToLogbook" id="${toBeViewedItem?.id}" params="[toBeMovedItemId:child?.id]" class="delButtonActive"></g:link></td><td style="vertical-align: middle"><g:link controller="item" action="handleMoveToLogbook" id="${toBeViewedItem?.id}" params="[toBeMovedItemId:child?.id]" class="dialogfont">Yes</g:link></td>
                         </tr>   
                         
                    </g:if>

                    <g:else>
                      <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
                        <g:if test="${child?.isUnchainable}">
                          <tr>
                            <td style="width: 1%"><g:link controller="item" action="handleUnchain" id="${toBeViewedItem?.id}" params="[toBeUnchainedItemId:child?.id]" class="unchainButtonActive"></g:link></td><td style="vertical-align: middle"><g:link controller="item" action="handleUnchain" id="${toBeViewedItem?.id}" params="[toBeUnchainedItemId:child?.id]" class="dialogfont">This list only</g:link></td>
                          </tr>
                        </g:if>
                        <g:if test="${child?.isDeletable}">
                          <tr>
                            <td style="width: 1%"><g:link controller="item" action="handleMoveToLogbook" id="${toBeViewedItem?.id}" params="[toBeMovedItemId:child?.id]" class="delButtonActive"></g:link></td><td style="vertical-align: middle"><g:link controller="item" action="handleMoveToLogbook" id="${toBeViewedItem?.id}" params="[toBeMovedItemId:child?.id]" class="dialogfont">All lists (-> logbook)</g:link></td>
                          </tr>
                        </g:if>
                    </g:else>

                    <tr>
                     <td style="width: 1%"><a href="#" class="cancelButton" onClick="javascript:Modalbox.hide()"></a></td><td style="vertical-align:middle; text-align: left"><a href="#" class="dialogfont" onClick="javascript:Modalbox.hide()">Cancel</a></td>
                    </tr>
                </table>
              </div>
          </div>
            <a href="#" title="Please Confirm..." onclick="
            Modalbox.show($('deleteDialog_${child?.id}'), {title: this.title, width: 250, slideDownDuration: 0.10, slideUpDuration: 0.10, overlayDuration: 0.2, resizeDuration: 0.01}); return false;
            " class="checkButton">
            </a>
        </g:if>
        <g:else>      
            <a href="#" class="delButton" target="_self"></a>
        </g:else>
      </g:if>
    </div>
    
    <div class="left" style="display: inline-block; width: 90%; padding-bottom: 8px">
      <g:link class="itemname" controller="item" action="stdview" id="${child?.id}">
        ${child?.name}<g:if test="${child?.notes}">...</g:if>
      </g:link>
      <g:if test="${child?.children?.size() != 0}">
        <span class="childcountList">${child?.children?.size()}</span>
      </g:if>
  
      <br/>

      <div style="margin-left: 6px">

          <g:if test="${child?.tags?.size()>0}">

                <g:each in="${child?.tags}" var="childtag">
                  <g:if test="${toBeViewedItem?.id != childtag?.id}">
                         <g:link  class="taglinklist" action="stdview" controller="item" id="${childtag?.id}">${childtag?.name},</g:link>
                  </g:if>
                </g:each>

          </g:if>

          <!-- DatumsTags des Kindes anzeigen -->
  
          <g:each in="${child?.itemDates}" var="childDate">              
                <g:link class="datelink" action="calendarlist" controller="itemDate" id="${childDate?.id}">${childDate.getPeriodString()},</g:link>              
          </g:each>
        
      </div>
    </div>

    <div style="clear:left"> </div>
    
  </div>







