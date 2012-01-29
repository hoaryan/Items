class ItemTagLib {

    static namespace = "items"

    /***
     * Creates Timepicker for hour and minute value.
     * <p>
     * Parameter for post editing:
     *
     * <ul>
     * <li><name-attribute-value>_hour: selected hour</li>
     * <li><name-attribute-value>_hour: selected hour</li>
     * </ul>
     */
    def timepicker = { attrs, body ->

        def name = "time"
        if(attrs.name){
            name = attrs.name
        }
         
        def defaultHour = null
        def defaultMinute = null
        if(attrs.defaultHour!=null && attrs.defaultMinute!=null){
            defaultHour = attrs.defaultHour
            defaultMinute = attrs.defaultMinute
        }

        def disabled = true
        if(attrs.disabled){
            disabled = attrs.disabled
        }

        println "name: " + name + "  defaultHour/Minute: " + defaultHour + ":" + defaultMinute + "  attrs: " + attrs

        def hour_name = name+"_hour"
        def minute_name = name+"_minute"
        
        out << "<span>"
        out << g.select(name: hour_name, from: TimeTaglibUtil.getHourList() , optionKey: "key", optionValue: "value", value: defaultHour, disabled:disabled)
        out << g.select(name: minute_name, from: TimeTaglibUtil.getMinuteList(), optionKey: "key", optionValue: "value", value: defaultMinute, disabled:disabled)
        out << "</span>"
    }

    def remoteAddDateBtn = { attrs, body ->
        
        String s = g.submitToRemote(value:"Add Date",
                        url:[controller: 'itemDate', action: 'showDateEditDialog', isNew: 'true'],
                        update:"editDateArea",
                        onLoading:"showSpinner(true)",
                        onLoaded:"showSpinner(false)",
                        onComplete:"showSpinner(false)",
                        onFailure:"showSpinner(false)"                        
            )
        String lnk =  g.resource(dir:"images/styleimages", file:"dateButton.png")
//        println lnk
        s = s.replaceAll("button","image")
        s = s.replaceAll("type=\"image\"","type=\"image\" src=\"" + lnk + "\" class=\"addDate\"")

//        println "!!!!!!!!!!!!: " + s
        out << s
    }

    def remoteEditDateBtn = { attrs, body ->

        String s = g.submitToRemote(value:"Edit",
                        url:[controller: 'itemDate', action: 'showDateEditDialog', isNew: 'false'],
                        update:"editDateArea",
                        onLoading:"showSpinner(true)",
                        onLoaded:"showSpinner(false)",
                        onComplete:"showSpinner(false)",
                        onFailure:"showSpinner(false)"
            )
        String lnk =  g.resource(dir:"images/styleimages", file:"editDateButton.png")
//        println lnk
        s = s.replaceAll("button","image")
        s = s.replaceAll("type=\"image\"","type=\"image\" src=\"" + lnk + "\" class=\"editDate\"")

//        println "!!!!!!!!!!!!: " + s
        out << s
    }

}
    
