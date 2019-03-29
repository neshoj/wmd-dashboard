package opdwms.web.configs.controllers;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.AppConstants;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.configs.ReasonCodeServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Controller
public class ReasonCodesController {

    @Autowired
    private DatatablesInterface dataTable;
    @Autowired
    private ReasonCodeServiceInterface reasonCodesService;

    @RequestMapping(value = "/reason-codes")
    public ModelAndView index(HttpServletRequest request) {
        View view = new View("configs/reason-codes");

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
                map = this.reasonCodesService.saveRecord( request );

                //When retrieving a record's information
            else if( "fetch-record".equals( action ))
                map = this.reasonCodesService.fetchRecord( request );

                //When updating a record
            else if( "edit".equals( action ))
                map = this.reasonCodesService.updateRecord(request );

                //When deleting a record
            else if( "delete".equals( action ))
                map = this.reasonCodesService.deleteRecord(request );
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
                .select("v.name, v.description, v.id")
                .from("ReasonCodes v")
                .where("flag <> :flag").setParameter("flag", AppConstants.STATUS_SOFTDELETED );
        return view.sendJSON(dataTable.showTable());
    }
}
