<div class="tagCheckboxList">
    <div class="groupseparator">
          Search Results
     </div>


      <g:each in="${tagOptions}" var="tagOption">
           <div class="tagCheckboxListElement">
              <g:link class="itemname" style="width: 80%; overflow: hidden; padding-bottom: 6px" controller="item" action="stdview" id="${tagOption?.id}">${tagOption?.name}</g:link>
              <g:link class="tagNotAssigned" action="handleAddTag" id="${itemInstance?.id}" params="[tagItemId:tagOption?.id]"></g:link>              
           </div>
      </g:each>

</div>

