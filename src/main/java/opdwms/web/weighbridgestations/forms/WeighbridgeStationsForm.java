package opdwms.web.weighbridgestations.forms;

import opdwms.core.template.forms.Form;
import opdwms.web.weighbridgestations.entities.WeighbridgeStations;
import org.springframework.stereotype.Component;

@Component
public class WeighbridgeStationsForm extends Form<WeighbridgeStations> {

    public WeighbridgeStationsForm(){
        setMapping( WeighbridgeStations.class );
        setRole("ROLE_WEIGHBRIDGE_STATION", "Weighbridge Stations", "super-admin,kenha-admin, aea-admin");
    }
}
