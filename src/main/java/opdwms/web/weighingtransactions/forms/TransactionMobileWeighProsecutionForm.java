package opdwms.web.weighingtransactions.forms;

import opdwms.core.template.forms.Form;
import opdwms.web.weighingtransactions.entities.TransactionMobileWeighProsecution;
import org.springframework.stereotype.Component;

@Component
public class TransactionMobileWeighProsecutionForm extends Form<TransactionMobileWeighProsecution> {

    public TransactionMobileWeighProsecutionForm(){
        setMapping( TransactionMobileWeighProsecution.class );
        setRole("ROLE_MOBILE_WEIGHING", "Weighbridge Stations", "super-admin, kenha-admin, aea-admin, mobile-operators");
    }
}
