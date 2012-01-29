<html>
  <head>
  
    <meta name="layout" content="main" />

    <!--Java-Script damit focus bei tastendruck auf dem suchfeld liegt-->
    <g:javascript>
        window.captureEvents(Event.KEYPRESS);
        window.onkeydown = focusSearchFieldonKeyPressed;
    </g:javascript>
  </head>

  <body>
      <div id="toptoolbar" >
        <div class="left">
          <a class="itemname" href="">Home</a>
          <div class="childcountTop">${homeItemSize}</div>
        </div>     
        <div class="right">
                      <g:link class="logoutButton" controller="user" action="logout"></g:link>
        </div>
      </div>

    <div id="additionaltoolarea">
        <div>
              <div class="left">
                   <g:render template="search"/>
              </div>
              <div class="right">                    
                    <g:if test="${session.user?.isAdmin}">
                      <g:link class="adminButton" controller="user" action="admin" target="_new"></g:link>
                    </g:if>
                      <g:link class="addButton" action="createInbox" target="_self"></g:link>
                      <g:link class="helpButton"/>
                      <g:link class="settingsButton" controller="user" action="edit"></g:link>
                    
              </div>
              <div style="clear:left"> </div>
            </div>
    </div>


      <g:render template="../common/message" />

      <div id="listarea">
      </div>


      <div id="homefunctionarea">
            <div class="groupseparator">Home</div>

            <div class="childitem"><!--inbox-->
              <div class="left">                
                <table style="padding:0px;margin:0px;border-width:0px">
                  <tr style="padding:0px;margin:0px;border-width:0px">
                    <td style="vertical-align:middle;border-width:0px; padding:0px; margin:0px"><g:link class="logoInbox" controller="item" action="stdview" id="${inboxItem?.id}"></g:link></td>
                    <td style="vertical-align:middle;border-width:0px; padding:0px; margin:0px">
                      <g:link class="itemname" controller="item" action="stdview" id="${inboxItem?.id}">${inboxItem?.name}</g:link>                                          
                        <g:if test="${inboxItem?.children?.size() != 0}">
                          <span class="childcountList" style="padding: 3px">
                          ${inboxItem?.children?.size()}
                          </span>
                        </g:if>                    
                    </td>
                  </tr>
                </table>               
              </div>
              <div class="right">
              </div>
              <div style="clear:left"> </div>
            </div><!--inbox-->

            <div class="childitem"><!--logbook-->
              <div class="left">
                <table style="padding:0px;margin:0px;border-width:0px">
                  <tr style="padding:0px;margin:0px;border-width:0px">
                    <td style="padding:0px;margin:0px;border-width:0px"><g:link class="logoLogbook" controller="item" action="stdview" id="${logbookItem?.id}"></g:link></td>
                    <td style="vertical-align:middle;border-width:0px; padding:0px;margin:0px">

                      <g:link class="itemname" controller="item" action="stdview" id="${logbookItem?.id}">${logbookItem?.name}</g:link>
                        <g:if test="${Item.findAllByUserAndDeleted(session.user, true).size() != 0}">
                          <span class="childcountList" style="padding: 3px">
                          ${Item.findAllByUserAndDeleted(session.user, true).size()}
                           </span>
                        </g:if>

                    </td>
                  </tr>
                </table>                
              </div>
              <div class="right">
              </div>
              <div style="clear:left"> </div>
            </div><!--logbook-->


            <div class="childitem">
              <div class="left">
                <table style="padding:0px;margin:0px;border-width:0px">
                  <tr style="padding:0px;margin:0px;border-width:0px">
                    <td style="padding:0px;margin:0px;border-width:0px"><div class="logoCalendar" /></td>
                    <td style="white-space:nowrap; vertical-align:middle;padding:0px;margin:0px;border-width:0px">
                      <g:link class="itemname" controller="itemDate" action="calendarlist" params="[startDate:(new Date()).format('yyyy-MM-dd'),viewType:CalendarviewType.VIEW_TODAY]">Today</g:link>
                      <g:if test="${itemsTodaySize>0 || dueItemsCount>0}">
                        <span class="childcountList" style="padding: 3px; background-color: red; border: 1px solid red; margin: 0px">
                          <g:if test="${itemsTodaySize>0}">${itemsTodaySize}</g:if>
                          <g:if test="${itemsTodaySize>0 && dueItemsCount>0}">|</g:if>
                          <g:if test="${dueItemsCount>0}"><span style="font-size: 10px">${dueItemsCount} due</span></g:if>
                        </span>
                      </g:if>
                      <span class="itemname" style="padding: 0px; margin:0px">,&nbsp;</span>
                      <g:link class="itemname" controller="itemDate" action="calendarlist" params="[startDate:DateUtil.getWeekBeginning(new Date()).format('yyyy-MM-dd'),endDate:DateUtil.getWeekEnd(new Date()).format('yyyy-MM-dd'),viewType:CalendarviewType.VIEW_THIS_WEEK]"> Week</g:link>
                      <g:if test="${itemsThisWeekSize>0}"><span class="childcountList" style="padding: 3px">${itemsThisWeekSize}</span></g:if>
                      <span class="itemname" style="padding: 0px; margin:0px">,&nbsp;</span>
                      <g:link class="itemname" controller="itemDate" action="calendarlist" params="[startDate:DateUtil.getFirstDayInMonth(new Date()).format('yyyy-MM-dd'),endDate:DateUtil.getLastDayInMonth(new Date()).format('yyyy-MM-dd'),viewType:CalendarviewType.VIEW_THIS_MONTH]">Month</g:link>
                      <g:if test="${itemsThisMonthSize>0}"><span class="childcountList" style="padding: 3px">${itemsThisMonthSize}</span></g:if>
                    </td>
                  </tr>
                </table>
              </div>
              <div class="right">
              
              </div>
            </div>


        </div>



            <div class="groupseparator"><span>Home Node Items</span></div>
          <div>
            <g:render template="../item/childItem" var="child" collection="${toBeViewedItem?.children.findAll{ item -> item.itemType==Item.ITEM_NORMAL}}" />
          </div>




      <div id="bottomtoolbar">
        
      </div>




  </body>
</html>