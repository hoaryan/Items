  <div class="childitem">
    <div class="left">
      <!-- DatumsTags des Kindes anzeigen -->      

      <span style="color:red; font-size: 10px; padding-left: 4px">(since ${DateUtil.getDuePeriod(dueItem, startDate)} days)</span> <br/>
      <g:link class="calendar_itemname" controller="item" action="stdview" id="${dueItem.id}">${dueItem.name}</g:link>
      
      
             
    </div>
    
    <div class="right">      
      <a href="#" title="Please Confirm..." onclick="
        Modalbox.show($('deleteItemDateDialog_${dueItem?.id}'), {title: this.title, width: 260, slideDownDuration: 0.10, slideUpDuration: 0.10, overlayDuration: 0.2, resizeDuration: 0.01}); return false;
        " class="delButtonActive">
      </a>
    </div>

    <div style="clear:left"> </div>
  </div>

<div id="deleteItemDateDialog_${dueItem?.id}" style="display:none">
  <span class="dialogfont" style="padding: 5px">Remove Date from Item "${dueItem?.name}"?<br>${dueItem.name} </span>
    <br>
    <g:if test="${dueItem.itemDates.size()<=1}">
      <span class="dialogfont" style="padding: 5px">Last Date for Item !</span>
    </g:if>
    <g:if test="${!dueItem.getIsDeletable()}">
      <span class="dialogfont" style="padding: 5px">Item is not deletable.</span>
    </g:if>
    <table style="width: 100%; border-width: 0px; padding: 0px; margin: 0px">
      <g:if test="${dueItem?.itemDates.size()<=1}">
        <g:if test="${dueItem.getIsDeletable()}">
          <tr>
            <td style="width: 1%"><g:link controller="item" action="handleMoveToLogbook" params="[toBeMovedItemId:dueItem.id, startDate:startDate.format('yyyy-MM-dd'), endDate:endDate.format('yyyy-MM-dd'), viewType: viewType]" class="delButtonActive"></g:link></td><td  style="vertical-align: middle"><g:link controller="item" action="handleMoveToLogbook" params="[toBeMovedItemId:dueItem?.id, startDate:startDate.format('yyyy-MM-dd'), endDate:endDate.format('yyyy-MM-dd'), viewType: viewType]" class="dialogfont">Delete Item</g:link></td>
          </tr>
        </g:if>
      </g:if>
      <tr>
        <td style="width: 1%"><g:link class="delDateButton" controller="itemDate" action="deleteDate" params="[itemId:dueItem.id, startDate:startDate.format('yyyy-MM-dd'), endDate:endDate.format('yyyy-MM-dd'), viewType: viewType, id:dueItem.itemDates.first().id]"></g:link></td><td  style="vertical-align: middle"><g:link class="dialogfont" controller="itemDate" action="deleteDate" params="[id:dueItem.itemDates.first().id, itemId:dueItem.id, startDate:startDate.format('yyyy-MM-dd'), endDate:endDate.format('yyyy-MM-dd'), viewType: viewType]">Just remove Date</g:link></td>
      </tr>
      <tr>
        <td style="width: 1%"><a href="#" class="cancelButton" onClick="javascript:Modalbox.hide()"></a></td><td  style="vertical-align: middle"><a href="#" onClick="javascript:Modalbox.hide()" class="dialogfont">Cancel</a></td>
      </tr>
    </table>
</div>





