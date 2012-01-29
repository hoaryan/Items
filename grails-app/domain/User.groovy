class User {
   
  static hasMany = [items: Item]
  static transients = ['confirmPassword','captchaSolved']

 //  String userName // I dont like userNames we should just use the email
  String email
  String firstName
  String lastName

  long homeItemID
  long inboxItemID
  long logbookItemID

  Date lastUpdated // grails takes care
  Date dateCreated // grails takes care


  String password  
  String confirmPassword
  Boolean captchaSolved = false
  Boolean isAdmin = false

  static constraints = {
    firstName(nullable: false, blank: false)
    lastName(nullable: false, blank: false)
    email(nullable:false, blank:false , email:true, unique:true)
    isAdmin(nullable:false, blank:false)
    password(nullable:false, blank:false, minSize:4)
    lastUpdated(display: false)
    dateCreated(display: false)
    confirmPassword(
        validator:{
          val, obj ->
            if (val!=null) {
                if (val==obj.password && val.size()>=4 ) {
                    return true
                }
                else {
                    return false
                }
                }
                return true
            })
    }

    public String asXML(){
        def xml = ""
        xml <<= "<user id=\"" << id << "\">\n"
        xml <<= "\t<firstName>" << firstName << "</firstName>\n"
        xml <<= "\t<lastName>" << lastName << "</lastName>\n"
        xml <<= "\t<email>" << email << "</email>\n"
        xml <<= "\t<homeItemID>" << homeItemID << "</homeItemID>\n"
//        xml <<= "\t<lastUpdated>" << lastUpdated << "</lastUpdated>\n"
//        xml <<= "\t<dateCreated>" << dateCreated << "</dateCreated>\n"
        xml <<= "</user>\n"
    }

    public String toString(){
        StringBuilder b = new StringBuilder();
        b.append(firstName).append(" ").append(lastName).append(" (ID=").append(id).append(" email=").append(email).append(")")
        b.toString()
    }


}
