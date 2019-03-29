package opdwms.web.usermanager.forms;

import opdwms.core.template.forms.Form;
import opdwms.web.usermanager.entities.Users;
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
public class UsersForm extends Form<Users> {

    public UsersForm(){
        setMapping( Users.class );
        setRole("ROLE_USERS", "Users", "superadmin");
    }
}
