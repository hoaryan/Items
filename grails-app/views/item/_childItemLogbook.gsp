  <div class="childitem">
    <div class="left" style="width: 80%; border: 0px solid black; padding-bottom: 6px">
      <div class="dialogfont" style="padding-left: 3px; padding-top: 3px"><g:formatDate format="dd.MM.yy HH:mm" date="${child?.lastUpdated}"/></div>
      <br/>
      <div class="itemname">${child?.name}</div>      

      <br>

      <!-- DatumsTags des Kindes anzeigen -->
      <ul class="datetaglist" style="padding-left: 3px">
        <g:each in="${child?.itemDates}" var="childDate">
            <li>
              <g:link class="datelink" action="calendarlist" controller="itemDate" id="${childDate?.id}">${childDate.getPeriodString()}</g:link>
            </li>
        </g:each>
      </ul>        
    </div>
    
    <div class="right">
      <g:if test="${child.itemType==Item.ITEM_NORMAL}">
            <g:link class="restoreButton" onclick="return confirm('Soll das Item wiederhergestellt werden?');" controller="item" action="handleRestore" id="${toBeViewedItem?.id}" params="[toBeRestoredItemId:child?.id]" target="_self"></g:link>
            <g:link class="delFromLogButton" onclick="return confirm('Soll das Item endgültig gelöscht werden?');" controller="item" action="handleDelete" id="${toBeViewedItem?.id}" params="[toBeDeletedItemId:child?.id]" target="_self"></g:link>
      </g:if>
    </div>

    <div style="clear:left"> </div>
  </div>







