/**
 *
 * @author hsueggel
 */
class DateUtil {


    private static List months = ['Januar','Februar','März','April','Mai','Juni','Juli','August','September','Oktober','November','Dezember']
    private static List monthsShort = ['JAN','FEB','MÄR','APR','MAI','JUN','JUL','AUG','SEP','OKT','NOV','DEZ']

    static String getMonthString(int number){
        if(number >=0 && number<12){
            return months[number]
        } else{
            return "invalid number for month: " + number
        }
    }

    static String getShortMonthString(int number){
        if(number >=0 && number<12){
            return monthsShort[number]
        } else{
            return "invalid number for short month: " + number
        }
    }

    static boolean equalsDay(Date one, Date two){
        if(one && two){
            GregorianCalendar calOne = new GregorianCalendar()
            calOne.setTime(one)
            GregorianCalendar calTwo = new GregorianCalendar()
            calTwo.setTime(two)
            int dayOne = calOne.get(Calendar.DAY_OF_MONTH)
            int monthOne = calOne.get(Calendar.MONTH)+1
            int yearOne = calOne.get(Calendar.YEAR)

            int dayTwo = calTwo.get(Calendar.DAY_OF_MONTH)
            int monthTwo = calTwo.get(Calendar.MONTH)+1
            int yearTwo = calTwo.get(Calendar.YEAR)
            if(dayOne==dayTwo && monthOne==monthTwo && yearOne==yearTwo){
                return true
            }
        }
        return false
    }
    
    static boolean dayGreaterThan(Date one, Date two){
        if(one && two){
            GregorianCalendar calOne = new GregorianCalendar()
            calOne.setTime(one)
            GregorianCalendar calTwo = new GregorianCalendar()
            calTwo.setTime(two)

            int dayOne = calOne.get(Calendar.DAY_OF_MONTH)
            int monthOne = calOne.get(Calendar.MONTH)+1
            int yearOne = calOne.get(Calendar.YEAR)

            int dayTwo = calTwo.get(Calendar.DAY_OF_MONTH)
            int monthTwo = calTwo.get(Calendar.MONTH)+1
            int yearTwo = calTwo.get(Calendar.YEAR)

//            println yearOne + ">" + yearTwo + ": " + (yearOne<yearTwo)
            if(yearOne>yearTwo){
                return true
            } else if(yearOne==yearTwo) {
//                println "_"+ monthOne + ">" + monthTwo + ": " + (monthOne<monthTwo)
                if(monthOne>monthTwo){
                    return true
                } else if(monthOne==monthTwo){
//                    println "_"+ dayOne + ">" + dayTwo + ": " + (dayOne<dayTwo)
                    if(dayOne>dayTwo){
                        return true
                    }
                }
            }
        }
        return false
    }

    static boolean dayLesserThan(Date one, Date two){        
        if(one && two){
            GregorianCalendar calOne = new GregorianCalendar()
            calOne.setTime(one)
            GregorianCalendar calTwo = new GregorianCalendar()
            calTwo.setTime(two)
            
            int dayOne = calOne.get(Calendar.DAY_OF_MONTH)
            int monthOne = calOne.get(Calendar.MONTH)+1
            int yearOne = calOne.get(Calendar.YEAR)

            int dayTwo = calTwo.get(Calendar.DAY_OF_MONTH)
            int monthTwo = calTwo.get(Calendar.MONTH)+1
            int yearTwo = calTwo.get(Calendar.YEAR)

//            println yearOne + "<" + yearTwo + ": " + (yearOne<yearTwo)
            if(yearOne<yearTwo){
                return true
            } else if(yearOne==yearTwo) {
//                println "_"+ monthOne + "<" + monthTwo + ": " + (monthOne<monthTwo)
                if(monthOne<monthTwo){
                    return true
                } else if(monthOne==monthTwo){
//                    println "_"+ dayOne + "<" + dayTwo + ": " + (dayOne<dayTwo)
                    if(dayOne<dayTwo){
                        return true
                    }
                }
            }
        }
        return false
    }



    static String getWeekInYear(Date date){
        Calendar c = Calendar.getInstance()
        c.setTime(date)
        c.setMinimalDaysInFirstWeek(1)
        int week = c.get(Calendar.WEEK_OF_YEAR)
        week.toString()
    }

    static Date getWeekBeginning(Date date){
        Calendar c = Calendar.getInstance()
        c.setTime(date)
        int day_of_week = c.get(Calendar.DAY_OF_WEEK)
        Date beginnDate = c.getTime()-day_of_week+1
        beginnDate
    }

