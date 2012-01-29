import grails.converters.*


class ItemService {

    // Service Klassen sind per default SINGLETON
    boolean transactional = true

    Result getItem(User user, def itemID)  {
        if (itemID !=null) {
            def itemInstance = Item.findByUserAndId(user, itemID)
            if (itemInstance) {
                return new Result(code:Result.OK, object:itemInstance)
            }
            else {
                return new Result(code:Result.NOTFOUND_ERROR, message: "Could not find item with ID " + itemID)
            }
        }
        return new Result(code:Result.GENERAL_ERROR, message: "Could not find item. Reason: ID not set")
    }


    Result getHomeItem(User user)  {
        def itemInstance = Item.findByUserAndId(user, user.homeItemID)
//        log.debug "HOMEITEM: " + itemInstance + " children: " + itemInstance.children
        if (itemInstance) {
            return new Result(code:Result.OK, object:itemInstance)
        }
        else {
            return new Result(code:Result.NOTFOUND_ERROR)
        }
    }

    Result getAllUserItems(User user) {
       List<Item> itemList = Item.findAllByUser(user)
       if (itemList) {
            return new Result(code:Result.OK, object:itemList)
        }
        else {
            return new Result(code:Result.NOTFOUND_ERROR)
        }
    }

    Result getAllDeletedItems(User user) {
       List<Item> itemList = Item.findAllByUserAndDeleted(user, true)
       if (itemList!=null) {

            // sortieren nach Löschdatum
            if(itemList){
                itemList = itemList.sort { a,b ->
//                    if(a && b){
//                        if(!a.lastUpdated) {
//                            return -1
//                        } else if(!b.lastUpdated) {
//                            return 1
//                        } else {
                            a.lastUpdated <=> b.lastUpdated
//                        }
//                    }
//                    return 0

                }
                itemList.reverse()
            }

            return new Result(code:Result.OK, object:itemList)
        } else {
            return new Result(code:Result.NOTFOUND_ERROR)
        }
    }

    Result getItemsByIds(User user, def toBeRetrievedItemIdsArray) {
        // TODO: User sicherhiet noch einbauen
        // irgendwas mit AndUser
        List<Item> itemList = Item.getAll(toBeRetrievedItemIdsArray)
        if (itemList) {
            return new Result(code:Result.OK, object:itemList)
        }
        else {
            return new Result(code:Result.NOTFOUND_ERROR)
        }
    }
    
    Result unchainItem(User user, def toBeUnchainedItemId, def toBeRemovedTagId) {
        def toBeRemovedTag = getItem(user,toBeRemovedTagId).object
        def toBeUnchainedItem = getItem(user,toBeUnchainedItemId).object

        if (toBeRemovedTag && toBeUnchainedItem) {
            if (toBeUnchainedItem.removeFromTags(toBeRemovedTag)) {
                log.debug "Item unchained from List " + toBeRemovedTag.name + ": " + toBeUnchainedItem.name
                return new Result(code:Result.OK)
            }
            else {
                if (!(toBeUnchainedItem?.isUnchainable)) {
                    return new Result(code:Result.LAST_TAG_ERROR)
                }
                else { 
                    return new Result(code:Result.UNCHAIN_ERROR)
                }
            }
        }
        else
        {            
              return new Result(code:Result.NOTFOUND_ERROR)
        }
    }


    Result deleteItem(User user, def itemId) {

        def result = getItem(user, itemId)
        if(!result.isOK()){
            log.debug result.message
            return result
        }
        def toBeDeletedItem = result.object
//        log.debug user
//        log.debug itemId
//        log.debug toBeDeletedItem
//        log.debug toBeDeletedItem?.isDeletable
        if(toBeDeletedItem && toBeDeletedItem.isDeletable) {
            try {
                    Set parentItems = toBeDeletedItem?.tags
                    // Das "refresh()" in der folgenden Iteration ist super wichtig da sonst totatler Mist passiert.
                    // Ist notwendig wenn in der Item Klasse das refresch bei der getTags() funktion auskommentiert ist. Was sinnvoll
                    // ist, da sonst unnnötig viel auf die Datenbank zugegriffen wird.                    
                    parentItems.each {
                        it.refresh()
                        it.removeFromChildren(toBeDeletedItem)
                    }
                    toBeDeletedItem.delete(flush:true) //liefert auch bei erfolgreichem Löschen nur "null" zurück
                    log.debug "item deleted"
                    return new Result(code:Result.OK)
            } catch(org.springframework.dao.DataIntegrityViolationException e) {
                  return new Result(code:Result.DELETE_ERROR, message: "e.getMessage()")
            }
        } else {
            return new Result(code:Result.NOTFOUND_ERROR, message: "Item not found or is not deletable.")
        }        
    }

