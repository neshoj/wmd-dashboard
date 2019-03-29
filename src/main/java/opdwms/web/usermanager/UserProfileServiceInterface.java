package opdwms.web.usermanager;

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
public interface UserProfileServiceInterface {

    /**
     * Fetch the user details associated with the user in question
     *
     * @param   userId
     * @return  Map<String, Object>
     */
    public Map<String, Object> fetchUserDetails(Long userId);

    /**
     * Update profile details
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> updateProfile(HttpServletRequest request);

    /**
     * Update profile image
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> updatePhoto(HttpServletRequest request);

    /**
     * Allow a user to change their password
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> changePassword(HttpServletRequest request);
}
