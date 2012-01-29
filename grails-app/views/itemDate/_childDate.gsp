  <div class="childitem">
    <div class="left">
      <!-- DatumsTags des Kindes anzeigen -->
      <g:if test="${child?.getShowTime(currentDate)}">
        <ul class="calendar_datetaglist" style="padding: 0px; margin: 0px">
        <g:each in="${child?.itemDates}" var="childDate">
            <li>
              <div class="calendar_childdatelabel" style="padding: 0px; margin: 3px">
                <g:if test="${!childDate.isPeriod()}">                  
                  ${childDate.getTimeString()}
                </g:if>
                <g:else><!-- isPeriod -->
                  <g:if test="${!childDate.endInSameDay()}">

                    <g:if test="${childDate.getSameStartDay(currentDate)}">
                      ab ${childDate.getStartTimeString()}
                    </g:if>
                    <g:else><!-- currDate != startDate -->
                      <g:if test="${childDate.getSameEndDay(currentDate)}">
                        bis ${childDate.getEndTimeString()}
                      </g:if>
                    </g:else>
                  </g:if>
                  <g:else><!-- endInSameDay -->
                    ${childDate.getTimeString()}
                  </g:else>

                </g:else>                
                
              </div>
            </li>
        </g:each>

      </ul>
      
      </g:if>

      <g:if test="${itemDate.isInDay(currentDate)}">
        <g:link class="calendar_itemname" controller="item" action="stdview" id="${child?.id}">${child?.name}<g:if test="${child?.notes}">...</g:if></g:link>
        <g:if test="${child?.children?.size() != 0}">
          <span class="childcountList">${child?.children?.size()}</span>
        </g:if>
      </g:if>
        <!--<g:if test="${child?.children?.size() != 0}">
          <span class="childcount">
          [${child?.children?.size()}]
          </span>
        </g:if>-->
      

      

     <!-- <g:if test="${child?.tags?.size()>0}"> TODO: das funzt irgendwie net...
      <ul class="calendar_taglist">
        <g:each in="${child?.tags}" var="childtag">
          <g:if test="${toBeViewedItem?.id != childtag?.id}">
            <li>          
                 [<g:link action="stdview" controller="item" id="${childtag?.id}">${childtag?.name}</g:link>]
            </li>
          </g:if>         
        </g:each>
      </ul>
      <br>
      </g:if>-->
             
    </div>
    
    <div class="right">      
      <a href="#" title="Please Confirm..." onclick="
        Modalbox.show($('deleteItemDateDialog_${itemDate?.id}'), {title: this.title, width: 260, slideDownDuration: 0.10, slideUpDuration: 0.10, overlayDuration: 0.2, resizeDuration: 0.01}); return false;
        " class="delButtonActive">
      </a>
    </div>

    <div style="clear:left"> </div>
  </div>

<div id="deleteItemDateDialog_${itemDate?.id}" style="display:none">

    <g:if test="${child.itemDates.size()<=1 && child.getIsDeletable()}">
      <span class="dialogfont" style="padding: 5px">
        Date: "${itemDate}" is the only/last date for item "${child?.name}". <br><br>
      </span>
        <br>
      <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
        <tr>
          <td style="width: 1%"><g:link controller="item" action="handleMoveToLogbook" params="[toBeMovedItemId:child.id, startDate:startDate.format('yyyy-MM-dd'), endDate:endDate.format('yyyy-MM-dd'), viewType: viewType]" class="delButtonActive"></g:link></td><td  style="vertical-align: middle"><g:link controller="item" action="handleMoveToLogbook" params="[toBeMovedItemId:child?.id, startDate:startDate.format('yyyy-MM-dd'), endDate:endDate.format('yyyy-MM-dd'), viewType: viewType]" class="dialogfont">Delete whole Item</g:link></td>
        </tr>
        <tr>
          <td style="width: 1%"><g:link class="delDateButton" controller="itemDate" action="deleteDate" params="[itemId:child.id, startDate:startDate.format('yyyy-MM-dd'), endDate:endDate.format('yyyy-MM-dd'), viewType: viewType, id:itemDate.id]"></g:link></td><td  style="vertical-align: middle"><g:link class="dialogfont" controller="itemDate" action="deleteDate" params="[id:itemDate.id, itemId:child.id, startDate:startDate.format('yyyy-MM-dd'), endDate:endDate.format('yyyy-MM-dd'), viewType: viewType]">Just remove Date</g:link></td>
        </tr>
        <tr>
          <td style="width: 1%"><a href="#" class="cancelButton" onClick="javascript:Modalbox.hide()"></a></td><td  style="vertical-align: middle"><a href="#" onClick="javascript:Modalbox.hide()" class="dialogfont">Cancel</a></td>
        </tr>
      </table>
    </g:if>
    <g:else>
      <span class="dialogfont" style="padding: 5px">
        Item has listitems and therefore can't be completely deleted! <br/><br/>
        Do you want to remove Date: "${itemDate}" from item "${child?.name}". <br>
      </span>
        <br>
      <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
        <tr>
          <td style="width: 1%"><g:link class="delDateButton" controller="itemDate" action="deleteDate" params="[itemId:child.id, startDate:startDate.format('yyyy-MM-dd'), endDate:endDate.format('yyyy-MM-dd'), viewType: viewType, id:itemDate.id]"></g:link></td><td  style="vertical-align: middle"><g:link class="dialogfont" controller="itemDate" action="deleteDate" params="[id:itemDate.id, itemId:child.id, startDate:startDate.format('yyyy-MM-dd'), endDate:endDate.format('yyyy-MM-dd'), viewType: viewType]">Remove Date</g:link></td>
        </tr>
        <tr>
          <td style="width: 1%"><a href="#" class="cancelButton" onClick="javascript:Modalbox.hide()"></a></td><td  style="vertical-align: middle"><a href="#" onClick="javascript:Modalbox.hide()" class="dialogfont">Cancel</a></td>
        </tr>
      </table>
    </g:else>
</div>