    /**
     * Trys to clear all Items in a List by unchaining/movingToLogbook Items.
     * Items which can't be unchained/movedToLogbook will be kept.
     *
     * <b>! Don't mix it up with deleting from logbook !</b>
     **/
    Result deleteList(User user, def itemId, String deleteFrom) {
        Result result = getItem(user, itemId)
        
        if(result.isOK()){
            def item = result.object
            List listIds = item.children*.id
            listIds.each { id ->
                Item child = getItem(user, id).object// workaround wegen concurrentmodificationexception

                if(child.isDeletable && !child.isUnchainable){                    
                    Result tmpResult = moveItemToLogbook(user, child.id)
                    if(!tmpResult.isOK()){
                        log.warn tmpResult
                        return new RuntimeException()
                    }
                } else if(!child.isDeletable && child.isUnchainable){                    
                    Result tmpResult = unchainItem(user, child.id, itemId)
                    if(!tmpResult.isOK()){
                        log.warn tmpResult
                        return new RuntimeException()
                    }
                } else if(child.isDeletable && child.isUnchainable){
                    Result tmpResult = null                    
                    if(deleteFrom==Item.REMOVE_FROM_ALL_LISTS){
                        tmpResult = moveItemToLogbook(user, child.id)
                    } else if(deleteFrom==Item.REMOVE_FROM_THIS_LIST){
                        tmpResult = unchainItem(user, child.id, itemId)
                    }
                    if(!tmpResult?.isOK()){
                        log.warn tmpResult
                        return new RuntimeException()
                    }
                }

            }
        } else {
            return new Result(code:Result.NOTFOUND_ERROR, message: "Item not found.")
        }
        return new Result()
    }


    Result moveItemToLogbook(User user, def itemId) {

        def result = getItem(user, itemId)
        if(!result.isOK()){
            log.debug result.message
            return result
        }
        def toBeLoggedItem = result.object
//        log.debug user
//        log.debug itemId
//        log.debug toBeLoggedItem
//        log.debug toBeLoggedItem?.deleted
        if(toBeLoggedItem && toBeLoggedItem.isDeletable) {
            try {
                    Set parentItems = toBeLoggedItem?.tags
                    // Das "refresh()" in der folgenden Iteration ist super wichtig da sonst totatler Mist passiert.
                    // Ist notwendig wenn in der Item Klasse das refresch bei der getTags() funktion auskommentiert ist. Was sinnvoll
                    // ist, da sonst unnnötig viel auf die Datenbank zugegriffen wird.
                    parentItems.each {
                        it.refresh()
                        it.removeFromChildren(toBeLoggedItem)
                    }
                    toBeLoggedItem.deleted = true
                    toBeLoggedItem.merge(flush:true)
                    log.debug "Item moved to logbook: " + toBeLoggedItem.name
                    return new Result(code:Result.OK)
            } catch(org.springframework.dao.DataIntegrityViolationException e) {
                  return new Result(code:Result.DELETE_ERROR, message: "e.getMessage()")
            }
        } else {
            return new Result(code:Result.NOTFOUND_ERROR, message: "Item not found or is not deletable.")
        }
    }
    
    Result restoreItem(User user, def itemId) {
        
        // TODO

        // move to inbox
        Result result = addTagToItem(user, itemId, user.inboxItemID)

        Item item = getItem(user, itemId).object
        item.deleted = false
        item.merge(flush:true)

        return result
           
    }


