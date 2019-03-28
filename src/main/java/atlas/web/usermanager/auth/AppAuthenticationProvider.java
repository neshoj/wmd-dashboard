package atlas.web.usermanager.auth;

import atlas.web.configs.AuditServiceInterface;
import atlas.web.configs.entities.AuditTrail;
import atlas.web.usermanager.auth.exceptions.InactiveAccountException;
import atlas.web.usermanager.auth.services.UserAttemptsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Component
public class AppAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserAttemptsService userAttempts;
    @Autowired
    private AuditServiceInterface auditService;

    @Autowired
    @Qualifier("appUserDetailsService")
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        super.setPasswordEncoder( passwordEncoder );
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        StringBuilder error = new StringBuilder();
        String email = authentication.getName();
        AuditTrail log = new AuditTrail();
        try {
            //Login has been successful.
            Authentication auth = super.authenticate(authentication);
            
            //Reset user attempts
            userAttempts.resetFailedAttempts(authentication.getName());
            
            //Generate audit trail
            log
                    .setLogType( AuditTrail.USER_GENERATED )
                    .setActivity("Logged into the System")
                    .setNewValues( "Log in" )
                    .setOldValues("Log out")
                    .setStatus("Success");
            auditService.saveLog(log, email);

            return auth;
        } catch (BadCredentialsException e) {
            //invalid login, update user_attempts
            long remainingRetries = userAttempts.updateFailedAttempts(authentication.getName());

            if (remainingRetries > 0) {
                error.append("Invalid email/password");
                if (remainingRetries == 1) {
                    error.append(": 1 attempt remaining");
                } else {
                    error.append(": ").append(remainingRetries).append(" attempts remaining");
                }
            } else {
                error.append(userAttempts.processLockedAccount(authentication.getName()));
            }

            log
                    .setLogType( AuditTrail.USER_GENERATED )
                    .setActivity("Attempt to log into the system - " +error.toString() )
                    .setNewValues( "Log out" )
                    .setOldValues("Log out")
                    .setStatus("Failed");
            auditService.saveLog(log, email);

            /*Return a custom message*/
            throw new BadCredentialsException(error.toString());
        } catch (LockedException e) {
            /*User account is locked*/
            error.append(userAttempts.processLockedAccount(authentication.getName()));

            log
                    .setLogType( AuditTrail.USER_GENERATED )
                    .setActivity("Attempt to log into the system - " +error.toString() )
                    .setNewValues( "Log out" )
                    .setOldValues("Log out")
                    .setStatus("Failed");
            auditService.saveLog(log, email);

            throw new LockedException(error.toString());
        } catch (InactiveAccountException en) {
            error.append("Sorry! Your account is inactive. Go to your email to activate your account ");

            log
                    .setLogType( AuditTrail.USER_GENERATED )
                    .setActivity("Attempt to log into the system - " +error.toString() )
                    .setNewValues( "Log out" )
                    .setOldValues("Log out")
                    .setStatus("Failed");
            auditService.saveLog(log, email);

            throw new InactiveAccountException(error.toString());
        }
    }
}
