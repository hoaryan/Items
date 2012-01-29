/**
 *
 * @author hsueggel
 */
class TimeTaglibUtil {

    String key
    String value

    public static List<TimeTaglibUtil> getMinuteList(){
        List<TimeTaglibUtil> list = new ArrayList<TimeTaglibUtil>()
        12.times() { it ->
            String viewValue = String.valueOf(it*5).length()==1? "0"+String.valueOf(it*5):String.valueOf(it*5)
            list.add(new TimeTaglibUtil(key: String.valueOf(it*5), value: viewValue))
        }        
        return list
    }

    public static List<TimeTaglibUtil> getHourList(){
        List<TimeTaglibUtil> list = new ArrayList<TimeTaglibUtil>()
        24.times() { it ->
            String viewValue = String.valueOf(it).length()==1? "0"+String.valueOf(it):String.valueOf(it)
            list.add(new TimeTaglibUtil(key: String.valueOf(it), value: viewValue))
        }
        return list
    }

    public static List<TimeTaglibUtil> getRepeatTypeList(){
        List<TimeTaglibUtil> list = new ArrayList<TimeTaglibUtil>()
        ItemDate.REPEAT_TYPE_MAP.keySet().each { it ->
            list.add(new TimeTaglibUtil(key: it, value: ItemDate.REPEAT_TYPE_MAP.get(it)))
        }
        println list
        return list
    }

    public String toString(){
        return key
    }
}

