     <div class="groupseparator">
          Contained in Lists
     </div>
     
     <g:each in="${untagOptions}" var="untagOption">

        <div class="tagCheckboxListElement">
                  <g:link class="itemname" style="width: 80%; overflow: hidden; padding-bottom: 6px" controller="item" action="stdview" id="${untagOption?.id}">${untagOption?.name}</g:link>
                  <g:link class="tagAssigned" action="handleRemoveTag" id="${itemInstance?.id}" params="[untagItemId:untagOption?.id]"></g:link>
        </div>

      </g:each>