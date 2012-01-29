<html>
  <head>
    <meta name="layout" content="main" />
  </head>


  <body>
      <div id="toptoolbar">
        <div class="left">
          <p class="itemname" style="font-weight: normal; font-size: 12px">Calendar:</p>
          <br>         
          <p class="calendar_datelabel">
          <g:if test="${viewType==CalendarviewType.VIEW_TODAY}">

            <g:if test="${DateUtil.equalsDay(startDate, new Date())}">
              Today
            </g:if>
            <g:else>
              <g:if test="${DateUtil.equalsDay(startDate, new Date()+1)}">
                Tomorrow
              </g:if>
              <g:else>
                <g:formatDate format="dd.MM.yy" date="${startDate}"/>
              </g:else>
            </g:else>           
          </g:if>

          <g:else>
            <g:if test="${viewType==CalendarviewType.VIEW_THIS_WEEK}">
              <g:formatDate format="dd.MM.yy" date="${startDate}"/>
              - <g:formatDate format="dd.MM.yy" date="${endDate}"/>
            </g:if>
            <g:else>
              <g:if test="${viewType==CalendarviewType.VIEW_THIS_MONTH}">
                ${DateUtil.getMonthString(startDate.getMonth())}  <g:formatDate format="yyyy" date="${startDate}"/>
              </g:if>
              <g:else>
                <g:if test="${viewType==CalendarviewType.VIEW_CUSTOM}">
                  <g:formatDate format="dd.MM.yy" date="${startDate}"/>
                  <g:if test="${!DateUtil.equalsDay(startDate, endDate)}">
                    - <g:formatDate format="dd.MM.yy" date="${endDate}"/></g:if>
                </g:if>
                <g:else>
                  no viewType :/
                </g:else>
              </g:else>
            </g:else>
          </g:else>
          
            
          </p>
        </div>
        <div class="right">
          <a href="javascript:history.back()" class="backButton"></a>
          <g:link class="homeButton" controller="item" action="homeview"></g:link>
        </div>
      </div>

    <div id="additionaltoolarea" style="vertical-align: middle">
        <div>
          <div class="left">
            
                  <g:if test="${viewType==CalendarviewType.VIEW_TODAY}">

                    <table style="color: #FF0; border: 0px solid #FFF; height: 33px; width: 10%; padding: 0px; margin: 0px">
                      <tr>
                        <td style="margin-top: 5px">
                            <g:link class="previousButton" action="calendarlist" controller="itemDate" params="[startDate:(startDate-1).format('yyyy-MM-dd'), viewType: CalendarviewType.VIEW_TODAY]" ></g:link>
                        </td>
                        <td>
                          <span class="dialogfont" style="vertical-align:middle"><g:formatDate format="dd.MM.yy" date="${startDate}"/></span>
                        </td>
                        <td style="margin-top: 5px">
                            <g:link class="nextButton" action="calendarlist" controller="itemDate" params="[startDate:(startDate+1).format('yyyy-MM-dd'), viewType: CalendarviewType.VIEW_TODAY]" ></g:link>
                        </td>
                      </tr>
                    </table>
                  </g:if>
                  <g:else>

                    <g:if test="${viewType==CalendarviewType.VIEW_THIS_WEEK}">
                      <table style="color: #FF0; border: 0px solid #FFF; height: 33px; width: 10%; padding: 0px; margin: 0px">
                        <tr>
                          <td style="margin-top: 5px">
                              <g:link class="previousButton" action="calendarlist" controller="itemDate" params="[startDate:(DateUtil.getWeekBeginning(startDate)-7).format('yyyy-MM-dd'),endDate:(DateUtil.getWeekEnd(startDate)-7).format('yyyy-MM-dd'),viewType:CalendarviewType.VIEW_THIS_WEEK]" ></g:link>
                          </td>
                          <td>
                            <span class="dialogfont" style="vertical-align:middle; white-space: nowrap">KW ${DateUtil.getWeekInYear(startDate)}</span>
                          </td>
                          <td style="margin-top: 5px"">
                              <g:link class="nextButton" action="calendarlist" controller="itemDate" params="[startDate:(DateUtil.getWeekBeginning(startDate)+7).format('yyyy-MM-dd'),endDate:(DateUtil.getWeekEnd(startDate)+7).format('yyyy-MM-dd'),viewType:CalendarviewType.VIEW_THIS_WEEK]" ></g:link>
                          </td>
                        </tr>
                      </table>
                    </g:if>
                    <g:else>

                      <g:if test="${viewType==CalendarviewType.VIEW_THIS_MONTH}">
                        <table style="color: #FF0; border: 0px solid #FFF; height: 33px; width: 10%; padding: 0px; margin: 0px">
                          <tr>
                            <td style="margin-top: 5px">
                                <g:link class="previousButton" action="calendarlist" controller="itemDate" params="[startDate:DateUtil.getFirstDayInMonth(DateUtil.getFirstDayInMonth(startDate)-1).format('yyyy-MM-dd'),endDate:DateUtil.getLastDayInMonth(DateUtil.getFirstDayInMonth(startDate)-1).format('yyyy-MM-dd'),viewType:CalendarviewType.VIEW_THIS_MONTH]" ></g:link>
                            </td>
                            <td>
                               <span class="dialogfont" class="dialogfont" style="vertical-align:middle; white-space: nowrap">${DateUtil.getShortMonthString(startDate.getMonth())}</span>
                            </td>
                            <td style="margin-top: 5px">
                                  <g:link class="nextButton" action="calendarlist" controller="itemDate" params="[startDate:(DateUtil.getLastDayInMonth(startDate)+1).format('yyyy-MM-dd'),endDate:DateUtil.getLastDayInMonth(DateUtil.getLastDayInMonth(startDate)+1).format('yyyy-MM-dd'),viewType:CalendarviewType.VIEW_THIS_MONTH]" ></g:link>
                            </td>
                          </tr>
                        </table>
                      </g:if>

                      <g:else>
                        

                        <g:if test="${viewType==CalendarviewType.VIEW_CUSTOM}">
                          <table style="color: #FF0; border: 0px solid #FFF; height: 33px; width: 10%; padding: 0px; margin: 0px">
                            <tr>
                              <td style="margin-top: 5px">
                                  <g:link class="previousButton" action="calendarlist" controller="itemDate" params="[startDate:(startDate-7).format('yyyy-MM-dd'),endDate:(startDate-1).format('yyyy-MM-dd'),viewType:CalendarviewType.VIEW_CUSTOM]" ></g:link>
                              </td>
                              <td>
                                <span class="dialogfont" style="vertical-align:middle; white-space: nowrap">7 days</span>
                              </td>
                              <td style="margin-top: 5px">
                                  <g:link class="nextButton" action="calendarlist" controller="itemDate" params="[startDate:(endDate+1).format('yyyy-MM-dd'),endDate:(endDate+7).format('yyyy-MM-dd'),viewType:CalendarviewType.VIEW_CUSTOM]" ></g:link>
                              </td>
                            </tr>
                          </table>
                        </g:if>






                        <g:else>
                            <a href="#" class="previousButton" onClick="" ></a>
                          <span class="showDateLabel">not defined</span>
                            <a href="#" class="nextButton" onClick="" ></a>
                        </g:else>

                      </g:else>
                    </g:else>
                  </g:else>
            
          </div>
          <div class="right">
            <table style="border: 0px solid #FFF; height: 33px; padding: 0px; margin: 0px">
              <tr>
                <td style="vertical-align:middle; margin: 0px; padding:0px"><g:link class="dialogfont" style="margin: 0px" controller="itemDate" action="calendarlist" params="[startDate:(new Date()).format('yyyy-MM-dd'),endDate:(new Date()).format('yyyy-MM-dd'),viewType:CalendarviewType.VIEW_TODAY]">day</g:link><span class="dialogfont">&nbsp;|&nbsp;</span></td>
                <td style="vertical-align:middle; margin: 0px; padding:0px"><g:link class="dialogfont" style="margin: 0px" controller="itemDate" action="calendarlist" params="[startDate:DateUtil.getWeekBeginning(new Date()).format('yyyy-MM-dd'),endDate:DateUtil.getWeekEnd(new Date()).format('yyyy-MM-dd'),viewType:CalendarviewType.VIEW_THIS_WEEK]">week</g:link><span class="dialogfont">&nbsp;|</span></td>
                <td style="vertical-align:middle; margin: 0px; padding:0px"><g:link class="dialogfont" style="margin-right: 6px" controller="itemDate" action="calendarlist" params="[startDate:DateUtil.getFirstDayInMonth(new Date()).format('yyyy-MM-dd'),endDate:DateUtil.getLastDayInMonth(new Date()).format('yyyy-MM-dd'),viewType:CalendarviewType.VIEW_THIS_MONTH]">month</g:link></td>
              </tr>
            </table>
          </div>
          <div style="clear:left"> </div>
        </div>
      </div>

      <div id="listarea">

        <g:render template="../common/message" />

        <g:if test="${overDueItems}">
          <div class="groupseparator">
            <span style="color:red">Due Items</span>
          </div>
          <g:render template="../itemDate/dueDate" collection="${overDueItems}" var="dueItem"/>
        </g:if>

        <g:if test="${!inRangeItems}">
          <div class="listinfo">
              No items for this period..
          </div>
        </g:if>

        <g:each var="i" in="${(0..(endDate-startDate))}">
          <!-- ${startDate+i} : ${DateUtil.itemsInDate(inRangeItems, (startDate+i) )}<br/>-->
          <g:if test="${DateUtil.itemsInDate(inRangeItems, (startDate+i) )}">
            <div class="groupseparator">
                <g:formatDate format="E, dd.MM.yy" date="${(startDate+i)}"/>
            </div>
          </g:if>

          <g:each var="ittwo" in="${inRangeItems}">
              <g:if test="${ittwo.dateInDateTags((startDate+i))}">                  
                <g:render template="../itemDate/childDate" var="child" model="['child':ittwo, 'currentDate':startDate+i,'itemDate': ittwo.getDateInDateTagsForDate(startDate+i)]"/>
              </g:if>
            </g:each>
        </g:each>
         
      </div>




      <div id="bottomtoolbar">

      </div>


  </body>
</html>