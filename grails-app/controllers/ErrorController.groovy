class ErrorController {

    def itemService
    
    def index = { }

    /**
     * Authentication ERROR message
     */
    def notAuthenticated = {
        log.debug "action: notAuthenticated"
//        response.status = 401 // Unauthorized
        render "<error>user could not be authenticated</error>"

    }
}
