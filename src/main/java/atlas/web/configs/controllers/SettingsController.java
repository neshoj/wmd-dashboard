package atlas.web.configs.controllers;

import atlas.core.template.AjaxUtils;
import atlas.core.template.View;
import atlas.core.template.datatables.DatatablesInterface;
import atlas.web.configs.SettingsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Controller
public class SettingsController {

    @Autowired
    private DatatablesInterface dataTable;
    @Autowired
    private SettingsServiceInterface settingsService;

    @RequestMapping("/system-settings")
    public ModelAndView index(HttpServletRequest request){
        View view = new View("configs/settings");

        if( AjaxUtils.isAjaxRequest(request) ){
            String action = request.getParameter("action");

            if( "fetch-record".equals( action ) ){
                return view.sendJSON( this.settingsService.fetchRecord( request ) );
            }
            else if("edit".equals( action )){
                return view.sendJSON( this.settingsService.editRecord( request ) );
            }

            return fetchTableInfo(request, view);
        }
        return view.getView();
    }

    /**
     * Fetch table data
     *
     * @param request
     * @param view
     * @return ModelAndView
     */
    private ModelAndView fetchTableInfo(HttpServletRequest request, View view){
        dataTable
                .select("name, value, description, id")
                .from("AppSettings");
        return view.sendJSON( dataTable.showTable());
    }
}
