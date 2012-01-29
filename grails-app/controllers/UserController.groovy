import org.apache.commons.codec.digest.DigestUtils

class UserController {

    //    def scaffold  = User

    def defaultHomeItems = [ "Todo","Next","Projects","Maybe/Sometimes","Locations","Contacts" ];


    // Instance of JCaptcha-Service
    def jcaptchaService

    def index = {
        if (session.user) {
            redirect(action:'show')
        }
        else
        {
            redirect(action:'login')
        }
    }

    def show = {
        def user = User.get(session.user.id)
        [userInstance:user]
    }


    def delete = {
        def user = User.get( params.id )
        if(user) {
            try {
                user.delete(flush:true)
                flash.message = "User mit Login: ${user.email} deleted"
                redirect(action:'logout')
                // TODO: Dafür sorgen, dass die flash messages auch angezeigt werden.
            }
            catch(org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "User ${params.id} could not be deleted"
                redirect(action:show,id:params.id)
            }
        }
        else {
            flash.message = "User not found with id ${params.id}"
            redirect(action:'logout')
        }



    }

    
    def edit = {
        def user = User.get(session.user.id)        
        return [userInstance:user]
    }

    def handleEdit = {
        def user = User.get(session.user.id)
        def oldSavedPassword = user.password
        if (params.oldPassword.size() > 0 && (oldSavedPassword == DigestUtils.md5Hex(params.oldPassword)) )
        {
             user.properties = params
             if(user.validate()) {
                def hashPassd = DigestUtils.md5Hex(user.password)
                user.password=hashPassd
                flash.message = "User mit Login ${user.email} updated"
                redirect(controller:'item', action:'homeview')
                session.user = user
              }
                else {
                    flash.user = user
                    chain(action:'edit', model:[userInstance:user])
                }
        }
        else {
                flash.user = user
                flash.message = "Altes Passwort stimmt nicht."
                chain(action:'edit', model:[userInstance:user])
        }

        
    }


    def login = {
        log.info "Action: login"
    }

    def logout = {
        log.info 'Action: logout'
        if(session.user) {
			session.user = null
                        session.itemViewOrderEls = null // Reihenfolge soll nicht mehr zugänglich sein
			session.invalidate()		
        }
        redirect(action:'login')
	}


    def handleLogin = {

        log.info "Action: handleLogin"
        def hashPassd = DigestUtils.md5Hex(params.password)
        
        // find the user name
        def user = User.findByEmailAndPassword(params.email, hashPassd)

        if (!user) {
                flash.message = "Login/Email und/oder Passwort nicht korrekt."
                redirect(action:'login')
                return
        } else {
            session.user = user
            session.itemViewOrderEls = []  // Die Reihenfolge der Betrachtung soll gespeichert werden
            redirect(controller:'item', action:'homeview')
        }
    }


    def admin = {
        def allUser = User.list()
        log.debug "All Users: " + allUser
        [userInstance: session.user, allUser: User.list()]
    }


    def register = {


    }


    def handleRegistration = {
       // println "Start von handle Registration"
       // println params.captchaSolved
        def user = new User(params)
       // println user?.captchaSolved
        
        if (!(user?.captchaSolved)) {
           //  println "Sprung in captcha validation check"
            if (jcaptchaService.validateResponse("imageCaptcha", session.id, params.captchaResponse))
            {
                user.captchaSolved=true
            }
            else
            {
                user.captchaSolved=false
                flash.message = "Captcha Challenge code did not match."
                chain(action:'register', model:[userInstance:user])
            }                
        }

        user?.isAdmin = false

        if (user.captchaSolved) {
             if(user.validate()) {

                // TODO - send confirmation email

                def hashPassd = DigestUtils.md5Hex(user.password)
                user.password=hashPassd
                user.confirmPassword=hashPassd                
                session.user = user
                user.save()


                // Home Items vom User erstellen
                def homeItem = new Item(name:"Home", user:user).save()

                defaultHomeItems.each(){
                    homeItem.addToChildren(new Item(name:it, user:user))
                }
                homeItem.save(flush:true)

                // Inbox Item erstellen
                def inboxItem = new Item(name:"Inbox", user:user, itemType:Item.ITEM_INBOX).save()
                inboxItem.addToTags(homeItem)
                
                // Logbook Item erstellen
                def logbookItem = new Item(name:"Logbook", user:user, itemType:Item.ITEM_LOGBOOK).save()
                logbookItem.addToTags(homeItem)

                // Dem User die home, inbox und logbook Item ID bekanntgeben
                user.homeItemID = homeItem.id
                user.inboxItemID = inboxItem.id
                user.logbookItemID = logbookItem.id
                user.save()

                session.itemViewOrderEls = []  // Die Reihenfolge der Betrachtung soll gespeichert werden

                redirect(action:'admin', model:[userInstance:user])
                }
             else {
                flash.user = user           
                chain(action:'register', model:[userInstance:user])
            }
        }

    }

    




}
