package opdwms.web.configs.forms;

import opdwms.core.template.forms.Form;
import opdwms.web.configs.entities.AppSettings;
import org.springframework.stereotype.Component;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Component
public class AppSettingsForm extends Form<AppSettings> {

    //This class constructor initiliazes the mapping managed by the Form class
    public AppSettingsForm() {
        setMapping( AppSettings.class );
    }
}
