package opdwms.web.usermanager.auth.handlers;

import opdwms.web.configs.AuditServiceInterface;
import opdwms.web.configs.entities.AuditTrail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Component
public class AppLogoutHandler implements LogoutHandler {

    @Autowired
    private AuditServiceInterface logService;

    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email =  authUser.getUsername();

        AuditTrail log = new AuditTrail()
                .setLogType( AuditTrail.USER_GENERATED )
                .setActivity("Logged out of the System")
                .setNewValues( "Log out" )
                .setOldValues("Log in")
                .setStatus("Success");
        logService.saveLog(log, email);

    }
}
