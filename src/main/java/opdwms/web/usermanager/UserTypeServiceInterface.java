package opdwms.web.usermanager;

import opdwms.web.usermanager.entities.UserTypes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public interface UserTypeServiceInterface {

    /**
     * Fetch a list of user types
     *
     * @return List<UserType>
     */
    public List<UserTypes> fetchRecords(HttpServletRequest request);
}