    /**
     * Only HomeItems in addTags view
     */
    Result getHomeTagOptions(User user, def toBeTaggedItemId) {

         Result result = getItem(user,toBeTaggedItemId)
         if (result.isOK()) {
             def itemInstance = result.object
             List untagOptions = itemInstance.tags;             
             //TODO: Folgende Logik als Datenbank-Abfrage realisieren
             Item home = getItem(user,user.homeItemID).object
             
             Item inbox = getItem(user, user.inboxItemID).object

             List tagOptions = home?.children.findAll{ item -> item?.itemType==Item.ITEM_NORMAL}
             
             untagOptions.each {tagOptions.remove(it)} // alle items aus der tagOption Liste entfernen, die schon tags sind
             
             tagOptions.remove(itemInstance) // topItem aus der Liste entfernen
             
             itemInstance?.children?.each { tagOptions.remove(it) } // alle eigenen Kinder sollen nicht als Tags zur verfügung stehen (direkter Ringschluss) und werden daher aus der Liste entfernt
             

//            nach tag count sortieren
             log.debug tagOptions
             if(tagOptions){
                tagOptions = tagOptions.sort { a,b ->
                    if(a && b){
                        if(!a.tags) {
                            return -1
                        } else if(!b.tags) {
                            return 1
                        } else {
                            a.tags.size() <=> b.tags.size()
                        }
                    }
                    return 0

                }
             }


             tagOptions = tagOptions.reverse()

             // special Items am Anfang der Liste - falls nicht in untag Liste -
             List tempColl = []
             boolean hasHome, hasInbox = false
             untagOptions.each(){
                 if(home.id==it.id){
                     hasHome=true
                 }
                 if(inbox.id==it.id){
                     hasInbox=true
                 }
             }
             if(!hasHome) tempColl += home
             if(!hasInbox) tempColl += inbox
             tempColl += tagOptions
             tagOptions = tempColl

             return new Result(code:Result.OK, object:tagOptions)
         }
         else {
             return new Result(code:result.code)
         }
//        return new Result(code:Result.OK, object:home.children)
    }

    /**
     * When user clicks on "more items" in addTag view
     */
    Result getMoreTagOptions(User user, def toBeTaggedItemId) {
         Result result = getItem(user,toBeTaggedItemId)
         if (result.isOK()) {
             def itemInstance = result.object
             List untagOptions = itemInstance.tags;
             //TODO: Folgende Logik als Datenbank-Abfrage realisieren
             List tagOptions = getAllUserItems(user).object
             untagOptions.each {tagOptions.remove(it)} // alle items aus der tagOption Liste entfernen, die schon tags sind
             Item home = getHomeItem(user).object
             home.children.each {tagOptions.remove(it)}// alle home items aus Liste löschen
             tagOptions.remove(itemInstance) // topItem aus der Liste entfernen
             itemInstance?.children?.each { tagOptions.remove(it) } // alle eigenen Kinder sollen nicht als Tags zur verfügung stehen (direkter Ringschluss) und werden daher aus der Liste entfernt
             def logbookItem = getItem(user, user.logbookItemID).object
             tagOptions.remove(logbookItem)

//            nach tag count sortieren
             tagOptions = tagOptions.sort { a,b -> a.tags.size() <=> b.tags.size() }
             tagOptions = tagOptions.reverse()

             return new Result(code:Result.OK, object:tagOptions)
         }
         else {
             return new Result(code:result.code)
         }
    }


    Result addTagToItem(User user, def toBeTaggedItemId, def toBeAddedTagId) {
         def itemInstance = getItem(user, toBeTaggedItemId).object
         def tagItemInstance = getItem(user, toBeAddedTagId).object
         if (itemInstance && tagItemInstance) {
            if (itemInstance.addToTags(tagItemInstance)) {
                return new Result(code:Result.OK)
            }
            else {
                return new Result(code:Result.TAGGING_ERROR)
            }
         }
         else {
             return new Result(code:Result.NOTFOUND_ERROR)
         }
    }


    Result addTagsToItem(User user, def toBeTaggedItemId, def toBeAddedTagIdsArray) {
        def toBeTaggedItem = getItem(user,toBeTaggedItemId).object
        List<Item> toBeAddedTags = getItemsByIds(user, toBeAddedTagIdsArray).object
        if (toBeTaggedItem.addToTags(toBeAddedTags)) {
            return new Result(code:Result.OK)
         }
          else {
            return new Result(code:Result.TAGGING_ERROR)
         }
    }

