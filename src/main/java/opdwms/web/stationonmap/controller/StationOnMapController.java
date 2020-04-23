package opdwms.web.stationonmap.controller;

import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class StationOnMapController {

  @Autowired private DatatablesInterface datatable;

  //    @MessageMapping("/connect")
  //    @SendTo("/topic/weighing-transactions")
  //    public void initiateConnection(OperatorRequest request) throws Exception {
  //        System.out.println("Connection request made " + request.getAction());
  //    }

  @RequestMapping("/station-on-map")
  public ModelAndView stationOnMap(HttpServletRequest request) {
    View view = new View("station-on-map/station-on-map");
    // Fetch the table data
    //        if (AjaxUtils.isAjaxRequest(request)) {
    //
    //            //Set-up data
    //            datatable
    //                    .select("str(a.transactionDate; 'YYYY-MM-DD HH24:MI'), a.ticketNo, b.name,
    // a.vehicleNo," +
    //                            " a.axleConfiguration, a.vehicleGVM, a.actionTaken")
    //                    .from("WeighingTransactions a LEFT JOIN WeighbridgeStations b ON b.id =
    // a.stationCode");
    //
    //            return view.sendJSON(datatable.showTable());
    //        }

    return view.getView();
  }
}
