package opdwms.web.dataagentstatus.controller;

import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.weighbridgestations.WeighbridgeStationsServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DataAgentStatusController {

  @Autowired private WeighbridgeStationsServiceInterface entityService;
  @Autowired private DatatablesInterface dataTable;

  @RequestMapping(value = "/data-agent-status")
  public ModelAndView index(HttpServletRequest request) {
    View view = new View("data-agent/data-agent-status");
    String parentType = (String) request.getSession().getAttribute("_userParentType");

    if (AjaxUtils.isAjaxRequest(request)) {
      String action = request.getParameter("action");

      if (null != action) return handleRequests(request, view);

      // When fetching table data
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
  private ModelAndView handleRequests(HttpServletRequest request, View view) {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      String action = request.getParameter("action");

      //            //When creating a record
      //            if ("new".equals(action))
      //                map = this.entityService.saveRecord(request);

    } catch (Exception e) {
      e.printStackTrace();
      map.put("status", "01");
      map.put("message", "Internal server error: contact admin");
    }

    return view.sendJSON(map);
  }

  /**
   * Fetch table information
   *
   * @param request Current Request
   * @param view Current View
   * @return JsonView - json structure that DataTable can consume
   */
  private ModelAndView fetchTableInfo(HttpServletRequest request, View view) {

    dataTable
        .select("  a.weighbridgeStationsLink.name, str(a.lastUpdatedOn; 'YYYY-MM-DD HH24:MI:SS') ")
        .from("DataAgentStatus a ");

    return view.sendJSON(dataTable.showTable());
  }
}