    Result updateItem(User user, Integer itemId, String name, String notes){        
        
        // Wenn kein User übergeben wurde
        if (user==null) {
            return new Result(code:Result.NOUSER_ERROR)
        }

        Result result = getItem(user, itemId)
        def itemInstance = null

        if(result.isOK()){
            itemInstance = result.object
        } else {
            log.error "Item with ID ${itemId}} not found. Cancel update..."
            return result
        }

        if (name?.size()>0) {
            itemInstance.name=name
        } else {
            itemInstance.name=""
        }

        if (notes?.size()>0) {
            itemInstance.notes=notes
        } else {
            itemInstance.notes=""
        }
        itemInstance.user = user

        if(!itemInstance.validate()){
            return new Result(code:Result.VALIDATION_ERROR, object:itemInstance)
        }

        if(itemInstance.save(flush:true) && !itemInstance.hasErrors()) {
            log.debug("Item mit der ID ${itemInstance.id} wurde geupdated. Data: " + itemInstance)
            return new Result(code:Result.OK, object:itemInstance, message: "Item ${itemInstance.name} mit der ID ${itemInstance.id} geupdated")
        } else {
            itemInstance.discard()
            log.warn "Item has errors: " + itemInstance.errors
            return new Result(code:Result.GENERAL_ERROR, object:itemInstance, message: itemInstance.errors)
        }

    }

    Result updateItemDate(User user, Integer itemId, Integer dateId, GregorianCalendar startDate, GregorianCalendar endDate, int repeatType, boolean ganztaegig, boolean zeitspanne, boolean isNew) {

        log.debug "updateItemDate"
        log.debug "repeatType: " + repeatType
        // Wenn kein User übergeben wurde
        if (user==null) {
            return new Result(code:Result.NOUSER_ERROR)
        }

        Result result = getItem(user, itemId)
        def itemInstance = null
        
        if(result.isOK()){            
            itemInstance = result.object            
        } else {
            log.error "Item with ID ${itemId}} not found. Cancel updateItemDate..."
            return result
        }

        log.debug "==========  start handle Dates  ======= "
        if (itemInstance!=null){

            if(!isNew){// check if dateItem belongsTo Item
                List<ItemDate> list_dates = ItemDate.findAllByOwnerItem(itemInstance)
//                log.debug "dates before removing: " + list_dates
                def itemDateBelongsToItem = false
                list_dates.each {
                    if(it.id == dateId){
                        itemDateBelongsToItem = true
                    }
                }
                if(!itemDateBelongsToItem){
                    def error_message = "updateItemDate: ItemDate with ID ${dateId} does NOT belong to Item  ${itemInstance} (${itemId})"
                    log.debug error_message
                    return new Result(code:Result.GENERAL_ERROR, object:null, message: error_message)
                }

            }
           
//            log.debug "itemInstance:" + itemInstance

            if (startDate!=null) {
                if(ganztaegig){


                    startDate.set(Calendar.MINUTE, 0)
                    startDate.set(Calendar.HOUR, 0)
                    startDate.set(Calendar.HOUR_OF_DAY, 0)
                    startDate.set(Calendar.SECOND, 0)
//                    log.debug "startDate: " + startDate
                    
                    if(endDate!=null && zeitspanne){
                        endDate.set(Calendar.HOUR, 0)
                        endDate.set(Calendar.MINUTE, 0)
                        endDate.set(Calendar.SECOND, 0)
                    }
                }
            }

            ItemDate itemDate = null;
            if(isNew) {
                itemDate = new ItemDate()
            } else {
                itemDate = ItemDate.get(dateId)
                if(itemDate.hasErrors()){
                    def error_message = "Could not find ItemDate with ID ${dateId}"
                    log.debug error_message
                    return new Result(code:Result.NOTFOUND_ERROR, object:null, message: error_message)
                }
            }
            itemDate.isGanztaegig = ganztaegig
            itemDate.repeatType = repeatType

            itemDate.startDate = startDate!=null? startDate.getTime():null;
            if (!zeitspanne) {
                itemDate.endDate = null
            } else {
                if(endDate!=null){
                    itemDate.endDate = endDate.getTime()
                } else {
                    log.error "Parameter endDate is missing, switching to single date..."
                    itemDate.endDate = null
                }
            }

            if(!itemDate.validate()){
                log.debug "Validation failed: itemDate: " + itemDate.getFullInfo()
                return new Result(code:Result.VALIDATION_ERROR, object:itemDate)
            }

            if(isNew) {
                itemInstance.addToItemDates(itemDate)
            }

            if(!itemInstance.validate()){
                log.debug "Validation failed: itemInstance: " + itemInstance
                return new Result(code:Result.VALIDATION_ERROR, object:itemDate)
            }

            if(itemInstance.save(flush:true) && !itemInstance.hasErrors()) {
                log.debug("ItemDate mit der ID ${dateId} wurde ${isNew? 'erstellt':'geupdated'}. Data: " + itemDate.id + " " + itemDate)
                return new Result(code:Result.OK, object:itemInstance, message: "Date in Item ${itemInstance.name} mit der ID ${itemInstance.id} ${isNew? 'created':'geupdated'}")
            } else {
                itemInstance.discard()
                log.warn "Item has errors: " + itemInstance.errors
                return new Result(code:Result.GENERAL_ERROR, object:itemInstance, message: itemInstance.errors)
            }
            
        } else {
            result.set(Result.NOTFOUND_ERROR, "Item not found: " + itemId, null)
        }

        result
    }

