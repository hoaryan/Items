class ItemController {

    // Instanz wird von Grails auomatisch injiziert
    def itemService
    
    // the save and update actions only accept POST requests
    static allowedMethods = [save:'POST', update:'POST']



    def index = { redirect(action:stdview,params:params) }


    def stdview = {
        log.debug "action: stdview"
        log.debug "params: " + params
        Result result = itemService.getItem(session.user,params.id)
        def tmpItem = result.object        
        // Wenn keine ID angegeben wurde, dann muss die HomeItemAction gestartet werden
        if (tmpItem==null || tmpItem.id == session.user.homeItemID) {
             if (flash.message) {flash.message=flash.message} // muss drin sein, sont geht flash message verloren
             redirect(action: homeview)
        }
        else {
            def deletedItems=null
            if(tmpItem.itemType==Item.ITEM_LOGBOOK){
                 result = itemService.getAllDeletedItems(session.user)
                 if(result.isOK()){
                    deletedItems = result.object    
                 } else {
                    log.error result.message
                 }

            }

            // itemDates

//           log.debug "params: " + params
//           log.debug "flash.itemDate: " + flash.itemDate
//
//           flash.itemDate = flash.itemDate
//           log.debug "flash.itemDate instanceof ItemDate: " + (flash.itemDate instanceof ItemDate)
//           log.debug "params.itemDate instanceof ItemDate: " + (params.itemDate instanceof ItemDate)

            // end itemDates -->

            if(flash.itemInstance) {
                log.debug "iteminstance: " + flash.itemInstance
            }
            return [toBeViewedItem: tmpItem, deletedItems: deletedItems, itemInstance:flash.itemInstance, isNew:params.isNew?.toBoolean(), itemDate:flash.itemDate]
        }
    }

    /**
     * @deprecated
     */
    def back = {
        log.debug "action: back"
        // Back concept removed.
        log.error "Back is not supported anymore...."
    }


    def homeview = {
        log.debug "action: homeview"
        Result result = itemService.getHomeItem(session.user)
        def tmpItem = result.object
        def homeItems = tmpItem.children.findAll{ item -> item.itemType==Item.ITEM_NORMAL}
        def itemsToday = itemService.getItemsInRange(session.user, new Date(), new Date(), true).object
        def itemsThisWeek = itemService.getItemsInRange(session.user, DateUtil.getWeekBeginning(new Date()), DateUtil.getWeekEnd(new Date()), true).object
        def itemsThisMonth = itemService.getItemsInRange(session.user, DateUtil.getFirstDayInMonth(new Date()), DateUtil.getLastDayInMonth(new Date()), true).object
        def dueItems = itemService.getDueItems(session.user, new Date()).object
        log.debug "Item in homeview: " + tmpItem
        return [toBeViewedItem: tmpItem, inboxItem: tmpItem?.children.find{ item -> item.itemType==Item.ITEM_INBOX}, logbookItem: tmpItem?.children.find{ item -> item.itemType==Item.ITEM_LOGBOOK}, homeItemSize:homeItems.size(), itemsTodaySize: itemsToday.size(), itemsThisWeekSize: itemsThisWeek.size(), itemsThisMonthSize: itemsThisMonth.size(), dueItemsCount: dueItems.size() ]
    }

     def handleUnchain = {
        if ( itemService.unchainItem(session.user, params.toBeUnchainedItemId, params.id).isOK())  {
            flash.message="removed"
        } else {
            flash.message="Item konnte nicht unchained werden!"
        }        
        redirect(action: stdview, id: params.id)
    }

    def handleMoveToLogbook = {
        log.debug "action: handleMoveToLogbook"
        log.debug "params: " + params
        if (itemService.moveItemToLogbook(session.user, params.toBeMovedItemId).isOK())  {
            flash.message = "deleted"
        } else {
            flash.message = "Item konnte nicht gelöscht werden."
        }
        if(params.toHome && params.toHome=='true'){
            redirect(action: homeview)
        } else if(params.startDate && params.endDate && params.viewType){
            redirect(controller:"itemDate", action:"calendarlist", params:[startDate:params.startDate, endDate:params.endDate, viewType:params.viewType])
        } else {
            redirect(action: stdview, id: params.id)
        }
    }

    def handleDeleteList = {
        if (itemService.deleteList(session.user, params.id, params.deleteFrom).isOK())  {
            flash.message = "deleted"
        } else {
            flash.message = "Liste konnte nicht gelöscht werden."
        }
        redirect(action: stdview, id: params.id)
    }

    def handleRestore = {
        if (itemService.restoreItem(session.user, params.toBeRestoredItemId).isOK())  {
            flash.message = "restored"
        } else {
            flash.message = "Item konnte nicht wiederhergestellt werden."
        }
        redirect(action: stdview, id: params.id)
    }


    def handleDelete = {
        if (itemService.deleteItem(session.user, params.toBeDeletedItemId).isOK())  {
            flash.message = "deleted"
        } else {
            flash.message = "Item konnte nicht gelöscht werden."
        }
        redirect(action: stdview, id: params.id)
    }


    def addTags = {
        log.debug "action: addTags"
        log.debug "params: " + params

        // TODO: Keine Ahnung!! muss drinnen sein, weil sonst die home.children liste fehlerhaft ist x(
         Result homeResult = itemService.getHomeItem(session.user)

//        if(homeResult.isOK()){
//            Item home = homeResult.object
//            log.debug "addTags home item: " + home + " children: " + home.children
//        }


         Result result = itemService.getItem(session.user, params.id)
         if (result.isOK()) {
             def itemInstance = result.object
//             log.debug "edit Tags von : " + itemInstance
             List untagOptions = itemInstance?.tags;
//             log.debug session.user.homeItemID
             Result resultHomeTagOptions = itemService.getHomeTagOptions(session.user, params.id)
             if (resultHomeTagOptions.isOK()) {
                 List homeTagOptions = resultHomeTagOptions.object
                 return [itemInstance : itemInstance, untagOptions: untagOptions, homeTagOptions : homeTagOptions ]
             }
         }
         flash.message = "Fehler: " + result.message
         redirect(action:stdview)
     }

    def handleAddTag = {
          Result result = itemService.addTagToItem(session.user, params.id, params.tagItemId)

          if (result.isOK()) {
                flash.message = "tag added"
          } else {
                flash.message = "Beim Hinzufügen des Tag ist ein Fehler aufgetreten: " + result.message
          }
          redirect(action: addTags, id: params.id)
    }

    def handleRemoveTag = {
          Result result = itemService.unchainItem(session.user, params.id, params.untagItemId)

          if (result.isOK()) {
                flash.message = "tag removed"
          } else {
                flash.message = "Fehler: " + result.message
          }
          redirect(action: addTags, id: params.id)
    }



    // TODO   UNBEDINGT verbessern!!!! <- bei jedem query werden zueerst ALLE Items geholt!!!!! und dann gesucht :(
     def searchTags = {

        log.debug "action: searchTags"
        log.debug "RemoteField Search on Item mit ID: " + params.id + " und mit searchString: " + params.searchString

        def searchString = params.searchString ?: null

        Result result = itemService.getItem(session.user, params.id)
        def itemInstance = result.object
        if (searchString) {
            def searchResults = Item.withCriteria {
                and {
                    eq('user', session.user)
                    eq('deleted', false)
                    ilike('name', '%'+ searchString+'%')
                }
             }
             log.debug "Results: " + searchResults
             if (searchResults) {
                 render(template:"tag_searchresults", model:[itemInstance : itemInstance, tagOptions : searchResults ] )
             } else {
                 render('<div class="tagcheckboxlist"><div class="listinfo">No tag items found...</div></div>')
             }
        } else {
        render ('') // Render nothing sonst exception
        }



//            log.debug "action: searchTags"
//            log.debug "RemoteField Search on Item mit ID: " + params.id + " und mit searchString: " + params.searchString
//            def searchString = params.searchString ?: null
//            Result result = itemService.getItem(session.user, params.id)
//            Result resultTagOptions = itemService.getMoreTagOptions(session.user, params.id)
//
//            if (result.isOK() && resultTagOptions.isOK()) {
//                 def itemInstance = result.object
//                 List untagOptions = itemInstance?.tags;
//                 List tagOptions = resultTagOptions.object
//
//                 if (searchString) {
//                     def searchResults = []
//                     searchString = searchString.toLowerCase()
//                     tagOptions.each { if ((it.name.toLowerCase()).find(searchString)) searchResults.add(it)}
//
//                     log.debug "Results: " + searchResults
//                     render(template:"tag_searchresults", model:[itemInstance : itemInstance, tagOptions : searchResults ] )
//                     // Nur anzeigen, dass keine Ergebnisse gefunden wurden, falls es überhaupt suchergebnisse gibt und auch nur dann, wenn es überhaupt potentielle tags zum hinzufügen gibt
//                     if (!searchResults && tagOptions.size() != 0) render('<div class="tagcheckboxlist"><div class="listinfo">No tag items found...</div></div>')
//                 }
//                 else { // Wenn kein Suchstring eingegeben wurde einfach normal anzeigen
//                        render ""
//                 }
//             }
//             else {
//                 flash.message = "Fehler: " + result.message
//             }


     }


     


    def search = {
        log.debug "action: search"
        log.debug "RemoteField Search on Item mit ID: " + params.id + " und mit searchString: " + params.searchString
        Result result = itemService.getItem(session.user,params.id)
        def tmpItem = result.object
        def searchString = params.searchString ?: null
        //      Wenn im Homeitem gesucht wird
        if (tmpItem.id == session.user.homeItemID) {        
             if (searchString) {
                 def searchResults = Item.withCriteria {
                     and {
                         eq('user', session.user)
                         ilike('name', '%'+ searchString+'%')
                         eq('deleted', false)
                     }
                 }
                 log.debug "Results: " + searchResults
                 if (searchResults) {
                      render('<div class="groupseparator">Search Items</div>')
                      render(template:"../item/childItem", var:"child", collection:searchResults, model:[toBeViewedItem: tmpItem] )
                 } else {
                      render('<div class="groupseparator">Search Items</div>')
                      render ('<div class="listinfo">No matching items found...</div>')
                 }
             } else {
                render ('') // Render nothing sonst exception
             }
        //      Wenn im Logbook gesucht wird
        } else if (tmpItem.id == session.user.logbookItemID) {
             
             def searchResults = searchString?
             Item.withCriteria {
                 and {
                     eq('user', session.user)
                     ilike('name', '%'+ searchString+'%')
                     eq('deleted', true)
                 }
             } :
             Item.withCriteria {
                 and {
                     eq('user', session.user)
                     eq('deleted', true)
                 }
             } ;
             log.debug "Results: " + searchResults
             if (searchResults) {
                  render('<div class="groupseparator">Search Items</div>')
                  render(template:"../item/childItem", var:"child", collection:searchResults, model:[toBeViewedItem: tmpItem] )
             } else {
                  render('<div class="groupseparator">Search Items</div>')
                  render ('<div class="listinfo">No matching items found...</div>')
             }
        } else { //      Wenn in Stdview gesucht wird
            if (searchString) {
                 // Statt einer komplizierten Abfrage, denke ich ist es hier einfacher nur nach den Strings der Kinder zu Suchen
                 def searchResults = []
                 searchString = searchString.toLowerCase()
                 tmpItem?.children.each { if ((it.name.toLowerCase()).find(searchString)) searchResults.add(it)}

                 log.debug "Results: " + searchResults
                 if (searchResults) {
                      render(template:"../item/childItem", var:"child", collection:searchResults, model:[toBeViewedItem: tmpItem] )
                 } else {
                      render ('<div class="listinfo">No matching items found...</div>')
                 }
            } else { // Wenn kein Suchstring eingegeben wurde einfach nur das Item mit allen Kindern anzeigen

                if (tmpItem?.children) {
                    render(template:"../item/childItem", var:"child", collection:tmpItem?.children, model:[toBeViewedItem: tmpItem] )
                } else {
                    render ('<div class="listinfo">No items to display in this list...</div>')
                }
                
            }
        }

    }

    def edit = {
        def result = itemService.getItem(session.user, params.id)
        if (result.isOK())
        {
            def itemInstance = result.object
            return [itemInstance:itemInstance]
        } else {
            flash.message = "Item not found with id ${params.id}"
            redirect(controller:'item', action:'stdview', id:params.id)
            return
        }
    }

    def handleEdit = {

        log.debug "action: handleEdit"
        def result = new Result()
        
//        log.debug "params: " + params
//        log.debug "params?.startDate: " + params?.startDate_day + "." + params?.startDate_month + "." + params?.startDate_year + " " + params?.starttime_hour + ":" + params?.starttime_minute
//        log.debug "params?.endDate: " + params?.endDate_day + "." + params?.endDate_month + "." + params?.endDate_year + " " + params?.endtime_hour + ":" + params?.endtime_minute
//        log.debug "params?.starttime: " + params?.starttime_hour + ":" + params?.starttime_minute
//        log.debug "params?.endtime: " + params?.endtime_hour + ":" + params?.endtime_minute
//        log.debug "params?.zeitspanne: " + params?.zeitspanne
//        log.debug "params?.ganztaegig: " + params?.ganztaegig


        if(!params.id){
            flash.message "ERROR: params.id is missing"
            render(view:'edit', model:[itemInstance:itemInstance])
        }

        int itemId = null
        try {
            itemId = params.id? Integer.valueOf(params.id) : null
        } catch(NumberFormatException ex) { itemId = null }
       
        def name = params.name
        def notes = params.notes
        
        log.debug "New Data:  itemId: " + itemId + "  name: " + name + "  notes: " + notes

        result = itemService.updateItem(session.user, itemId, name, notes)

        if (result.isOK()) {                        
            redirect(action:stdview, id:result.object.id)
        } else {             
             def itemInstance = result.object
             if (result.code==Result.VERSION_ERROR) {
                log.warn "VERSION_ERROR on item: " + itemInstance
                itemInstance.errors.rejectValue("version", "item.optimistic.locking.failure", "Item wurde zwischenzeitlich verändert!")
             } else if (result.code==Result.VALIDATION_ERROR) {
                log.warn "VALIDATION ERRORS: " + itemInstance.errors
                render(view:'edit', model:[itemInstance:itemInstance])
                return
             }
//             log.debug itemInstance
//             log.debug "Item Error: " + result.code + " message: " + result.message
             if(itemInstance) {
                 log.debug itemInstance
             }
             render(view:'edit', model:[itemInstance:itemInstance])
             return
        }
      }

    def create = {
        // hier muss nicht die Service Klasse verwendet werden, da hier nichts mit der Datenbank gemacht wird.
        // Es wird lediglich ein erzeugt, damit die view dargestllt werden kann
        // das properties = params dient dazu, dass nichts an eingegebnene Daten verloren geht.
        def itemInstance = new Item()
        itemInstance.properties = params
        return ['itemInstance':itemInstance, parentId:params.id]
    }

    def handleCreateAndRedir = {
        log.debug "action: handleCreateAndRedir"
        params.redirect = "tagview"
        flash.params = params        
        redirect(action:handleCreate)
    }

    def handleCreate = {

        log.debug "action: handleCreate"

        def atts = flash.params? flash.params : params

        def itemInstance = new Item(atts)
        itemInstance.properties = atts
        log.debug "Form values: " + atts
        log.debug "flash.params values: " + flash.params
        // Auch wenn der User bei params schon mit drin steckt wenn es aus der GSP kommt, ist es besser, wenn der hier nochmal explizit zugewiesen wird
        itemInstance.user = session.user

        def result = itemService.createItem(session.user, atts.parentId.toLong(), itemInstance)
        itemInstance = result.object
        
        def dateExist = atts.existDate=="true"? true : false;
        log.debug "dateExist: " + dateExist
        def itemCreatedOK = false;

        def newItem=null
        if(result.isOK()) {
            itemCreatedOK = true;
            newItem = result.object
            log.debug "ID des neuen Items " + newItem.name + ":  " + newItem?.id
            if(!dateExist){
                flash.message = "updated"
                
                log.debug "Sucessfully created new Item and Date: " + itemInstance
                flash.message = "created"
                if(atts.redirect){
                    if(atts.redirect=="tagview"){// jump to edittag after create
                        redirect(action:addTags, id:itemInstance.id)
                    }
                } else {
                    redirect(action:stdview, id:atts.parentId)
                }
            }
            
        } else {
            log.warn result.message
            flash.message = result.message
            flash.itemInstance = itemInstance
            redirect(action:stdview, id:atts.parentId)
        }

        // handle date if one exist
        if(itemCreatedOK && dateExist){
              result = new Result()

//            log.debug "params: " + params
//            log.debug "params?.__itemId: " + params?.__itemId
//            log.debug "params?.__isNew: " + params?.__isNew
//            log.debug "params?.__id: " + params?.__id
//            log.debug "params?.startDate: " + params?.startDate_day + "." + params?.startDate_month + "." + params?.startDate_year + " " + params?.starttime_hour + ":" + params?.starttime_minute
//            log.debug "params?.endDate: " + params?.endDate_day + "." + params?.endDate_month + "." + params?.endDate_year + " " + params?.endtime_hour + ":" + params?.endtime_minute
//            log.debug "params?.starttime: " + params?.starttime_hour + ":" + params?.starttime_minute
//            log.debug "params?.endtime: " + params?.endtime_hour + ":" + params?.endtime_minute
//            log.debug "params?.zeitspanne: " + params?.zeitspanne
//            log.debug "params?.timeactive: " + params?.timeactive


            int itemId = null
            try {
                itemId = (Integer)newItem?.id
            } catch(NumberFormatException ex) { itemId = null }

            int dateId = null
            try {
                dateId = atts.__id? Integer.valueOf(atts.__id) : null
            } catch(NumberFormatException ex) { dateId = null }

            boolean isNew = atts.__isNew == 'false'? false : true

            if(itemId==null){
                flash.message = "ERROR: ItemId is missing"
                log.error "ItemId is missing"
                render(view:'editDates', model:[itemInstance:session.user.homeItemID])
            }

            boolean isZeitspanne = atts?.zeitspanne!=null
            boolean isGanztaegig = atts?.timeactive==null

            int repeatType = ItemDate.TYPE_ONE_TIME
            if(atts.repeat_type){
                repeatType = new Integer(atts.repeat_type)
            }
            

            GregorianCalendar startDate = null
            GregorianCalendar endDate = null
            int startHour = 0
            int startMinute = 0
            int endHour = 0
            int endMinute = 0

            if(atts?.starttime_hour && atts?.starttime_minute){
                startMinute = new Integer(atts.starttime_minute)
                startHour = new Integer(atts.starttime_hour)
            }
            if(atts?.endtime_hour && atts?.endtime_minute){
                endMinute = new Integer(atts.endtime_minute)
                endHour = new Integer(atts.endtime_hour)
            }

            if(atts?.startDate_year && atts?.startDate_month && atts?.startDate_day){
                startDate = new GregorianCalendar(atts.startDate_year.toInteger(), atts.startDate_month.toInteger()-1, atts.startDate_day.toInteger() , startHour, startMinute)
            }
            if(params?.endDate_year && params?.endDate_month && params?.endDate_day){
                endDate = new GregorianCalendar(atts.endDate_year.toInteger(), atts.endDate_month.toInteger()-1, atts.endDate_day.toInteger() , endHour, endMinute)
            }

            log.debug "itemId: " + itemId
            log.debug "dateId: " + dateId
            log.debug "startDate: " + ((startDate!=null)?startDate.getTime():startDate) + "  startTime: " + startHour + ":" + startMinute
            log.debug "endDate: " + ((endDate!=null)?endDate.getTime():endDate) + "  endTime: " + endHour + ":" + endMinute
            log.debug "isGanztaegig: " + isGanztaegig
            log.debug "isZeitspanne: " + isZeitspanne
            log.debug "isNew: " + isNew
            log.debug "repeatType: " + repeatType

            result = itemService.updateItemDate(session.user, itemId, dateId, startDate, endDate, repeatType, isGanztaegig, isZeitspanne, isNew)

            if (result.isOK()) {
                flash.message = "updated"

                log.debug "Sucessfully created new Item and Date: " + itemInstance
                flash.message = "created"
                if(atts.redirect){
                    if(atts.redirect=="tagview"){// jump to edittag after create
                        redirect(action:addTags, id:itemInstance.id)
                    }
                } else {
                    redirect(action:stdview, id:atts.parentId)
                }

//                redirect(controller: "item", action: "stdview", id:itemId)
            } else {
                log.warn result.message
                flash.message = result.message
                flash.itemInstance = itemInstance
                redirect(action:stdview, id:atts.parentId) // TODO
//                 def itemDate = result.object
//                 if (result.code==Result.VERSION_ERROR) {
//                    log.warn "VERSION_ERROR on item: " + itemInstance
//                    itemDate.ownerItem.errors.rejectValue("version", "item.optimistic.locking.failure", "Item wurde zwischenzeitlich verändert!")
//                } else if (result.code==Result.VALIDATION_ERROR) {
//                    flash.message = null
//                    log.debug result.message
//                 }
//                 log.debug "Item Error: " + result.code + " message: " + result.message
//
//                 flash.itemDate = itemDate
//                 redirect(controller: "item", action: "stdview", params:[id:itemId, isNew:isNew, itemDate:itemDate])
//                 return
            }
        }


    }

    def createInbox = {

        log.debug "action: createInbox"
        def itemInstance = new Item()      
        itemInstance.user = session.user
        
        flash.itemInstance = itemInstance
        redirect(action:stdview, id:session.user.inboxItemID)
    }

    def toolTipLoader = {
        def tooltip = ""
        if(params?.message){
            tooltip = itemService.getTooltipText(params.message).object            
        } else {
            tooltip = "error: tag is missing..."
        }
//        log.debug "Tooltip for tag \"" + params?.message + "\": " + tooltip
        render(tooltip)
    }

    




  



 














  def testSomething = {
        def string = "<br>TEST SOMETHING<br>"
   
        render(string)
      }



  def loadHSQLDBManager = {
        def toBeRenderedString = "------------------- LADEN VON HSQL DB MANAGER -----------------------<br><br>"
        toBeRenderedString += "Zum Laden des HSQL DB Manager muss die URL auf jdbc:hsqldb:mem:devDB abgeändert werden!"+"<br><br>"
        toBeRenderedString += "Beenden des HSQL DB Managers führt zur Beendigung des Grails Servers"
        render(toBeRenderedString)
        org.hsqldb.util.DatabaseManager.main()
      }


}
