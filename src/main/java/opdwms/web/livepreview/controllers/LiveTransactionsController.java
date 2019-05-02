package opdwms.web.livepreview.controllers;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.livepreview.models.OperatorRequest;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LiveTransactionsController {

    @Autowired
    private DatatablesInterface datatable;

    @MessageMapping("/connect")
    @SendTo("/topic/weighing-transactions")
    public void initiateConnection(OperatorRequest request) throws Exception {
        System.out.println("Connection request made "+ request.getAction());
    }

    @MessageMapping("/connect-tags")
    @SendTo("/topic/tags-transactions")
    public void initiateTagsConnection(OperatorRequest request) throws Exception {
        System.out.println("Tags Connection request made "+ request.getAction());
    }

    @RequestMapping("/live-preview")
    public ModelAndView weighingTransactionLivePreview(HttpServletRequest request){
        View view = new View("live-preview/live-preview");

        // Fetch the table data
        if ( AjaxUtils.isAjaxRequest( request ) ) {

            //Set-up data
            datatable
                    .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.ticketNo, b.name, a.vehicleNo, a.axleConfiguration, a.vehicleGVM, ")
                    .select("a.operator, a.origin, a.destination, a.actionTaken")
                    .from("WeighingTransactions a LEFT JOIN WeighbridgeStations b ON b.id = a.stationCode");

            return view.sendJSON( datatable.showTable() );
        }

        return view.getView();
    }

    @RequestMapping("/tags-live-preview")
    public ModelAndView tagsLivePreview(HttpServletRequest request){
        View view = new View("live-preview/tags-live-preview");

        // Fetch the table data
        if ( AjaxUtils.isAjaxRequest( request ) ) {

            //Set-up data
            datatable
                    .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.tagReference, a.vehicleNo, a.transgression, ")
                    .select(" a.taggingSystem, a.taggingScene, a.weighbridge ")
                    .from("TaggingTransactions a");

            return view.sendJSON( datatable.showTable() );
        }

        return view.getView();
    }
}