    Result deleteItemDate(User user, Integer itemId, Integer dateId){
//        log.debug "deleteItemDate"
        // Wenn kein User übergeben wurde
        if (user==null) {
            return new Result(code:Result.NOUSER_ERROR)
        }

        Result result = getItem(user, itemId)
        def itemInstance = null

        if(result.isOK()){
            itemInstance = result.object
        } else {
            log.error "Item with ID ${itemId}} not found. Cancel deleteItemDate..."
            return result
        }
        
        if (itemInstance!=null){

            // check if dateItem belongsTo Item
            List<ItemDate> list_dates = ItemDate.findAllByOwnerItem(itemInstance)
//            log.debug "dates before removing: " + list_dates
            def itemDateBelongsToItem = false
            list_dates.each {
                if(it.id == dateId){
                    itemDateBelongsToItem = true
                }
            }
            if(!itemDateBelongsToItem){
                def error_message = "deleteItemDate: ItemDate with ID ${dateId} does NOT belong to Item  ${itemInstance} (${itemId})"
                log.debug error_message
                return new Result(code:Result.GENERAL_ERROR, object:null, message: error_message)
            }

            ItemDate itemDate = ItemDate.get(dateId)
            if(itemDate.hasErrors()){
                def error_message = "Could not find ItemDate with ID ${dateId}"
                log.debug error_message
                return new Result(code:Result.NOTFOUND_ERROR, object:null, message: error_message)
            }

            itemInstance.removeFromItemDates(itemDate)


            if(!itemInstance.validate()){
                return new Result(code:Result.VALIDATION_ERROR, object:itemInstance)
            }

            if(itemInstance.save(flush:true) && !itemInstance.hasErrors()) {
                log.debug("ItemDate mit der ID ${dateId} wurde gelöscht")
                return new Result(code:Result.OK, object:itemInstance, message: "Datum gelöscht.")
            } else {
                itemInstance.discard()
                log.warn "Item has errors: " + itemInstance.errors
                return new Result(code:Result.GENERAL_ERROR, object:itemInstance, message: itemInstance.errors)
            }
        } else {
            result.set(Result.NOTFOUND_ERROR, "Item not found: " + itemId, null)
        }

        result
    }

