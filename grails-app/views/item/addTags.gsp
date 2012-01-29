

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="layout" content="main" />     
    </head>
    <body>  
      <div id="toptoolbar">
             <div class="left" style="width: 80%">
                <div id="itemname">
                  <p class="itemname" style="font-weight: normal; font-size: 12px">Add/Remove Tags:</p>
                  <br/>
                  <g:link class="itemname" action="stdview" id="${itemInstance.id}">${itemInstance.name}</g:link>
                </div>
                <!--<span class="childcount">[${itemInstance?.children.size()}]</span>-->
             </div>
              <div class="right">
                <a href="javascript:history.back()" class="backButton" ></a>
                <g:link class="homeButton" action="homeview"></g:link>
              </div>

             <div class="clearfloat"></div>
       </div>

      <div id="additionaltoolarea">
          <div>
            <div class="left">
              <!--ohne die wrapperFrom kann man den focus() grundsätzlich nicht in das search field setzen-->
              <form name="wrapperForm">
                  <g:remoteField
                      style="margin-top: 6px"
                      name="searchInput"
                      update="searchResults"
                      id="${itemInstance?.id}"
                      paramName="searchString"
                      action="searchTags"
                      before=""
                      onLoading="showSpinner_2(true, '6D84A2')"
                      onLoaded="showSpinner_2(false, '6D84A2')"
                      onComplete="showSpinner_2(false, '6D84A2')"
                      onFailure="showSpinner_2(false, '6D84A2')"
                      />
              </form>
              <a href="javascript:resetSearchField()" class="xButton"></a>
            </div>
            <div class="right">
              <img id="spinner_6D84A2" style="display:none; vertical-align:top; margin-top: 7px; margin-right: 20px"
                src="<g:resource dir='/images' file='spinner_6D84A2.gif' />" />
            </div>
          </div>
      </div>

        <g:render template="../common/message" />

        <g:hasErrors bean="${itemInstance}">
           <div id="messagebox" style="display: none">
              <div class="errors">
                <g:renderErrors bean="${itemInstance}" as="list" />
              </div>
           </div>
           <g:javascript>
                showMessage();
           </g:javascript>
        </g:hasErrors>

          <div id="listarea">            

                 <div class="tagCheckboxList">
                    <g:render template="untagcheckbox"/>
                    <div id="searchResults">
                    </div>
                    <g:render template="tagcheckbox"/>
                 </div>                 
          </div>

    <div id="bottomtoolbar">
      <!--<div class="left" style="vertical-align: middle">
        <span class="button"><g:link controller="item" action="stdview" id="${itemInstance.id}">BACK</g:link></span>
      </div>-->
    </div>
    
    <g:javascript>
        document.wrapperForm.searchInput.focus();
    </g:javascript>

    </body>
</html>
