package atlas.web.configs.controllers;


import atlas.core.template.AjaxUtils;
import atlas.core.template.View;
import atlas.core.template.datatables.DatatablesInterface;
import atlas.web.configs.MakerCheckerServiceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class MakerCheckerController {

    @Autowired
    private MakerCheckerServiceInterface makerCheckerServiceInterface;
    @Autowired
    private DatatablesInterface dataTable;
    private static final Logger logger = LoggerFactory.getLogger(MakerCheckerController.class);

    @RequestMapping( value = "/maker-checker")
    public ModelAndView index(HttpServletRequest request){
        View view = new View("configs/maker-checker");

        /*Process ajax requests*/
        if (AjaxUtils.isAjaxRequest(request)) {
            String action = request.getParameter("action");
            if( null != action )
                return handleRequests(request, view);

                //When fetching table info
            else return fetchTableInfo(request, view);
        }
        return view.getView();
    }

    /**
     * Handle various client requests
     *
     * @param request Current Request
     * @param view Current view
     * @return ModelAndView
     */
    private ModelAndView handleRequests(HttpServletRequest request, View view){
        Map<String, Object> map = new HashMap<>();
        try
        {
            String action = request.getParameter("action");

            //When editing a record
            if("edit".equals( action ))
                map = makerCheckerServiceInterface.editRecord( request );

                //When fetching a record
            else if("fetch-record".equals( action ) )
                map = makerCheckerServiceInterface.fetchRecord( request );

        }
        catch(Exception e){
            logger.error("Serving ajax requests responded with: " + e );
            map.put("status", "01");
            map.put("message", "Internal server error; contact admin.");
        }

        return view.sendJSON( map );
    }

    /**
     * Fetch table information
     *
     * @param request
     * @param view
     * @return JSON View
     */
    public ModelAndView fetchTableInfo(HttpServletRequest request, View view){

        dataTable
                .select("v.code, v.enabled, v.id")
                .from("MakerChecker v");

        return view.sendJSON(dataTable.showTable());
    }
}
