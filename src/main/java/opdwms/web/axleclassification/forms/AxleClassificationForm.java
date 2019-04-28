package opdwms.web.axleclassification.forms;

import opdwms.core.template.forms.Form;
import opdwms.web.axleclassification.entities.AxleClassification;
import org.springframework.stereotype.Component;

@Component
public class AxleClassificationForm extends Form<AxleClassification> {
    public AxleClassificationForm() {
        setMapping(AxleClassification.class);
        setRole("ROLE_WEIGHBRIDGE_STATION", "Weighbridge Stations", "super-admin,kenha-admin, aea-admin");
    }
}
