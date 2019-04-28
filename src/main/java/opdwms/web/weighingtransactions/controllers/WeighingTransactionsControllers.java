package opdwms.web.weighingtransactions.controllers;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class WeighingTransactionsControllers {
    @Autowired
    private DatatablesInterface datatable;

    @RequestMapping("/weighing-transactions")
    public ModelAndView weighingTransaction(HttpServletRequest request){
        View view = new View("weighing-transactions/weighing-transactions");
        // Fetch the table data
        if ( AjaxUtils.isAjaxRequest( request ) ) {
            //Set-up data
            datatable
                    .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.ticketNo, b.name, a.vehicleNo, ")
                    .select(" a.axleConfiguration, a.vehicleGVM,  ")
                    .select("a.firstAxleWeight, a.secondAxleWeight, a.thirdAxleWeight, a.fourthAxleWeight, a.fifthAxleWeight, a.sixthAxleWeight, a.seventhAxleWeight, ")
                    .select("a.operator, a.operatorShift, a.actionTaken, a.permitNo ")
                    .from("WeighingTransactions a LEFT JOIN a.weighbridgeStationsLink  b ");

            return view.sendJSON( datatable.showTable() );
        }
        return view.getView();
    }

    @RequestMapping("/tagging-transactions")
    public ModelAndView taggingTransaction(HttpServletRequest request){
        View view = new View("weighing-transactions/tagging-transactions");
        // Fetch the table data
        if ( AjaxUtils.isAjaxRequest( request ) ) {
            //Set-up data
            datatable
                    .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.tagReference, a.vehicleNo,  a.confirmedVehicle_no, a.transgression, ")
                    .select(" a.weighingReference, a.taggingSystem, a.taggingScene, ")
                    .select("a.tagStatus, a.tagOnChargeAmount, a.weighbridge, a.chargedReason ")
                    .from("TaggingTransactions a");

            return view.sendJSON( datatable.showTable() );
        }
        return view.getView();
    }

    @RequestMapping("/hswim-transactions")
    public ModelAndView HSWIMTransaction(HttpServletRequest request){
        View view = new View("weighing-transactions/hswim-transactions");
        // Fetch the table data
        if ( AjaxUtils.isAjaxRequest( request ) ) {
            //Set-up data
            datatable
                    .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.ticketNo, a.stationName,  a.vehicleSpeed, ")
                    .select(" a.vehicleLength, a.axleCount, ")
                    .select("a.axleOne, a.axleTwo, a.axleThree, a.axleFour, a.axleFive, a.axleSix, a.axleSeven ")
                    .from("HSWIMTransaction a");

            return view.sendJSON( datatable.showTable() );
        }
        return view.getView();
    }
}
