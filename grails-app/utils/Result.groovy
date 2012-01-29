class Result {

    public static String OK = "OK"
    public static String GENERAL_ERROR = "ERROR"
    public static String VERSION_ERROR = "VERSION_ERROR"
    public static String NOTFOUND_ERROR = "NOTFOUND_ERROR"
    public static String VALIDATION_ERROR = "VALIDATION_ERROR"
    public static String NOUSER_ERROR = "NOUSER_ERROR"
    public static String UNCHAIN_ERROR = "UNCHAIN_ERROR"
    public static String DELETE_ERROR = "DELETE_ERROR"
    public static String TAGGING_ERROR = "TAGGING_ERROR"
    public static String LAST_TAG_ERROR = "LAST_TAG_ERROR"
    
    public Object object = null
    public String message = null
    public String code = null

    public Result(){
        code = OK;
    }

    boolean isOK() {
        return (code == OK)
    }

    public Result set(def code, def message, def object){
        this.code = code
        this.message = message
        this.object = object
        this
    }

    public void setResultObject(def resultObject){
        this.object = resultObject
    }

    String getMessage() {
        if (message?.size()<=1) {
           return code
        }
        else {
           return message
        }
    }

    String toString(){
        return "code: ${code} message: ${message} object: ${object}"
    }

}

