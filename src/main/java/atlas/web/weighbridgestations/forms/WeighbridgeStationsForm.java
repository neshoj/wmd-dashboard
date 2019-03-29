package atlas.web.weighbridgestations.forms;

import atlas.core.template.forms.Form;
import atlas.web.weighbridgestations.entities.WeighbridgeStations;
import org.springframework.stereotype.Component;

@Component
public class WeighbridgeStationsForm extends Form<WeighbridgeStations> {

    public WeighbridgeStationsForm(){
        setMapping( WeighbridgeStations.class );
        setRole("ROLE_WEIGHBRIDGE_STATION", "Weighbridge Stations", "super-admin,kenha-admin, aea-admin");
    }
}
