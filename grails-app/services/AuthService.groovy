import org.apache.commons.codec.digest.DigestUtils
import javax.servlet.http.HttpServletRequest

class AuthService {

    boolean transactional = true

    def checkAuthorization(String login, String password) {
        def valid = false
        User user
        if(login!=null  && password!=null){
            def hashPassd = DigestUtils.md5Hex(password)
            user = User.findByEmailAndPassword(login, hashPassd);        
            
            valid = (user!=null);
            if(valid){
                log.info "User authenticated: " + user.email;
            } else {
                log.info "User authentication failed: Login=" + login + " Pass: " +  password;
            }
        }
        valid
    }

    boolean checkAuthorization(HttpServletRequest request) {
        def authString = request.getHeader('Authorization')

        log.debug("HTTP Authorization Header: " + authString)
        if (!authString) {
            return false;
        }

        def credentials = authString.split(':')
        if( credentials.length != 2 || credentials[0] == null || credentials[0] == "" || credentials[1] == "" || credentials[1] == null)
        return false;

        log.debug("Credentials: user=" + credentials[0] + "  pass=" + credentials[1])
        def hashPassd = DigestUtils.md5Hex(credentials[1])
        def user = User.findByEmailAndPassword(credentials[0], hashPassd)

        log.debug("User for Authorization found: " + user)
        if (user) {
            this.user = user
            return true;
        }
        return false;
    }

}
