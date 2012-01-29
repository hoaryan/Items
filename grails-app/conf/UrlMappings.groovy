class UrlMappings {
   
    //**
    //    URL Mapping Precedence rules:
    //
    //    The precedence is calculated on a token by token basis.
    //    A URL is made up of a number of tokens, divided by /, #
    //    the more tokens a URL has the higher its precedence.
    //    The more concrete definitions within the tokens the higher its precedence.
    //**


    // ************ Web URIs **********************************

    static mappings = {

        "/"(view:'index')

        "/*"(view:'index')

        "400"(view:'/error')
        "404"(view:'/error')
        "500"(view:'/error')
        
        // Error Controller
        "/error/$action/$id?"(parseRequest:true){
            controller = "error"
        }



        // WEB Mapping **********



        "/web/$controller/$action/$id?"(parseRequest:true){
        }

        
        // REST Mappings ***********

        "/rest/item/$id?"(parseRequest:true, controller: "itemREST") {
            action = [GET:"getItemRest", PUT:"updateItemRest", DELETE:"deleteItemRest", POST:"createItemRest"]
        }

        "/rest/items"(parseRequest:true) {
            action = [GET:"getItemsRest"]
            controller = "itemREST"
        }

        "/rest/item/$id/tags/$tagid?"(parseRequest:true, controller: "itemREST") {
            action = [GET:"getTagsRest", PUT:"assignTagToItemRest", DELETE:"removeTagFromItemRest", POST:"workaroundRestTagActionHub"]
        }

        
       
        /*"/rest/item/$id/tags/count"(parseRequest:true, controller: "ItemREST") {
            action = [GET:"tagsCountRest"]
        }*/

        "/rest/item/$id/children"(parseRequest:true, controller: "itemREST") {
            action = [GET:"getChildrenRest"]
        }

        /*"/rest/item/$id?/children/count"(parseRequest:true, controller: "itemREST") {
            action = [GET:"childrenCountRest"]
        }*/

    }
}
