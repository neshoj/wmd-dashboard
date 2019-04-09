package opdwms.web.configs.controllers;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Ignatius
 * @version 1.0.0
 * @category Audit Logs
 * @package Dev
 * @since Nov 05, 2018
 */
@Controller
public class AuditLogController {

    private DatatablesInterface datatable;

    @Autowired
    public AuditLogController(DatatablesInterface datatable) {
this.datatable = datatable;
    }

    @RequestMapping("/audit-logs")
    public ModelAndView index(HttpServletRequest request) {
        View view = new View("configs/audit-logs");

        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {

            //Set-up data
            datatable
                    .select("str(a.createdOn; 'YYYY-MM-DD HH24:MI'), CONCAT(b.firstName; ' '; b.surname ), a.activity, a.status, a.oldValues, a.newValues ")
                    .from("AuditTrail a LEFT JOIN a.userLink b");

            return view.sendJSON(datatable.showTable());
        }

        return view.getView();
    }
}
