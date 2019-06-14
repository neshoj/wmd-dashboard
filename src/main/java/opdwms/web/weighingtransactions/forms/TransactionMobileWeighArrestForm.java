package opdwms.web.weighingtransactions.forms;

import opdwms.core.template.forms.Form;
import opdwms.web.weighingtransactions.entities.TransactionMobileWeighArrest;
import org.springframework.stereotype.Component;

@Component
public class TransactionMobileWeighArrestForm extends Form<TransactionMobileWeighArrest> {

    public TransactionMobileWeighArrestForm(){
        setMapping( TransactionMobileWeighArrest.class );
        setRole("ROLE_MOBILE_WEIGHING", "Weighbridge Stations", "super-admin, kenha-admin, aea-admin, mobile-operators");
    }
}
