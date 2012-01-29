import java.text.SimpleDateFormat
import java.text.DateFormat

class ItemDateController {

    // Instanz wird von Grails auomatisch injiziert
    def itemService

    def index = { }

    def calendarlist = {
        log.debug "action: calendarlist"
        log.debug "params: " + params
        Date startDate 
        Date endDate
        def viewType
        
        if(params.viewType){
            viewType = params.viewType;
        }
        if(!viewType){
            viewType=CalendarviewType.VIEW_CUSTOM
        }
        
        if (params.startDate) {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd")

            startDate = formatter.parse(params.startDate)
            if (params.endDate) {
                endDate = formatter.parse(params.endDate)
            } else {
                endDate = startDate.clone()
            }
        }
//        log.debug "startDate: " + startDate
//        log.debug "endDate: " + endDate

        def result
        if(params.id){
            
            result = itemService.getItemDate(params.id.toInteger())
            if(result.isOK()){
                def itemDate = result.object
                startDate = itemDate.startDate
                endDate = itemDate.endDate?itemDate.endDate:startDate
            } else {
                log.error result.message
            }
            result = itemService.getItemsInRange(session.user, params.id.toInteger(), true)
        } else {
            result = itemService.getItemsInRange(session.user, startDate, endDate, true)
        }



        List overDueItems
        if(viewType==CalendarviewType.VIEW_TODAY && DateUtil.equalsDay(startDate, new Date())){
            Result dueItemsResult = itemService.getDueItems(session.user, startDate)
            if(dueItemsResult.isOK()){
                overDueItems = dueItemsResult.object
            } else {
                log.error dueItemsResult.message
            }
        }

        if(result.isOK()){
            log.debug "opening calendarview: " + startDate + "  -  " + endDate + "    type: " + viewType
            def rangeItems = result.object
            log.debug "rangeItems: " + rangeItems
            return [startDate:startDate, endDate:endDate, inRangeItems: rangeItems, viewType: viewType, overDueItems: overDueItems]
        } else {
            log.warn result.message
            redirect(action: homeview)
        }

   }

   def editDates = {
       log.debug "action: editDates"
//       log.debug "params: " + params
//       log.debug "flash.itemDate: " + flash.itemDate

//       flash.itemDate = flash.itemDate
//       log.debug "flash.itemDate instanceof ItemDate: " + (flash.itemDate instanceof ItemDate)
//       log.debug "params.itemDate instanceof ItemDate: " + (params.itemDate instanceof ItemDate)
       
        def result = itemService.getItem(session.user, params.id)
        if (result.isOK()) {
            def itemInstance = result.object
            if(params.itemDate==null || params.isNew==null){
                return [itemInstance:itemInstance]
            } else {
                return [itemInstance:itemInstance, isNew:params.isNew.toBoolean(), itemDate:flash.itemDate]
            }            
        } else {
            log.warn "Item not found with id ${params.id}"
            redirect(controller:'item', action:'stdview', id:params.id)
            return
        }
    }

    def showDateEditDialog = {
        log.debug "action: showDateEditDialog"
//        log.debug "params: " + params
        
        if(params.isNew && params.itemId){
            ItemDate temp = ItemDate.get(params.dateId) ?: null;
            def isNew = params.isNew=='true'?true:false;
//            log.debug temp?.getFullInfo()
            render (template:"formEditSingleDate", model:[isNew: isNew, itemId: params.itemId, itemDate:temp])
        } else {
            render "no itemDate"
        }        
    }

