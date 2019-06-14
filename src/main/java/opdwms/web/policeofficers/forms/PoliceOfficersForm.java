package opdwms.web.policeofficers.forms;

import opdwms.core.template.forms.Form;
import opdwms.web.policeofficers.entities.PoliceOfficers;
import org.springframework.stereotype.Component;

@Component
public class PoliceOfficersForm extends Form<PoliceOfficers> {
    public PoliceOfficersForm(){
        setMapping( PoliceOfficers.class );
        setRole("ROLE_MOBILE_WEIGHING", "Weighbridges", "super-admin,kenha-admin, aea-admin");
    }
}
