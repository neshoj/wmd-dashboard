package opdwms.web.usermanager;


import opdwms.core.template.forms.BaseServiceInterface;
import opdwms.web.usermanager.entities.UserGroups;

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
public interface UserGroupServiceInterface extends BaseServiceInterface {

    /**
     * Fetch a list of permissions
     *
     * @param request
     * @return List<Object>
     */
    public List<Object> fetchPermissions(HttpServletRequest request);

    /**
     * Fetch  a list of records given their status
     *
     * @return List<UserGroups>
     */
    public List<UserGroups> fetchAllRecords(HttpServletRequest request);
}
