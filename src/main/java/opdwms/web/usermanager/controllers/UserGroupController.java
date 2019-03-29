package opdwms.web.usermanager.controllers;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.configs.ReasonCodeServiceInterface;
import opdwms.web.usermanager.UserGroupServiceInterface;
import opdwms.web.usermanager.entities.UserTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Controller
public class UserGroupController {

    @Autowired
    private DatatablesInterface dataTable;
    @Autowired
    private UserGroupServiceInterface entityService;
    @Autowired
    private ReasonCodeServiceInterface reasonCodesService;

    @RequestMapping( value = "/user-groups")
    public ModelAndView index(HttpServletRequest request){
        View view = new View("user-manager/user-groups");
        String parentType = (String)request.getSession().getAttribute("_userParentType");

        //When handling other institutions' records
        if( !StringUtils.isEmpty( parentType ) ) view = new View("user-manager/school-admin-user-groups");

        String action = request.getParameter("action");
        if ( AjaxUtils.isAjaxRequest(request) ) {

            if( null != action ){
                return handleRequests(request, view);
            }

            //when fetching table data
            else return fetchTableInfo(request, view);
        }

        return view
                .addAttribute("permissions", this.entityService.fetchPermissions( request) )
                .addAttribute("reasoncodes", reasonCodesService.fetchRecords() )
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
        Map<String, Object> map = new HashMap<>();
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

        return view.sendJSON(map);
    }

    public ModelAndView fetchTableInfo(HttpServletRequest request, View view){
        String status = request.getParameter("fetch-table");
        Long parentNo = (Long)request.getSession().getAttribute("_userParentNo");
        String parentType = (String)request.getSession().getAttribute("_userParentType");

        dataTable
                .select("a.name, a.description, str(a.createdOn; 'YYYY-MM-DD HH24:MI:SS'), str(a.updatedOn; 'YYYY-MM-DD HH24:MI:SS'), a.id")
                .from("UserGroups a")
                .where("a.flag = :flag").setParameter("flag", status);

        // Set the parent entity
        if( !StringUtils.isEmpty( parentType ) && null != parentNo ){

            if( parentType.equals( UserTypes.AEA_ADMIN) || parentType.equals( UserTypes.AEA_ADMIN) ){
                dataTable
                        .from("LEFT JOIN a.clientsGroups b")
                        .where("b.clientNo = :parentNo")
                        .setParameter("parentNo", parentNo);
            }
        }
        return view.sendJSON( dataTable.showTable() );
    }
}
