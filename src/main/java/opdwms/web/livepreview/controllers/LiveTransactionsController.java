package opdwms.web.livepreview.controllers;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.livepreview.models.OperatorRequest;
import opdwms.web.weighingtransactions.entities.TaggingTransactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LiveTransactionsController {

    @Autowired
    private DatatablesInterface datatable;

    @MessageMapping("/connect")
    @SendTo("/topic/weighing-transactions")
    public void initiateConnection(OperatorRequest request) throws Exception {
        System.out.println("Connection request made " + request.getAction());
    }

    @MessageMapping("/connect-tags")
    @SendTo("/topic/tags-transactions")
    public void initiateTagsConnection(OperatorRequest request) throws Exception {
        System.out.println("Tags Connection request made " + request.getAction());
    }

    @RequestMapping("/live-preview")
    public ModelAndView weighingTransactionLivePreview(HttpServletRequest request) {
        View view = new View("live-preview/live-preview");

        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {
            datatable.esDocument("static_station_weigh_transaction")
                    .esFields("transactionDate", "status", "ticketNo", "stationName", "vehicleNo", "axleConfiguration",
                            "vehicleGVM", "permitNo")
                    .esDateFields("transactionDate");
            return view.sendJSON(datatable.showEsTable());

            //Set-up data
//            datatable
//                    .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.ticketNo, b.name, a.vehicleNo," +
//                            " a.axleConfiguration, a.vehicleGVM, a.actionTaken")
//                    .from("WeighingTransactions a LEFT JOIN WeighbridgeStations b ON b.id = a.stationCode");
//
//            return view.sendJSON(datatable.showTable());
        }

        return view.getView();
    }

    @RequestMapping("/tags-live-preview")
    public ModelAndView tagsLivePreview(HttpServletRequest request) {
        View view = new View("live-preview/tags-live-preview");

        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {

            //Set-up data
            datatable
                    .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.tagReference, a.vehicleNo, a.transgression, ")
                    .select(" a.taggingSystem, a.taggingScene, a.weighbridge ")
                    .from("TaggingTransactions a ")
                    .where("a.tagStatus = :state")
                    .setParameter("state", TaggingTransactions.OPEN_TAGS);

            return view.sendJSON(datatable.showTable());
        }

        return view.getView();
    }
}
