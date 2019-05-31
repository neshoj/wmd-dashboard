package opdwms.web.weighingtransactions.controllers;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.weighingtransactions.entities.VirtualStationTransactions;
import opdwms.web.weighingtransactions.forms.VirtualStationWeighingTransactionsForm;
import opdwms.web.weighingtransactions.repositories.VirtualStationTransactionRepository;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class VirtualStationTransactionsController {
    private DatatablesInterface datatable;
    private VirtualStationTransactionRepository entityRepository;
    private VirtualStationWeighingTransactionsForm entityForm;

    @Autowired
    private VirtualStationTransactionsController(
            DatatablesInterface datatable,
            VirtualStationTransactionRepository entityRepository,
            VirtualStationWeighingTransactionsForm entityForm

    ) {
        this.datatable = datatable;
        this.entityRepository = entityRepository;
        this.entityForm = entityForm;
    }

    @RequestMapping("/virtual-station-transactions")
    public ModelAndView weighingTransaction(HttpServletRequest request) {
        View view = new View("weighing-transactions/virtual-station-transactions");
        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {

            String action = request.getParameter("action");
            //When creating a record
            if (null != action && "fetch-record".equals(action)) {
                return view.sendJSON(fetchDetailedTransactionData(request));

            } else {
                //Set-up data
                datatable
                        .select("str(a.dateTime; 'YYYY-MM-DD HH24:MI'), a.flag, a.virtualStation, " +
                                "a.frontPlate, a.totalWeight, a.axleConfiguration, a.velocity, a.id ")
                        .from("VirtualStationTransactions a");

                return view.sendJSON(datatable.showTable());
            }
        }
        return view.getView();
    }

    private Map<String, Object> fetchDetailedTransactionData(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        Optional<VirtualStationTransactions> weighbridgeTransactionsOptional = entityRepository.findById(Long.valueOf(request.getParameter("index")));
        if (weighbridgeTransactionsOptional.isPresent()) {
            VirtualStationTransactions weighingTransactions = weighbridgeTransactionsOptional.get();
            map = entityForm.transformEntity(weighingTransactions);
            map.put("status", "00");
            map.put("message", "Transaction processed successfully");
        } else {
            map.put("status", "01");
            map.put("message", "Invalid Transaction Id provided");
        }
        return map;
    }
}
