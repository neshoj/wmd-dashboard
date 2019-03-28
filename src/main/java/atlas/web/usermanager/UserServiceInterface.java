package atlas.web.usermanager;



import atlas.core.template.forms.BaseServiceInterface;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public interface UserServiceInterface extends BaseServiceInterface {

    /**
     * Validates email and sends a reset password link to the email in question
     *
     * @param email
     * @return
     */
    public Map<String, Object> generateResetToken(String email);

    /**
     * Allows a user to reset their password
     *
     * @param names Client full names
     * @param email Client email address
     * @param token Password reset token
     * @return Boolean Results of processing
     */
    public boolean sendPasswordToken(String names, String email, String token);
    
     /**
     * Validates the secure token send to the user
     *
     * @param token
     * @return
     */
    public String validateResetToken(final String token);

    
    /**
     * Reset forgotten user password
     *
     * @param token Random secure code send to the user email
     * @param password New password for this account
     * @return
     */
    public Map<String, Object> setupNewPassword(String token, String password);

    /**
     * Reset user attempts
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> unlockUser(HttpServletRequest request);
}
