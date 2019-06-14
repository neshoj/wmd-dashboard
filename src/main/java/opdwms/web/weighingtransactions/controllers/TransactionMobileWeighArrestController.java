package opdwms.web.weighingtransactions.controllers;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.weighingtransactions.TransactionMobileWeighArrestServiceInterface;
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
public class TransactionMobileWeighArrestController {

    private DatatablesInterface dataTable;
    private TransactionMobileWeighArrestServiceInterface transactionMobileWeighArrestServiceInterface;

    @Autowired
    public TransactionMobileWeighArrestController(
            DatatablesInterface dataTable,
            TransactionMobileWeighArrestServiceInterface transactionMobileWeighArrestServiceInterface
    ){
        this.dataTable = dataTable;
        this.transactionMobileWeighArrestServiceInterface = transactionMobileWeighArrestServiceInterface;
    }

    @RequestMapping(value = "/mobile-weigh-arrest")
    public ModelAndView index(HttpServletRequest request) {
        View view = new View("mobile-weigh/mobile-weigh-arrest");

        /*Process ajax requests*/
        if (AjaxUtils.isAjaxRequest(request)) {
            String action = request.getParameter("action");

            if( null != action )
                return handleRequests(request, view);

                //When fetching table data
            else
                return fetchTableInfo(request, view);
        }
        return view.getView();
    }

    public ModelAndView handleRequests(HttpServletRequest request, View view){
        Map<String, Object> map = new HashMap<>();
        try
        {
            String action = request.getParameter("action");

            //When creating a new record
            if( "new".equals( action ))
                map = this.transactionMobileWeighArrestServiceInterface.saveRecord( request );

                //When retrieving a record's information
            else if( "fetch-record".equals( action ))
                map = this.transactionMobileWeighArrestServiceInterface.fetchRecord( request );

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
                .select("str(a.transactionDate; 'YYYY-MM-DD'), a.vehicleNo, a.transporter, a.excessGVW, a.excessAxleWeight," +
                        "a.cargo, a.origin, a.destination, a.description")
                .from("TransactionMobileWeighArrest a");
        return view.sendJSON(dataTable.showTable());
    }
}
