class SecurityFilters {

    def authService
    def filters = {
        WebFilter(controller:'item', action:'*') {
            before = {
                if (!session.user
                    && !(controllerName.equals('user') && actionName.equals('login'))
                    && (!controllerName.equals('jcaptcha'))
                    && !(controllerName.equals('user') && actionName.equals('register'))
                    && !(controllerName.equals('user') && actionName.equals('handleRegistration'))
                    && !(controllerName.equals('user') && actionName.equals('handleLogin'))
                )
                {
                    redirect(controller:'user', action:'login')
                    return false
                }
            }
        }

        RESTFilter(controller:'itemREST', action:'*') {
            before = {               
                log.info("Authorization: " + request.getHeader("Authorization"));

                def login = "horst.sueggel@googlemail.com"
                def password = "test"
                             
//                def isAuthenticated = authService.checkAuthentication(login, password)
                def isAuthenticated = authService.checkAuthorization(request)
                if(!isAuthenticated){
                    log.warn("Login with User: " + login + "  Pass: " + password + "    ...failed")
                    redirect(controller:'error', action:'notAuthenticated')
                    return false
                }
                log.info("Login with User: " + login + "  Pass: " + password + "     ...sucessfull")
            }
        }
    }

}

