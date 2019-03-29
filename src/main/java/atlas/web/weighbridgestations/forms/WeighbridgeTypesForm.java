package atlas.web.weighbridgestations.forms;

import atlas.core.template.forms.Form;
import atlas.web.weighbridgestations.entities.WeighbridgeTypes;
import org.springframework.stereotype.Component;

@Component
public class WeighbridgeTypesForm extends Form<WeighbridgeTypes> {

    public WeighbridgeTypesForm(){
        setMapping( WeighbridgeTypes.class );
        setRole("ROLE_WEIGHBRIDGE_STATION", "Weighbridges", "super-admin,kenha-admin, aea-admin");
    }
}
