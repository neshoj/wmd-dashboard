package atlas.web.schools.forms;

import atlas.core.template.forms.Form;
import atlas.web.schools.entities.Schools;
import org.springframework.stereotype.Component;

@Component
public class SchoolsForm extends Form<Schools> {
    public SchoolsForm(){
        setMapping( Schools.class );
        setRole("ROLE_SCHOOLS", "Schools", "super-admin");
    }
}
