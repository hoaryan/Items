import java.text.SimpleDateFormat

class ItemDate implements Comparable {

    static int TYPE_ONE_TIME = 1
    static int TYPE_EVERY_WEEK = 2
    static int TYPE_EVERY_MONTH = 4
    static int TYPE_EVERY_YEAR = 8


    // workaround
    static Map<Integer, String> REPEAT_TYPE_MAP = [
        1:"Don't repeat",
        2:"Repeat Every Week",
        4:"Repeat Every Month",
        8:"Repeat Every Year"
            ];
    static belongsTo = [ownerItem: Item]
    static transients = ['period','timeString','endTimeString','startTimeString','fullInfo','sameStartDay','sameEndDay','periodString','inDay']

    Boolean isGanztaegig = true
    Date startDate
    Date endDate

    Integer repeatType = TYPE_ONE_TIME

    Date lastUpdated // grails takes care
    Date dateCreated // grails takes care
 

    static constraints = {        
        startDate(nullable: false)
        endDate(nullable: true)
        isGanztaegig(nullable: false)
        lastUpdated(display: false)
        dateCreated(display: false)
        ownerItem(nullable:true)
        repeatType(nullable:false)
    }

    boolean isInDay(Date date) {
        Date start = date.clone()
        Date end = date.clone()
        end.setMinutes(59)
        end.setSeconds(59)
        end.setHours(23)
        start.setMinutes(0)
        start.setSeconds(0)
        start.setHours(0)

        boolean ret = false

//            println "item: " + this.name
//            println "start: " + start + "end: " + end
//            println "it.startDate: " + it.startDate
//            println "it.endDate: " + it.endDate
            if (startDate>=start && startDate<=end) {
                ret = true
            }
            if (endDate && ((startDate<=start && endDate>=start) || (startDate<=end && endDate>=end))) {
                ret = true
            }

//        println ret
        return ret
    }

    boolean isPeriod() {
        return endDate!=null
    }

    boolean endInSameDay() {       
        return getSameEndDay(startDate)
    }

    boolean getSameStartDay(Date date){
        boolean sameStartDay = false;
        if(startDate!=null){
            sameStartDay = ((startDate.getDay()==date.getDay())
                && (startDate.getMonth()==date.getMonth())
                && (startDate.getYear()==date.getYear()));
        }
        return sameStartDay
    }

    boolean getSameEndDay(Date date){
        boolean sameEndDay = false;
        if(endDate!=null){
            sameEndDay = ((endDate.getDay()==date.getDay())
                && (endDate.getMonth()==date.getMonth())
                && (endDate.getYear()==date.getYear()));
        }
        return sameEndDay
    }

    int compareTo(obj) {
        startDate.compareTo(obj.startDate)
    }


    String toString() {
        String resultString = ""

        SimpleDateFormat dayFormatter = new SimpleDateFormat("dd.MM")
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM HH:mm")
                
        if (startDate != null) {
            if(isGanztaegig){
                resultString = dayFormatter.format(startDate.getTime())
            } else {
                resultString = dateFormatter.format(startDate.getTime())
            }

        } else {
            return "startTime not defined."
        }
        if (isPeriod()) {
            if(!isGanztaegig){
                if(endInSameDay()){
                    resultString += "-" + timeFormatter.format(endDate.getTime())
                } else {
                    resultString += " - " + dateFormatter.format(endDate.getTime())
                }
            } else {
                resultString += " - " + dayFormatter.format(endDate.getTime())
            }
          
        }
        return resultString
    }

    String getTimeString(){
        String resultString = ""

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        if (startDate != null) {
          resultString = formatter.format(startDate.getTime())
        }
        if (endInSameDay()){
            resultString += "-" + formatter.format(endDate.getTime())
        }
        return resultString
    }
    
    String getPeriodString(){
        String resultString = ""

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM");
        if (startDate) {
          resultString = formatter.format(startDate.getTime())
        }
        if (endDate){
            resultString += "-" + formatter.format(endDate.getTime())
        }
        return resultString
    }

    String getStartTimeString(){
        String resultString = ""

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        if (startDate != null) {
          resultString = formatter.format(startDate.getTime())
        }
        return resultString
    }

    String getEndTimeString(){
        String resultString = ""
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        if (endDate != null) {            
          resultString += formatter.format(endDate.getTime())
        }
        return resultString
    }

    String getFullInfo(){
        String resultString = ""

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM HH:mm");
        if (startDate != null) {
          resultString = formatter.format(startDate.getTime())
        }
        if (endDate != null) {
          resultString += "-" + formatter.format(endDate.getTime())
        }
        resultString += "  ganztaegig: " + isGanztaegig
        resultString += "  zeitspanne: " + (endDate!=null)
        resultString += "  repeatType: " + repeatType

        return resultString
    }
    
}
