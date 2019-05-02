package opdwms.web.weighingtransactions.controllers;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.weighingtransactions.entities.TaggingTransactions;
import opdwms.web.weighingtransactions.repositories.TaggingTransactionsRepository;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
public class WeighingTransactionsControllers {
    @Autowired
    private DatatablesInterface datatable;
    @Autowired
    private TaggingTransactionsRepository taggingTransactionsRepository;

    @RequestMapping("/weighing-transactions")
    public ModelAndView weighingTransaction(HttpServletRequest request) {
        View view = new View("weighing-transactions/weighing-transactions");
        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {

            String action = request.getParameter("action");
            //When creating a record
            if (null != action && "fetch-record".equals(action)) {
                return view.sendJSON(fetchTicketData(request));

            } else {
                //Set-up data
                datatable
                        .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.status, a.ticketNo, b.name, a.vehicleNo, ")
                        .select(" a.axleConfiguration, a.vehicleGVM,  ")
                        .select("a.operator, a.operatorShift, a.permitNo, a.id ")
                        .from("WeighingTransactions a LEFT JOIN a.weighbridgeStationsLink  b ");

                return view.sendJSON(datatable.showTable());
            }
        }
        return view.getView();
    }

    private Map<String, Object> fetchTicketData(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("status", "00");
        return map;
    }

    private Map<String, Object> fetchTaggingTicketData(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();

        Optional<TaggingTransactions> taggingTransactionsRepositoryById = taggingTransactionsRepository.findById(Long.valueOf(request.getParameter("index")));
        if (taggingTransactionsRepositoryById.isPresent()) {
            map.put("status", "00");
            map.put("message", "Transaction successful");
            map.put("data", taggingTransactionsRepositoryById.get());
        }else{
            map.put("status", "01");
            map.put("message", "Invalid Transaction ID requested");
        }
        return map;
    }

    @RequestMapping("/tagging-transactions")
    public ModelAndView taggingTransaction(HttpServletRequest request) {
        View view = new View("weighing-transactions/tagging-transactions");


        String action = request.getParameter("action");
        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {
            if (null != action && "fetch-record".equals(action)) {
                return view.sendJSON(fetchTaggingTicketData(request));

            } else {
                //Set-up data
                datatable
                        .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.tagReference, a.vehicleNo, a.transgression, ")
                        .select("a.taggingSystem, a.taggingScene, ")
                        .select(" a.weighbridge, a.chargedReason, a.id ")
                        .from("TaggingTransactions a");

                return view.sendJSON(datatable.showTable());
            }
        }
        return view.getView();
    }

    @RequestMapping("/hswim-transactions")
    public ModelAndView HSWIMTransaction(HttpServletRequest request) {
        View view = new View("weighing-transactions/hswim-transactions");
        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {
            //Set-up data
            datatable
                    .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.ticketNo, a.stationName,  a.vehicleSpeed, ")
                    .select(" a.vehicleLength, a.axleCount, ")
                    .select("a.axleOne, a.axleTwo, a.axleThree, a.axleFour, a.axleFive, a.axleSix, a.axleSeven ")
                    .from("HSWIMTransaction a");

            return view.sendJSON(datatable.showTable());
        }
        return view.getView();
    }
}
