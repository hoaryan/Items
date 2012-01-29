
import grails.converters.*

class ItemRESTController {

    // auto injected by GRAILS
    def itemService

    def authService

    /**
     * REST Function: getItem
     */
    def getItemRest = {

        log.info( "call action: getItemRest from User " + authService.getUser())

        def item = null

        if(params.id) {

            Long id
            if(params.id == "home") { // test if home item is requested
                id = new Long(authService.getUser().homeItemID)                
            } else {
                id = new Long(params.id)
            }
            
            Result result = itemService.getItemXML(authService.getUser(), id)
            if(result.isOK()){
                render result.object
            } else {
                render result.message
            }
        } else {
            response.status = 404 //Not found
            render """GET request has no parameter: ID"""
        } 
    }

    /**
     * REST Function: createItem
     */
    def createItemRest = {
        // Furchtbarer Workaround für Flex Clients und Browser, die keine PUT- und DELETE Requests senden können
        // TODO: in UrlMappings realisieren? Eigenes Mapping innerhalb des Controllers?
        String methodOverrideHeader = request.getHeader("X-HTTP-Method-Override")
        if (methodOverrideHeader == "PUT") {
            log.info "method override -> PUT"
            this.updateItemRest()
        }
        else if (methodOverrideHeader == "DELETE") {
            log.info "method override -> DELETE"
            this.deleteItemRest()
        }
        else if (methodOverrideHeader == "GET") {
            log.info "method override -> GET"
            this.getItemRest()
        }
        else
        {
            log.info "doing createItemRest, methodOverrideHeader is " + methodOverrideHeader
            log.info "call action: createItemRest"

            User user = authService.getUser()
            
            String parentIDString = request.XML.parent.text()
            Long parentID = new Long(parentIDString)

            def newItem = request.XML.item
            Item createItem = new Item(
                name: newItem.name?.text(),
                notes: newItem.notes?.text(),
                clickCount: newItem.clickcount?.text(),
                deleted: newItem.deleted?.text()
            );

//            log.debug("user: " + user + " class: " + user.getClass().getName())
//            log.debug("parentID: " + parentID + " class: " + parentID.getClass().getName())
//            log.debug("new item: " + createItem + " class: " + createItem.getClass().getName())

            def result = itemService.saveItem(user, parentID, createItem )

            if(result.isOK()){
                def response
                if(result.object instanceof Item){
                    response = result.object.asDeepXML()
                    log.info "item sucessfully created: " + response                    
                } else{
                    response = "Missing item instance in Result object."
                    log.info response
                }
                render response
            } else {
                log.info "Creating item failed: " + result.message
                render result.message
            }
        }
    }

    /**
     * REST Function: updateItem
     */
    def updateItemRest = {
        log.debug "call action: updateItemRest"
        log.debug "request: " + request
        log.debug "request.XML: " + request.XML
        log.debug "body: " + request.XML.item

        if (request.XML.item) {

            User user = authService.getUser()            
            Long parentID = null
            def newItem = request.XML            
            
            Result result = itemService.getItem(user, new Long(params.id));
            Item updateItem = null
            if(result.isOK()){
                log.debug "result is OK, result object is " + result.object
                updateItem = (Item)result.object

                updateItem.name = newItem.name?.text()
                updateItem.notes = newItem.notes?.text()

                //updateItem.name = newItem.name
                //updateItem.notes = newItem.notes

                log.debug "update item is now " + updateItem
            } else {
                log.error "Updating Item failed. Could not find Item with id " + itemID + ": " + result.message
                render result.message
                return 
            }

//            log.debug("user: " + user + " class: " + user.getClass().getName())
//            log.debug("parentID: " + parentID + " class: " + parentID.getClass().getName())
//            log.debug("new item: " + createItem + " class: " + createItem.getClass().getName())

            result = itemService.updateItem(user, updateItem)

            if(result.isOK()){
                def response

                def itemInstance = result.object

                if(itemInstance){
                    response = result.object.asDeepXML()
                    log.info "item sucessfully updated: " + response
                } else {
                    response = "Missing item instance in Result object."
                    log.info response
                    log.info "result object is " + result.object
                }
                render response
            } else {
                log.info "Updating item failed: " + result.message
                render result.message
            }
        } else {
            response.status = 500
            render "Could not update Item. Request body in Method PUT is empty."
        }
    }

    /**
     * REST Function: deleteItem
     */
    def deleteItemRest = {
        println "call action: deleteItemRest"
        if (params.id) {
            User user = authService.getUser()
            Result result = itemService.deleteItem(user, new Long(params.id));
            if(result.isOK()){
                response.status = 200
                // TODO: sonstige relevante informationen mitschicken? z.b. childCount?
                render params.id
            } else {
                render result.message
            }
        }
        else {
            response.status = 400 //Bad Request
            render """DELETE request must include the Item Id
                 Example: /itemservice/id
       """
        }
    }

