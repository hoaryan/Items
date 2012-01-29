class Item {

    static String ITEM_NORMAL = "ITEM_NORMAL"
    static String ITEM_INBOX = "ITEM_INBOX"
    static String ITEM_HOME = "ITEM_HOME"
    static String ITEM_LOGBOOK = "ITEM_LOGBOOK"

    static String REMOVE_FROM_THIS_LIST = "THIS_LIST"
    static String REMOVE_FROM_ALL_LISTS = "ALL_LISTS"

    static hasMany = [children: Item, itemDates: ItemDate]
    static belongsTo = [user: User]

    List children  // Bei implementierung als "List" wird die sortierung mitgespeichert (fügt idx column hinzu)                 
    // Was an List scheiße ist, ist, dass die Listeneinträge nicht unique sind! (wie dies bei Set der Fall wäre)
        
    String name
    String notes
    Integer clickCount = 0

    String itemType = ITEM_NORMAL

    Boolean deleted = false

    SortedSet itemDates // Ein Item soll mehrere DateTags haben können

    Date lastUpdated // grails takes care
    Date dateCreated // grails takes care

    static transients = ['tags','isDeletable', 'isUnchainable','showTime','hasChildrenByUnchainableANDDeletable','dateInDateTagsForDate']

    static mapping = {      
        children lazy: false
        itemDates lazy: false
    }

    static constraints = {
        name(nullable: false, blank: false)
        user(nullable: false, display: false)
        notes(nullable: true, size: 0..12000)
        deleted(nullable: false)
        clickCount(nullable: false, min: 0, display:false)
        lastUpdated(display: false)
        dateCreated(display: false)          
    }

 

    // ---- EIN VERSUCH REDUNDANTE DATENHALTUNG ZU VERMEIDEN -----
    // tags ist als transiente property implementiert
    // um redundante Datenhaltung zu verhindern
    List getTags() {
        def results = Item.createCriteria().list {
            children {
                eq('id',this.id)
            }
//            order('name', 'desc') // Wenn nach dem alphabet sortiert werden soll 
        }
        // Das folgende refresh() kann unter Umstänaden super wichtig sein. Z.B. dann, wenn die Tag
        // items komplett mit Kindern richtig geladen werden müssen. Kann manchmal der Fall sein.
        // Da dieses Laden der Performance jedoch abträglich ist, habe ich es hier auskommentiert. Meistens ist bei den 
        // Tags der komplette Baum nicht erforderlich, nur bei bestimten rutinen und da kann es ja auch von Hand noch 
        // vorgenommen werden.
        // Falls bei der Verwendung von getTags() oder item.tags Merkwürdige Dinge passieren, dann liegt es i.d.R. daran,
        // dass die tag-Items nicht refreshed wurden.
        // results.each {it.refresh()} // Dieser Refresh ist notwendig, da sonst die Tags der Childitems nicht korrekt geladen werden
        return results
    }



    //TODO: Es wäre besser wenn das Unique in den beiden folgenden addToTags Methoden in einer beforeUpdate funktion untergebracht wäre
    //      Problem ist nämlich, dass wenn addToChildren verwendet wird, kein unique aufgerufen wird. Programmierer müssen aufpassen
    boolean addToTags(Item tagItem){        
         // verhindern, dass items mit sich selbst getagged werden und dass ein tag doppelt hinzugefügt wird und dass kein eigenes Kind als Tag hinzugefügt wird.
        if (tagItem != this && (!(tagItem?.children?.contains(this))) && (!(this?.children?.contains(tagItem)))) {
            tagItem.addToChildren(this)          
            if (tagItem.save(flush:true)) {
                return true
            }
            else {
                return false
            }
        }
        else {
            return false
        }
    }

    boolean addToTags(List tagItems){
        def ret = true
        for (itemInstance in tagItems) {
            if (!addToTags(itemInstance)) { //        einzel item methode aufrufen
                ret = false
            }            
        }
        return ret
    }



    boolean removeFromTags(Item tagItem){
        if (this.getIsUnchainable()) {
            tagItem.removeFromChildren(this)
            if (this.save(flush:true)) { return true }
        }
        return false
    }



    // BeforeUpdate erzeugt in Grails 1.1 einen Hibernate.AssertionFailure - an assertion failure occured (this may indicate a bug in Hibernate, but is more likely due to unsafe use of the session)
    // Soll wohl mit version 1.2 behoben werden
    //    def beforeUpdate = {
    //        println 'beforeUpdate Foo'
    //      //  if (children.unique()) {println 'uniqued!'}        // Super wichtig sonst können Tags doppelt vorkommen
    //    }

    Boolean getIsDeletable() {
        if (!children || children.size()==0) {
            return true
        }
        else {
            return false
        }
    }
    
    Boolean getIsUnchainable() {
        if (tags && tags.size()>1) {
            return true
        }
        else {
            return false
        }
    }


    boolean dateInDateTags(Date tmpDate) {
        Date start = tmpDate.clone()
        Date end = tmpDate.clone()
        end.setMinutes(59)
        end.setSeconds(59)
        end.setHours(23)
        start.setMinutes(0)
        start.setSeconds(0)
        start.setHours(0)

        boolean ret = false
        this.itemDates.each {
//            println "item: " + this.name
//            println "start: " + start + "end: " + end
//            println "it.startDate: " + it.startDate
//            println "it.endDate: " + it.endDate
            if (it.startDate>=start && it.startDate<=end) {
                ret = true
            }
            if (it.endDate && ((it.startDate<=start && it.endDate>=start) || (it.startDate<=end && it.endDate>=end))) {
                ret = true
            }
        }
//        println ret
        return ret
    }

    ItemDate getDateInDateTagsForDate(Date forDate) {
        Date start = forDate.clone()
        Date end = forDate.clone()
        end.setMinutes(59)
        end.setSeconds(59)
        end.setHours(23)
        start.setMinutes(0)
        start.setSeconds(0)
        start.setHours(0)

        ItemDate result
        this.itemDates.each {
//            println "item: " + this.name
//            println "start: " + start + "end: " + end
//            println "it.startDate: " + it.startDate
//            println "it.endDate: " + it.endDate
            if (it.startDate>=start && it.startDate<=end) {
                result = it
            }
            if (it.endDate && ((it.startDate<=start && it.endDate>=start) || (it.startDate<=end && it.endDate>=end))) {
                result = it
            }
        }
//        println result
        result
    }

    // true falls ein itemDate existiert das nicht ganztägig ist und dabei das gleiche Datum wie currentDate hat, ansonsten false
    boolean getShowTime(Date currentDate) {

        boolean showTime = false
        if(!currentDate){
            log.warn "currentDate is null "
            showTime = false
        } else {
            itemDates.each {

//                log.debug "currentDate: " + currentDate
//                log.debug "itemDate: " + this
//                log.debug "it.isGanztaegig" + it.isGanztaegig
//
//                log.debug "it.startDate: " + it.startDate
//                log.debug "it.startDate.getDay() == currentDate.getDay(): " + (it.startDate.getDay() == currentDate.getDay())
//                log.debug "it.startDate.getMonth() == currentDate.getMonth(): " + (it.startDate.getMonth() == currentDate.getMonth())
//                log.debug "it.startDate.getYear() == currentDate.getYear(): " + (it.startDate.getYear() == currentDate.getYear())
//                log.debug "it.endDate: " + it.endDate
//                log.debug "it.endDate.getDay() == currentDate.getDay(): " + (it.endDate.getDay() == currentDate.getDay())
//                log.debug "it.endDate.getMonth() == currentDate.getMonth(): " + (it.endDate.getMonth() == currentDate.getMonth())
//                log.debug "it.endDate.getYear() == currentDate.getYear(): " + (it.endDate.getYear() == currentDate.getYear())

                if(it.isGanztaegig==false &&
                    (
                        (it.startDate
                         && it.startDate.getDay() == currentDate.getDay()
                         && it.startDate.getMonth() == currentDate.getMonth()
                         && it.startDate.getYear() == currentDate.getYear())
                      ||
                        (it.endDate
                         && it.endDate.getDay() == currentDate.getDay()
                         && it.endDate.getMonth() == currentDate.getMonth()
                         && it.endDate.getYear() == currentDate.getYear())
                    )
                ){
                    showTime = true
                }
            }


        }
//        log.debug "getShowTime is: " + showTime
        return showTime
    }

    boolean getHasChildrenByUnchainableANDDeletable(boolean unchainable, boolean deletable){
        boolean result = false
        children.each(){
            if(it.isUnchainable==unchainable && it.isDeletable==deletable){
                result = true
            }
        }
        result
    }

    //Author: Horst
    String tagString() {
    
        def result = "("
        if (tags) {
            Integer count = 0
            result <<= "size:${tags.size()})("
            tags.each {
                count++
                result <<= it.id + ((count!=tags.size())? "," : "")
            }
        } else {
            result <<= "null"
        }

        result <<= ")"
    }

    //Author: Horst
    String childrenString() {
        def result = "("
        if (children) {
            def count = 0
            result <<= "size:${children.size()})("
            children.each {
                count++
                result <<= it.id + ((count!=children.size())? "," : "")
            }
        } else {
            result <<= "null"
        }
        result <<= ")"
    }


    public String asDeepXML(){
        def xml = "";
        if(id==null || id<0){
            log.warn("Item has no valid id: " + id)
        }        

        xml <<= "<item id=\"" << id << "\">\n"

        xml <<= "\t<name>" << (name!=null? name : "") << "</name>\n"
        xml <<= "\t<notes>" << (notes!=null? notes : "") << "</notes>\n"
        xml <<= "\t<clickcount>" << (clickCount!=null? clickCount : "") << "</clickcount>\n"
        xml <<= "\t<deleted>" << (deleted!=null? deleted : "") << "</deleted>\n"
        xml <<= "\t<lastUpdated>" << (lastUpdated!=null? lastUpdated : "") << "</lastUpdated>\n"
        xml <<= "\t<dateCreated>" << (dateCreated!=null? dateCreated : "") << "</dateCreated>\n"
        xml <<= "\t<isDeletable>" << (getIsDeletable()!=null? getIsDeletable() : "") << "</isDeletable>\n"
        xml <<= "\t<isUnchainable>" << (getIsUnchainable()!=null? getIsUnchainable() : "") << "</isUnchainable>\n"

        xml <<= "<tags>\n"
        for(Item i: getTags()){
            xml <<= asFlatXML(i)
        }
        xml <<= "</tags>\n"

        xml <<= "<children>\n"
        for(Item i: children){
            xml <<= asChildXML(i)
        }
        xml <<= "</children>\n"

        xml <<= user.asXML()

        xml <<= "</item>\n"


    }

    private String asChildXML(Item item){
        def xml = "";
        if(item!=null){
            
            if(item.id==null || item.id<0){
                log.warn("Item has no valid id: " + id)
            }
            xml <<= "\t<item id=\"" << item.id << "\" childCount=\"" << item.children.size() << "\">\n"

            xml <<= "\t\t<name>" << item.name << "</name>\n"
            xml <<= "\t\t<notes>" << (item.notes!=null? item.notes : "") << "</notes>\n"
//            xml <<= "\t<clickcount>" << item.clickCount << "</clickcount>\n"
//            xml <<= "\t<deleted>" << item.deleted << "</deleted>\n"
//            xml <<= "\t<lastUpdated>" << item.lastUpdated << "</lastUpdated>\n"
//            xml <<= "\t<dateCreated>" << item.dateCreated << "</dateCreated>\n"
            xml <<= "\t\t<isDeletable>" << item.getIsDeletable() << "</isDeletable>\n"
            xml <<= "\t\t<isUnchainable>" << item.getIsUnchainable() << "</isUnchainable>\n"


            xml <<= "\t\t<tags>\n"
            for(Item i: getTags(item)){
                xml <<= asFlatXML(i)
            }
            xml <<= "\t\t</tags>\n"
          
            xml <<= "\t</item>\n"            
        }
        xml
    }

    private String asFlatXML(Item item){
        def xml = "";
        if(item!=null){
           
            if(item.id==null || item.id<0){
                log.warn("Item has no valid id: " + id)
            }
            xml <<= "\t\t<item id=\"" << item.id << "\">\n"

            xml <<= "\t\t\t<name>" << item.name << "</name>\n"
//            xml <<= "\t<notes>" << item.notes!=null? item.notes : "" << "</notes>\n"
//            xml <<= "\t<clickcount>" << item.clickCount << "</clickcount>\n"
//            xml <<= "\t<deleted>" << item.deleted << "</deleted>\n"
//            xml <<= "\t<lastUpdated>" << item.lastUpdated << "</lastUpdated>\n"
//            xml <<= "\t<dateCreated>" << item.dateCreated << "</dateCreated>\n"
//            xml <<= "\t<isDeletable>" << item.isDeletable << "</isDeletable>\n"
//            xml <<= "\t<isUnchainable>" << item.isUnchainable << "</isUnchainable>\n"
            xml <<= "\t\t</item>\n"            
        }
        
        xml
    }

    // wird von asFlatXML benötigt
    private Set getTags(Item item) {
        def results = null
        if(item!=null){

            results = Item.createCriteria().list {
                children {
                    eq('id',item.id)
                }
            }
        }
        results
    }

    @Override
    String toString() {
    "[ID]:${id} [Name]:${name} [Notes]:${notes} [Clicks]:${clickCount} [Dates]:${itemDates*.id} ${itemDates} dates: #${itemDates?.size()}}"
    }

   
}