    Result createItem(User user, Long parentID, Item itemInstance) {

        // Wenn kein User übergeben wurde
        if (!user) {
            return new Result(code:Result.NOUSER_ERROR)
        }

        def parentItem = null
        if(parentID != null) {
            parentItem = getItem(user, parentID).object
        }
        if(parentID == null || parentItem == null) {
            return new Result(code:Result.GENERAL_ERROR, object: null, message: "Can not create new Item without parent.")
        }

        log.debug("Item created: " + itemInstance)
        if (itemInstance) {

            if(!itemInstance.validate()){// TODO
                def errormessage = ""
                itemInstance.errors.fieldErrors.each {
                    errormessage += it                                        
                }

                log.debug errormessage
                return new Result(code:Result.VALIDATION_ERROR, object:itemInstance, message: errormessage)
            }


            if (itemInstance.notes?.size()==0) { itemInstance.notes=null }
            itemInstance.user=user
            if(itemInstance.save(flush:true) && !itemInstance.hasErrors()) {
                if(parentItem) parentItem.addToChildren(itemInstance).save(flush:true)
                return new Result(code:Result.OK, object:itemInstance, message: "Item ${itemInstance.name} mit der ID ${itemInstance.id} erstellt")
            } else {
                 return new Result(code:Result.GENERAL_ERROR, object:itemInstance, message: itemInstance.errors)
            }
        } else {
            return new Result(code:Result.GENERAL_ERROR, object: null, message: "No item to be created." )
        }
    }

    Result getItemsInRange(User user, int dateItemId, boolean withRepeatDates){

        Date startDate
        Date endDate


        def dateItem = ItemDate.get(dateItemId)
        if (dateItem) {
            startDate = dateItem.startDate.clone(); // new GregorianCalendar(dateItem.startDate.get(Calendar.YEAR),dateItem.startDate.get(Calendar.MONTH),dateItem.startDate.get(Calendar.DAY_OF_MONTH),0,0,0)

            if (dateItem.isPeriod()) {
                endDate = dateItem.endDate.clone();
//                //new GregorianCalendar(dateItem.endDate.get(Calendar.YEAR),dateItem.endDate.get(Calendar.MONTH),dateItem.endDate.get(Calendar.DAY_OF_MONTH),23,59,59)
            } else {
                endDate = dateItem.startDate.clone();
                //new GregorianCalendar(dateItem.startDate.get(Calendar.YEAR),dateItem.startDate.get(Calendar.MONTH),dateItem.startDate.get(Calendar.DAY_OF_MONTH),23,59,59)
            }
        }

        Result result = getItemsInRange(user, startDate, endDate, withRepeatDates)
        result
    }

    Result getItemsInRange(User currUser, Date startDate, Date endDate, boolean withRepeatDates){
        Result result = new Result()
        if(startDate && endDate){
            // workaround hack: weil startDate auf 0:0:0 gesetzt funktioniert nicht
            // Startdatum soll um 00:00:00 Uhr beginnen
            startDate = startDate-1
            startDate.setMinutes(59);
            startDate.setHours(23);
            startDate.setSeconds(59);
            // ENDDATUM soll um 23:59:59 Uhr sein
            endDate.setMinutes(59);
            endDate.setSeconds(59);
            endDate.setHours(23);


//            log.debug "Zeitspanne: ${startDate?.toString()} - ${endDate?.toString()}"
//            log.info "Zeitspanne umfasst " + (endDate-startDate) + " Tage"

            // matching dates not repeating
            def results = Item.withCriteria {// generiert falsche Ergebnisse: doppelte Items mit unterschiedlichen itemDates !?!
                and {
                    eq('deleted', false)
                    user {
                        eq('id', currUser.id)
                    }
                    itemDates {
                        and {
                            le('repeatType', ItemDate.TYPE_ONE_TIME)
                            or {
                                between('startDate', startDate, endDate)
                                between('endDate', startDate, endDate)
                                and {
                                      le('startDate', startDate)
                                      ge('endDate', endDate)
                                }
                            }
                        }
                        order("endDate", "asc")
                    }
                }
            }

            List resultList = []
            List rememberList = []            
            results.each {// workaround für obige fehlerhafte Abfrage                
                if(!rememberList.contains(it.id)){
                    it.refresh()
                    resultList << it
                    rememberList << it.id
                }                
            }

            if(withRepeatDates){
                // matching dates not repeating
                Result resultRepeatDates = getMatchingRepeatItems(currUser, startDate, endDate);
                if(resultRepeatDates.isOK()){
                    resultList.addAll(resultRepeatDates.object)
                } else {
                    log.error "Error in fetching matching Repeat Dates." + resultRepeatDates.message
                }
            }

//            log.debug "Items gefunden: " + results.size() + "    items: " + results
//            log.debug "List merged to: " + resultList.size() + "    items: " + resultList
            result = new Result(object: resultList);
        } else {
            result = new Result(code: Result.GENERAL_ERROR, message:"startDate or endDate is null", null);
        }

        result
    }

