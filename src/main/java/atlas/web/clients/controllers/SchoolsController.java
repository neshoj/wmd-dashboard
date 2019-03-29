package atlas.web.clients.controllers;

import atlas.core.template.AjaxUtils;
import atlas.core.template.View;
import atlas.core.template.datatables.DatatablesInterface;
import atlas.web.clients.ClientsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import atlas.web.configs.ReasonCodeServiceInterface;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class SchoolsController {
    private ClientsServiceInterface entityService;
    private DatatablesInterface dataTable;
    private ReasonCodeServiceInterface reasonCodeService;

    @Autowired
    public SchoolsController(ClientsServiceInterface entityService,
                             DatatablesInterface dataTable,
                             ReasonCodeServiceInterface reasonCodeService){
        this.entityService = entityService;
        this.dataTable = dataTable;
        this.reasonCodeService = reasonCodeService;
    }

    @RequestMapping( value = "/schools")
    public ModelAndView index(HttpServletRequest request){
        View view = new View("schools/schools");

        if (AjaxUtils.isAjaxRequest(request)) {
            String action = request.getParameter("action");

            if( null != action )
                return handleRequests(request, view);

                //When fetching table data
            else
                return fetchTableInfo(request, view);

        }

        return view
                .addAttribute("reasoncodes", reasonCodeService.fetchRecords() )
                .getView();
    }

    /**
     * Handle various client requests
     *
     * @param request Current Request
     * @param view Current view
     * @return ModelAndView
     */
    private ModelAndView handleRequests(HttpServletRequest request, View view){
        Map<String, Object> map = new HashMap<String, Object>();
        try
        {
            String action = request.getParameter("action");

            //When creating a record
            if("new".equals( action ) )
                map = this.entityService.saveRecord( request );

            else if("edit".equals( action ))
                map = this.entityService.editRecord( request );

                //When fetching a record
            else if("fetch-record".equals( action ) )
                map = this.entityService.fetchRecord( request );

                //View record changes
            else if("vedit".equals( action ) )
                map = this.entityService.fetchRecordChanges( request );

                //When approving changes
            else if("approve-edit".equals( action ) || "decline-edit".equals( action) )
                map = this.entityService.approveEditChanges( request );

                //View deactivation reasons
            else if("vdeactivation".equals( action ) )
                map = this.entityService.fetchDeactivationInfo( request );

                //When deactivating an object
            else if("deactivate".equals( action ))
                map = this.entityService.deactivateRecord( request );

                //When toggling entity status
            else if("decline-deactivation".equals( action ) || "approve-deactivation".equals( action )
                    || "activate".equals( action ) || "approve-new".equals( action )
                    || "delete".equals( action ) || "decline-new".equals( action ) )
                map = this.entityService.flagRecords( request );

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
     * @param request Current Request
     * @param view Current View
     * @return JsonView - json structure that DataTable can consume
     */
    private ModelAndView fetchTableInfo(HttpServletRequest request, View view){
        String state = request.getParameter("fetch-table");

        dataTable
                .select("a.name, a.location, a.contactPhone, a.contactEmail, str(a.createdOn; 'YYYY-MM-DD HH24:MI:SS'), str(a.updatedOn; 'YYYY-MM-DD HH24:MI:SS'), a.id")
                .from("Clients a")
                .where("a.flag = :flag")
                .setParameter("flag", state );
        return view.sendJSON( dataTable.showTable() );
    }
}
