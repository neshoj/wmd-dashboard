package atlas.web.dashboard.controllers;

import atlas.core.template.AjaxUtils;
import atlas.core.template.View;
import atlas.core.template.datatables.DatatablesInterface;
import atlas.web.dashboard.DashboardServiceInterface;
import atlas.web.usermanager.entities.UserTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @category    Dashboard
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Controller
public class DashboardController {

    @Autowired
    private DatatablesInterface dataTable;
    @Autowired
    private DashboardServiceInterface dashboardService;

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request) throws Exception {
        View view = new View("dashboard/default-view");
        String parentType = (String)request.getSession().getAttribute("_userParentType");

        if(UserTypes.SCHOOL_ADMIN.equals( parentType ) || UserTypes.SCHOOL_OFFICER.equals( parentType ) )
            view = new View("dashboard/school-admin-view");

        if (AjaxUtils.isAjaxRequest(request)) {
            String calendarStart = request.getParameter("start");

            if( null != calendarStart )
                return view.sendJSON( dashboardService.fetchCalendarData( request ) );

                //When fetching table data
            else
                return fetchTableInfo(request, view);

        }

        return view
                .addAttribute("data", dashboardService.fetchStatistics( request ) )
                .getView();
    }

    /**
     * Fetch calendar data
     *
     * @param request
     * @param view
     * @return ModelAndView
     */
    private ModelAndView fetchTableInfo(HttpServletRequest request, View view){
        Long parentNo = (Long) request.getSession().getAttribute("_userParentNo");
        String parentType = (String)request.getSession().getAttribute("_userParentType");

        if(StringUtils.isEmpty( parentType )) {
            dataTable
                    .nativeSQL(true)
                    .select(" a.name, SUM(c.amount)")
                    .from("merchants a")
                    .from("LEFT JOIN terminals b ON b.merchant_no = a.id ")
                    .from("LEFT JOIN transactions c ON c.terminal_no = b.id ")
                    .groupBy("a.name ")
            ;
        }

        //When serving merchants
        else{
            dataTable
                    .nativeSQL(true)
                    .select(" a.name, FORMAT( COALESCE( SUM(b.amount); 0) ; 2)")
                    .from("outlets a")
                    .from("LEFT JOIN transactions b ON b.outlet_no = a.id ")
                    .where("a.merchant_no = :merchantNo")
                    .groupBy("a.name ")
                    .setParameter("merchantNo", parentNo )
            ;
        }

        return view.sendJSON( dataTable.showTable() );
    }


}