    Result getMatchingRepeatItems(User currUser, Date startDate, Date endDate){
        

        Result result = getAllRepeatItems(currUser)
        log.debug "All Repeat Items: " + result.object
        List allRepeatItems = result.object
        
        List resultList = []
        allRepeatItems.each { item ->
            boolean match = DateUtil.repeatItemMatchesDatespan(item, startDate, endDate)
            if(match) {
                resultList << item
            }
        }

        return new Result(code: Result.OK, object: resultList)
    }

    Result getAllRepeatItems(User currUser){
        def results = Item.withCriteria {// generiert falsche Ergebnisse: doppelte Items mit unterschiedlichen itemDates !?!
            and {
                eq('deleted', false)
                user {
                    eq('id', currUser.id)
                }
                itemDates {
                    ge('repeatType', ItemDate.TYPE_EVERY_WEEK)
                    order("endDate", "asc")
                }
            }
        }

        new Result(code: Result.OK, object: results)
    }

    /*
     * Fetches all Items with Dates earlier than today.
     * For Items with timespans the enddate must be at least one day in the past.
     */
    Result getDueItems(User user, Date date){
        List itemsInPast = getItemsInRange(user, date-365, date-1, false).object
        List itemsToday = getItemsInRange(user, date, date, false).object

        // check wheter items has ended at least yesterday
        itemsInPast.removeAll(itemsToday)

        Result result = new Result(code:Result.OK, object:itemsInPast)
    }

    Result getItemDate(int itemDateId){
        def itemDate = ItemDate.get(itemDateId)
        if(!itemDate.hasErrors()){
            new Result(object: itemDate)
        } else {
            new Result(Result.GENERAL_ERROR, "could not find ItemDate with Id: " + itemDateId, null)
        }        
    }
    
    Result getTooltipText(String tag){
        Result result = new Result();
        String message, description;
        if(tag.equals(TooltipConstants.BUTTON_BACK)) {
            message = "Back"
        } else if(tag.equals(TooltipConstants.BUTTON_HOME)) {
            message = "Home"
        } else if(tag.equals(TooltipConstants.BUTTON_NEW_ITEM)) {
            message = "Add new Item"
        } else if(tag.equals(TooltipConstants.BUTTON_UNCHAIN_ITEM)) {
            message = "Unchain Item"
            description = "Removes the Item ONLY from this list"
        } else if(tag.equals(TooltipConstants.BUTTON_DELETE_ITEM)) {
            message = "Check Item"
            description = "stores Item in Logbook"
        } else if(tag.equals(TooltipConstants.BUTTON_REAL_DELETE_ITEM)) {
            message = "Delete Item"
            description = "NOT recoverable !!!"
        } else if(tag.equals(TooltipConstants.BUTTON_DELETE_ITEM_INACTIVE)) {
            message = "Not Deletable"
            description = "Item is not empty"
        } else if(tag.equals(TooltipConstants.BUTTON_DELETE_ALL_ITEMS_ACTIVE)) {
            message = "Check All Items"
            description = "Removes All deletable Items to Logbook"
        } else if(tag.equals(TooltipConstants.BUTTON_DELETE_ALL_ITEMS_INACTIVE)) {
            message = "Items cannot be deleted"
            description = "List is empty or no Items can be deleted"
        } else if(tag.equals(TooltipConstants.BUTTON_RESTORE_ITEM)) {
            message = "Restore Item"
            description = "Item will be recovered and sent to Inbox"
        } else if(tag.equals(TooltipConstants.BUTTON_EDIT_ITEM)) {
            message = "Edit Item"
            description = "Change name and notes"
        } else if(tag.equals(TooltipConstants.BUTTON_EDIT_DATES)) {
            message = "Edit Dates"
            description = "Organize the dates"
        } else if(tag.equals(TooltipConstants.BUTTON_ADD_DATE)) {
            message = "Add new Date"
        } else if(tag.equals(TooltipConstants.BUTTON_EDIT_DATE)) {
            message = "Edit Date"
        } else if(tag.equals(TooltipConstants.BUTTON_DELETE_DATE)) {
            message = "Delete Date"
        } else if(tag.equals(TooltipConstants.BUTTON_EDIT_TAGS)) {
            message = "Edit Tags"
        } else if(tag.equals(TooltipConstants.BUTTON_TODAY)) {
            message = "Show Today"
        } else if(tag.equals(TooltipConstants.BUTTON_THIS_WEEK)) {
            message = "Show current week"
        } else if(tag.equals(TooltipConstants.BUTTON_THIS_MONTH)) {
            message = "Show current month"
        } else if(tag.equals(TooltipConstants.BUTTON_PREVIOUS)) {
            message = "Previous"
        } else if(tag.equals(TooltipConstants.BUTTON_NEXT)) {
            message = "Next"
        } else if(tag.equals(TooltipConstants.BUTTON_EDIT_PROFILE)) {
            message = "Edit Profile"
        } else if(tag.equals(TooltipConstants.BUTTON_LOGOUT)) {
            message = "Logout"
        } else if(tag.equals(TooltipConstants.BUTTON_HELP)) {
            message = "Help"
        } else {
            message = "Tooltip message for tag " + tag + " not defined"
        }

        String tooltip = """
            <div style="font-color:gray; font-family: arial; font-size: 10pt; font-weight: bold; background-color: #EEEEEE; border: 1px solid #E0E0E0; padding: 4px">${message}
            """
        if(description!=null && description!="")  {
            tooltip += """ 
                <p><div style="font-color:gray; font-family: arial; font-size: 8pt; font-weight: normal">${description}</div>
                """
        }
        tooltip += "</div>";
//        log.debug( "html tooltip: " + tooltip)
        result.setResultObject(tooltip);
        return result
    }



