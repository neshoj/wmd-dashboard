package opdwms.web.hardwaremonitoring.controller;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class HardwareMonitoringController {

    @Autowired
    private DatatablesInterface datatable;

//    @MessageMapping("/connect")
//    @SendTo("/topic/hardware-monitoring")
//    public void initiateConnection(OperatorRequest request) throws Exception {
//        System.out.println("Connection request made " + request.getAction());
//    }


    @RequestMapping("/hardware-monitoring")
    public ModelAndView hardwareMonitoring(HttpServletRequest request) {
        View view = new View("hardware-monitoring/hardware-monitoring");

        // Fetch the table data
        if (AjaxUtils.isAjaxRequest(request)) {

            //Set-up data
            datatable
                    .select("a.siteName, a.siteIPAddress, a.deviceName, str(a.lastUpdatedOn; 'YYYY-MM-DD HH24:MI')")
                    .from("HardwareMonitoring a");

            return view.sendJSON(datatable.showTable());
        }

        return view.getView();
    }

}
