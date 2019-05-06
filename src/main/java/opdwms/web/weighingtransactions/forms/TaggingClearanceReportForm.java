package opdwms.web.weighingtransactions.forms;

import opdwms.core.template.forms.Form;
import opdwms.web.weighingtransactions.entities.TaggingClearanceReport;
import org.springframework.stereotype.Component;

@Component
public class TaggingClearanceReportForm extends Form<TaggingClearanceReport> {

    public TaggingClearanceReportForm(){
        setMapping( TaggingClearanceReport.class );
        setRole("ROLE_WEIGHBRIDGE_STATION", "Weighbridge Stations", "super-admin,kenha-admin, aea-admin");
    }
}