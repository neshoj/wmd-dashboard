package opdwms.web.trafficcensus.controller;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.trafficcensus.repository.TrafficCensusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TrafficCensusControllers {

    private DatatablesInterface datatable;
    private TrafficCensusRepository trafficCensusRepository;

    @Autowired
    public TrafficCensusControllers(DatatablesInterface datatable,
                                    TrafficCensusRepository trafficCensusRepository) {
        this.datatable = datatable;
        this.trafficCensusRepository = trafficCensusRepository;
    }


    @RequestMapping("/traffic-census")
    public ModelAndView weighingTransaction(HttpServletRequest request) {
        View view = new View("traffic-census/traffic-census");

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