    /**
     * REST Function: getItems
     */
    def getItemsRest = { email, passwd ->

        println "call action: getItemsRest"
        def user = authService.checkAuthentication(email, passwd)
        println "User authenticated: " + user
        response.contentType = "xml"
        def items = (user!=null)? itemService.getAllItemsXML(user) : new ArrayList<Item>()
        render items
        
        

    }


    /**
     * REST Function: getTags
     */
    def getTagsRest = {
        log.info "REST: getTagsRest"
        log.info "request is " + request
        println "call action: getTagsRest"

        if (params.id) {
            def item = Item.get(params.id)
            if(item){
                render itemService.getTagsXML(item)
            } else {
                response.status = 404 //Not found
                render """GET request has not found item with ID ${params.id}"""
            }
        } else {
            response.status = 400 //Bad Request
            render """GET request must include the Item Id
                 Example: /item/\$id/tags
       """
        }
    }

    /**
     * REST Function: assignTagToItem
     */
    def assignTagToItemRest = {
        println "call action: assignTagToItem"

        if(params.id && params.tagid){
            def item = Item.get(params.id)
            if(item){
                def tag = Item.get(params.tagid)
                if(tag){
                    try {
                        item.addToTags(tag)
                        println "Tag ${params.tagid} sucessfully assigned to Item ${params.id}"
                        response.status = 200 //ok
                        render "Tag ${params.tagid} sucessfully assigned to Item ${params.id}"
                    } catch(Exception ex){
                        println ex
                        response.status = 500 //Internal Server Error
                        render "Error in assigning Tag ${params.tagid} to Item ${params.id}"
                    }

                } else {
                    response.status = 404 //Not found
                    render """PUT request has not found item with ID ${params.id}"""
                }
            }  else {
                response.status = 404 //Not found
                render """PUT request has not found item with ID ${params.id}"""
            }
        } else {
            response.status = 400 //Bad request
            render """PUT request must include the Id of the tag to be assigned
                 Example: /item/\$id/tags/\$tagid
       """
        }


    }

    /**
     * REST Function: removeTagFromItem
     */
    def removeTagFromItemRest = {
        println "call action: removeTagFromItem"

        if(params.id && params.tagid){
            def itemId = new Long(params.id)
            def tagItemId = new Long (params.tagid)


            Result result = itemService.getItem(authService.getUser(), itemId)
            Result tagItemResult = itemService.getItem(authService.getUser(), tagItemId)
            
            if(result.isOK() && tagItemResult.isOK()){
                def item = result.object
                def tagItem = tagItemResult.object

                //item.removeFromTags(tagItem)
                tagItem.removeFromTags(item);
                
                render item.asDeepXML()
            } else {
                render result.message
            }
        }
    }

    /**
     * REST Function: tagsCount
     */
    def tagsCountRest = {
        println "call action: tagsCountRest"
        if (params.id) {
            Item item = Item.get(params.id)
            if(item){
                render "<item id=\"${item.id}\" tagscount=\"${item.tags.size()}\" />"
            } else {
                response.status = 404 //Not found
                render "GET request: No Item with ID ${} found"
            }

        }
        else {
            response.status = 400 //Bad Request
            render """"GET request must include the Item Id
                 Example: /item/\$id/tags/count
       """
        }

    }

    /**
     * REST Function: getChildren
     */
    def getChildrenRest = {
        println "call action: getChildrenRest"
        if (params.id) {
            def item = Item.get(params.id)
            if(item){
                render getChildrenXML(item)
            } else {
                response.status = 404 //Not found
                render """GET request has not found item with ID ${params.id}"""
            }
        } else {
            response.status = 400 //Bad Request
            render """GET request must include the Item Id
                 Example: /item/\$id/children
       """
        }

    }

    /**
     * REST Function: childrenCount
     */
    def childrenCountRest = {
        println "call action: childrenCountRest"
        if (params.id) {
            Item item = Item.get(params.id)
            if(item){
                render "<item id=\"${item.id}\" childrencount=\"${item.children.size()}\" />"
            } else {
                response.status = 404 //Not found
                render "GET request: No Item with ID ${} found"
            }

        }
        else {
            response.status = 400 //Bad Request
            render """"GET request must include the Item Id
                 Example: /item/\$id/children/count
       """
        }
    }

    /**
     * Authentication ERROR message
     */
    def notAuthenticated = {
        log.info "call action: notAuthenticated"
        response.status = 401 // Unauthorized
        render "<error>user could not be authenticated</error>"

    }



    /**
     * Da für den X-HTTP-Method Override Workaround die POST-Methode verwendet wird, diese aber noch kein Mapping
     * für die REST Tag Methoden hatte, bauen wir hier einfach mal einen Switch ein
     */
     def workaroundRestTagActionHub = {
        String methodOverrideHeader = request.getHeader("X-HTTP-Method-Override")
        if (methodOverrideHeader == "PUT") {
            log.info "method override -> PUT"
            this.assignTagToItemRest()
        }
        else if (methodOverrideHeader == "DELETE") {
            log.info "method override -> DELETE"
            this.removeTagFromItemRest()
        }
        else if (methodOverrideHeader == "GET") {
            log.info "method override -> GET"
            this.getTagsRest()
        }
     }

}