    /* IMPLEMENTIERUNG DER FUER REST VERWENDETEN FUNKTIONEN */



    /**
     * Returns the specified item in XML
     */
    Result getItemXML(User user, Long id){
        String xmlstr = null

        Result result = new Result();
        if(user!=null){
            result = getItem(user, id)
            if(result!=null && result.isOK()){
                def item = result.object
//                def xml = new XML(item)
//                xmlstr = xml.toString()
//                xmlstr = xmlstr.substring(xmlstr.indexOf('?>') + 2)
                result.setResultObject(item.asDeepXML())
                
            } else {
                log.warn("Item couldn't be found for user " + user + " with ID=" + id + ".");
                result.set(Result.NOTFOUND_ERROR, "Item with ID id could not be found", null)
            }
        } else {
            log.warn("Item couldn't be found for user " + user + " with ID=" + id + ".");
            result.set(Result.GENERAL_ERROR, "User object is null", null)
        }
        
        result
    }

    /**
     * Returns the list of items mathicng the specified criteria
     */
    def getAllItemsXML = {String criteria, Boolean deleted ->
        //    def c = Item.createCriteria()
        //    def items = c.list{
        //      checked: true
        //        // todo
        //    }

        def xmlstr = "<items count=\"${Item.count()}\">"
        getAllItems(criteria, deleted).each {
            xmlstr += getItemXML(it)
        }
        xmlstr += "</items>"
    }













    /**
     * Returns the list of tags of the specified item.
     */
    /*
    def getTagsXML = { Item item ->
        println "getTagsXML"

        def xmlstr =""
        if(item){
            xmlstr <<= "<tags count=\"${item.tags.size()}\">"
            item.tags.each { tag ->
                xmlstr <<= getItemXML(tag)
            }
            xmlstr <<= "</tags>"
        }
        xmlstr.toString()
    }
*/

    /**
     * Returns the list of children of the specified item.
     *//*
    def getChildrenXML = { Item item ->
        println "getChildrenXML"

        def xmlstr =""
        if(item){
            xmlstr <<= "<children count=\"${item.children.size()}\">"
            item.children.each { child ->
                xmlstr <<= getItemXML(child)
            }
            xmlstr <<= "</children>"
        }
        xmlstr.toString()
    }

    def removeTagFromHome =  {
        redirect(action: home)
    }*/




}
