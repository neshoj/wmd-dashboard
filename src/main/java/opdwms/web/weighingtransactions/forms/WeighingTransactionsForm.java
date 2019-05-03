package opdwms.web.weighingtransactions.forms;

import opdwms.core.template.forms.Form;
import opdwms.web.weighingtransactions.entities.WeighingTransactions;
import org.springframework.stereotype.Component;

@Component
public class WeighingTransactionsForm extends Form<WeighingTransactions> {

    public WeighingTransactionsForm(){
        setMapping( WeighingTransactions.class );
        setRole("ROLE_WEIGHBRIDGE_STATION", "Weighbridge Stations", "super-admin,kenha-admin, aea-admin");
    }
}
