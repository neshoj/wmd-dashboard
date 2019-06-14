package opdwms.web.weighingtransactions.controllers;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.policeofficers.PoliceOfficerServiceInterface;
import opdwms.web.weighingtransactions.TransactionMobileWeighProsecutionServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @category    Weighing Transaction
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Controller
public class TransactionMobileWeighProsecutionController {

    private DatatablesInterface dataTable;
    private TransactionMobileWeighProsecutionServiceInterface transactionMobileWeighProsecutionServiceInterface;
    private PoliceOfficerServiceInterface policeOfficerServiceInterface;

    @Autowired
    public TransactionMobileWeighProsecutionController(
            DatatablesInterface dataTable,
            TransactionMobileWeighProsecutionServiceInterface transactionMobileWeighProsecutionServiceInterface,
            PoliceOfficerServiceInterface policeOfficerServiceInterface
    ){
        this.dataTable = dataTable;
        this.transactionMobileWeighProsecutionServiceInterface = transactionMobileWeighProsecutionServiceInterface;
        this.policeOfficerServiceInterface = policeOfficerServiceInterface;
    }

    @RequestMapping(value = "/mobile-weigh-prosecution")
    public ModelAndView index(HttpServletRequest request) {
        View view = new View("mobile-weigh/mobile-weigh-prosecution");

        /*Process ajax requests*/
        if (AjaxUtils.isAjaxRequest(request)) {
            String action = request.getParameter("action");

            if( null != action )
                return handleRequests(request, view);

                //When fetching table data
            else
                return fetchTableInfo(request, view);
        }
        return view
                .addAttribute("officers", policeOfficerServiceInterface.fetchRecords(request))
                .getView();
    }

    public ModelAndView handleRequests(HttpServletRequest request, View view){
        Map<String, Object> map = new HashMap<>();
        try
        {
            String action = request.getParameter("action");

            //When creating a new record
            if( "new".equals( action ))
                map = this.transactionMobileWeighProsecutionServiceInterface.saveRecord( request );

                //When retrieving a record's information
            else if( "fetch-record".equals( action ))
                map = this.transactionMobileWeighProsecutionServiceInterface.fetchRecord( request );

        }
        catch(Exception e){
            e.printStackTrace();
            map.put("status", "01");
            map.put("message", "Internal server error: contact admin");
        }
        return view.sendJSON( map );
    }

    /**
     * Fetch table information
     *
     * @param request
     * @param view
     * @return ModelAndView
     */
    private ModelAndView fetchTableInfo(HttpServletRequest request, View view){
        dataTable
                .select("str(a.paymentDate; 'YYYY-MM-DD'), a.vehicleNo, a.transporter, a.expectedAmount, a.actualAmount," +
                        "a.axleClass, a.receiptNo, b.policeNo, a.cargo, a.origin, a.destination")
                .from("TransactionMobileWeighProsecution a LEFT JOIN a.policeOfficerLink b ");
        return view.sendJSON(dataTable.showTable());
    }
}