    def handleCreateOrEditDate = {

        log.debug "action: handleCreateOrEditDate"
        def result = new Result()

//        log.debug "params: " + params
//        log.debug "params?.__itemId: " + params?.__itemId
//        log.debug "params?.__isNew: " + params?.__isNew
//        log.debug "params?.__id: " + params?.__id
//        log.debug "params?.startDate: " + params?.startDate_day + "." + params?.startDate_month + "." + params?.startDate_year + " " + params?.starttime_hour + ":" + params?.starttime_minute
//        log.debug "params?.endDate: " + params?.endDate_day + "." + params?.endDate_month + "." + params?.endDate_year + " " + params?.endtime_hour + ":" + params?.endtime_minute
//        log.debug "params?.starttime: " + params?.starttime_hour + ":" + params?.starttime_minute
//        log.debug "params?.endtime: " + params?.endtime_hour + ":" + params?.endtime_minute
//        log.debug "params?.zeitspanne: " + params?.zeitspanne
//        log.debug "params?.timeactive: " + params?.timeactive

        int repeatType =  params.repeat_type? Integer.valueOf( params.repeat_type) : null

        log.debug "repeatType: " + repeatType
        int itemId = null
        try {
            itemId = params.__itemId? Integer.valueOf(params.__itemId) : null
        } catch(NumberFormatException ex) { itemId = null }
        
        int dateId = null
        try {
            dateId = params.__id? Integer.valueOf(params.__id) : null
        } catch(NumberFormatException ex) { dateId = null }
        
        boolean isNew = params.__isNew == 'false'? false : true

        if(itemId==null){
            flash.message = "ERROR: ItemId is missing"
            log.error "ItemId is missing"
            render(view:'editDates', model:[itemInstance:session.user.homeItemID])
        }

        boolean isZeitspanne = params?.zeitspanne!=null
        boolean isGanztaegig = params?.timeactive==null

        GregorianCalendar startDate = null
        GregorianCalendar endDate = null
        int startHour = 0
        int startMinute = 0
        int endHour = 0
        int endMinute = 0

        if(params?.starttime_hour && params?.starttime_minute){
            startMinute = new Integer(params.starttime_minute)
            startHour = new Integer(params.starttime_hour)
        }
        if(params?.endtime_hour && params?.endtime_minute){
            endMinute = new Integer(params.endtime_minute)
            endHour = new Integer(params.endtime_hour)
        }

        if(params?.startDate_year && params?.startDate_month && params?.startDate_day){
            startDate = new GregorianCalendar(params.startDate_year.toInteger(), params.startDate_month.toInteger()-1, params.startDate_day.toInteger() , startHour, startMinute)
        }
        if(params?.endDate_year && params?.endDate_month && params?.endDate_day){
            endDate = new GregorianCalendar(params.endDate_year.toInteger(), params.endDate_month.toInteger()-1, params.endDate_day.toInteger() , endHour, endMinute)
        }

//        log.debug "itemId: " + itemId
//        log.debug "dateId: " + dateId
//        log.debug "startDate: " + ((startDate!=null)?startDate.getTime():startDate) + "  startTime: " + startHour + ":" + startMinute
//        log.debug "endDate: " + ((endDate!=null)?endDate.getTime():endDate) + "  endTime: " + endHour + ":" + endMinute
//        log.debug "isGanztaegig: " + isGanztaegig
//        log.debug "isZeitspanne: " + isZeitspanne
//        log.debug "isNew: " + isNew

        result = itemService.updateItemDate(session.user, itemId, dateId, startDate, endDate, repeatType, isGanztaegig, isZeitspanne, isNew)

        if (result.isOK()) {            
            flash.message = "updated"
            redirect(controller: "item", action: "stdview", id:itemId)
        } else {
             def itemDate = result.object             
             if (result.code==Result.VERSION_ERROR) {
                log.warn "VERSION_ERROR on item: " + itemInstance
                itemDate.ownerItem.errors.rejectValue("version", "item.optimistic.locking.failure", "Item wurde zwischenzeitlich verändert!")
            } else if (result.code==Result.VALIDATION_ERROR) {
                flash.message = null
                log.debug result.message
             }
             log.debug "Item Error: " + result.code + " message: " + result.message

             flash.itemDate = itemDate
             redirect(controller: "item", action: "stdview", params:[id:itemId, isNew:isNew, itemDate:itemDate])
             return
        }
        
    }

    def deleteDate = {

        log.debug "action: deleteDate"
        def result = new Result()

        log.debug "params: " + params
//        log.debug "params?.itemId: " + params?.itemId
//        log.debug "params?.id: " + params?.id
//        log.debug "params?.startDate: " + params?.startDate
//        log.debug "params?.endDate: " + params?.endDate

        int itemId = null
        try {
            itemId = params.itemId? Integer.valueOf(params.itemId) : null
        } catch(NumberFormatException ex) { itemId = null }

        int dateId = null
        try {
            dateId = params.id? Integer.valueOf(params.id) : null
        } catch(NumberFormatException ex) { dateId = null }

        

        if(itemId==null){
            flash.message = "ERROR: ItemId is missing"
            render(view:'editDates', model:[itemInstance:session.user.homeItemID])
        }

//        log.debug "itemId: " + itemId
//        log.debug "dateId: " + dateId
        
        result = itemService.deleteItemDate(session.user, itemId, dateId)

        if (result.isOK()) {
            flash.message = "deleted"
            if(params.startDate && params.endDate && params.viewType){
                redirect(action:"calendarlist", params:[startDate:params.startDate, endDate:params.endDate, viewType:params.viewType])
            } else {
                redirect(controller:"item", action:"stdview", id:itemId)
            }
        } else {
             def itemInstance = result.object
             if (result.code==Result.VERSION_ERROR) {
                log.warn "VERSION_ERROR on item: " + itemInstance
                itemInstance.errors.rejectValue("version", "item.optimistic.locking.failure", "Item wurde zwischenzeitlich verändert!")
             }
             log.debug "Item Error: " + result.code + " message: " + result.message
             if(params.startDate && params.endDate && params.viewType){
                redirect(action:"calendarlist", params:[startDate:params.startDate, endDate:params.endDate, viewType:params.viewType])
             } else {
                 redirect(controller:"item", action:"stdview", id:itemId)
             }
             return
        }
    }
    
}