    static Date getWeekEnd(Date date){
        Calendar c = Calendar.getInstance()
        c.setTime(date)
        int day_of_week = c.get(Calendar.DAY_OF_WEEK)
        Date endDate = c.getTime()-day_of_week+7
        endDate
    }

    static Date getFirstDayInMonth(Date date){
        Calendar c = Calendar.getInstance()
        c.setTime(date)
        int value = c.getActualMinimum(Calendar.DAY_OF_MONTH)
        c.set(Calendar.DAY_OF_MONTH, value)
        Date firstDayInMonth = c.getTime()
        firstDayInMonth
    }

    static Date getLastDayInMonth(Date date){
        Calendar c = Calendar.getInstance()
        c.setTime(date)
        int value = c.getActualMaximum(Calendar.DAY_OF_MONTH)
        c.set(Calendar.DAY_OF_MONTH, value)
        Date lastDayInMonth = c.getTime()
        lastDayInMonth
    }

    static boolean itemsInDate(List items, Date date){        
        boolean hasItemInDate = false
        if(date && items){
            items.each { item ->
                item.itemDates.each { itemDate ->
//                    println "   "
//                    println it.startDate.toString() + "==" + date + ": " + equalsDay(it.startDate,date)
                    if(hasItemInDate==false){
                        if(itemDate.repeatType<=1){ // normal Date
                            if(equalsDay(itemDate.startDate,date)) {
                                hasItemInDate = true
                            } else if(itemDate.endDate) {
        //                        println it.endDate.toString() + "==" + date + ": " + equalsDay(it.endDate,date) + " || ("
        //                        println date.toString() + ">" + it.startDate + ": " + dayGreaterThan(date, it.startDate) + " && "
        //                        println date.toString() + "<" + it.endDate + ": " + dayLesserThan(date, it.endDate) + ")"
                                if(equalsDay(itemDate.endDate,date) || (dayGreaterThan(date, itemDate.startDate)&&dayLesserThan(date, itemDate.endDate)) ){
                                    hasItemInDate = true
                                }
                            }
                        } else { // repeat Date
                            hasItemInDate = repeatItemDateMatchesDate(itemDate, date)
                        }
                    }
                    
                }
            }
        }        
        return hasItemInDate
    }

    static boolean repeatItemMatchesDatespan(Item item, Date startDate, Date endDate){
        boolean match = false
        for(Date currDate: (startDate..endDate)) {
            match = repeatItemMatchesDate(item, currDate)

            
        }
        return match
    }

    static boolean repeatItemMatchesDate(Item item, Date date){
        boolean match = false
        for(int i=0; i<item.itemDates.size(); i++) {
            ItemDate itemDate = item.itemDates.toArray(new ItemDate[0])[i]
            if(itemDate){
                match = repeatItemDateMatchesDate(itemDate, date)
                if(match){
                    return match
                }
            }
        }
        return match
    }

    static boolean repeatItemDateMatchesDate(ItemDate itemDate, Date date) {
        boolean match = false

        // check only repeat dates
        if(itemDate.repeatType >= ItemDate.TYPE_EVERY_WEEK){
            
            if(!itemDate.isPeriod()){// not period
                boolean end = false
                Date tmpDate = itemDate.startDate
                while(!end){
                   if(equalsDay(tmpDate, date)){
                        match = true
                        end = true
                   }
                   tmpDate = tmpDate + 7
                   if(tmpDate > date){
                       end = true
                   }
                }
                
            } else {// period
                for(Date currDate: (itemDate.startDate..itemDate.endDate)) {
                    boolean end = false
                    Date tmpDate = currDate
                    while(!end){
                       if(equalsDay(tmpDate, date)){
                            match = true
                            end = true
                       }
                       tmpDate = tmpDate + 7
                       if(tmpDate > date){
                           end = true
                       }
                    }
                    if(match){
                        break;
                    }
                }
            }

        } else if(itemDate.repeatType >= ItemDate.TYPE_EVERY_MONTH){

        } else if(itemDate.repeatType >= ItemDate.TYPE_EVERY_YEAR){

        } else {

        }

        return  match
    }

    static String getDuePeriod(Item item, Date today){
        int period = 0
        item.itemDates.each(){
            period = 365*10
            Date tempDate
            if(it.isPeriod()){
                tempDate = it.endDate
            } else {
                tempDate = it.startDate
            }

            int delta = today - tempDate
            //println delta
            if(delta<period && delta>0){
                period = delta
            }
        }
        period
    }

}

