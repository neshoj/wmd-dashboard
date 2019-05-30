package opdwms.web.weighingtransactions.forms;

import opdwms.core.template.forms.Form;
import opdwms.web.weighingtransactions.entities.VirtualStationTransactions;
import org.springframework.stereotype.Component;

@Component
public class VirtualStationWeighingTransactionsForm extends Form<VirtualStationTransactions> {

    public VirtualStationWeighingTransactionsForm(){
        setMapping( VirtualStationTransactions.class );
        setRole("ROLE_WEIGHBRIDGE_STATION", "Weighbridge Stations", "super-admin,kenha-admin, aea-admin");
    }
}
