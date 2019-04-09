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
                    .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.ticketNo, a.stationCode, a.vehicleNo, ")
                    .select(" a.axleConfiguration, a.vehicleGVM, a.cargo, ")
                    .select("a.firstAxleWeight, a.firstAxleType, a.secondAxleWeight, a.secondAxleType, ")
                    .select("a.thirdAxleWeight, a.thirdAxleType, a.fourthAxleWeight, a.fourthAxleType, ")
                    .select("a.fifthAxleWeight, a.fifthAxleType, a.sixthAxleWeight, a.sixthAxleType, ")
                    .select("a.seventhAxleWeight, a.seventhAxleType, ")
                    .select("a.operator, a.operatorShift, ")
                    .select(" a.origin, a.destination, a.actionTaken, a.permitNo")
                    .from("WeighingTransactions a");

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
}
