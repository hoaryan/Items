<!--ohne die wrapperFrom kann man den focus() grundsätzlich nicht in das search field setzen-->
<form name="wrapperForm" id="wrapperForm">
<g:remoteField
    style="margin-top: 6px; width: 135px"
    name="searchInput"
    update="listarea"
    id="${toBeViewedItem?.id}"
    paramName="searchString"
    onLoading="showSpinner(true)"
    onComplete="showSpinner(false)"
    onLoaded="showSpinner(false)"
    onFailure="showSpinner(false)" 
    action="search"
    before=""/>
</form>

<a href="javascript:resetSearchField()" class="xButton"></a>
<img id="spinner" style="display:none; vertical-align:top; margin-top: 7px"
     src="<g:resource dir='/images' file='spinner_6D84A2.gif' />" />
