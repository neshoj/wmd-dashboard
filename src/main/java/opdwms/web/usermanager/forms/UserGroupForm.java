package opdwms.web.usermanager.forms;

import opdwms.core.template.forms.Form;
import opdwms.web.usermanager.entities.UserGroups;
import org.springframework.stereotype.Component;

/**
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Component
public class UserGroupForm extends Form<UserGroups> {

    //This class constructor initiliazes the mapping managed by the Form class
    public UserGroupForm() {
        setMapping( UserGroups.class);
        setRole("ROLE_USERGROUP", "Usergroups", "quickgas");
    }
}
