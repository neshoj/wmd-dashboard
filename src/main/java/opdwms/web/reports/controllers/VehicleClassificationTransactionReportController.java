package opdwms.web.reports.controllers;

import opdwms.core.export.ExportService;
import opdwms.core.export.ReportMetaData;
import opdwms.core.template.AjaxUtils;
import opdwms.core.template.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class VehicleClassificationTransactionReportController extends AbstractExcelReportController {

  @Autowired
  public VehicleClassificationTransactionReportController() {}

  @RequestMapping(value = "/vehicle-class-transaction-report")
  public ModelAndView index(HttpServletRequest request) {
    String parentType = (String) request.getSession().getAttribute("_userParentType");
    View view = new View("reports/vehicle-class-transaction-report");

    if (AjaxUtils.isAjaxRequest(request)) {
      return fetchTableData(request, view);
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
  private ModelAndView fetchTableData(HttpServletRequest request, View view) {
    Long parentNo = (Long) request.getSession().getAttribute("_userParentNo");
    String parentType = (String) request.getSession().getAttribute("_userParentType");
    String transactionDate = request.getParameter("data");
    String columns[] = null;

    if (StringUtils.isEmpty(transactionDate)) {
      // Define the columns
      columns = new String[] {"Vehicle Classification", "Vehicles Weighed"};

      dataTable
          .nativeSQL(true)
          .select("a.axle_configuration, COALESCE(COUNT(a.id); 0), a.axle_configuration ")
          .from("weighing_transactions a ")
          .groupBy(" a.axle_configuration");

      // Set Date Filters
      setDateFilters(request, "a");

    }

    else {

      // Define the columns
      columns =
          new String[] {
            "Date",
            "Reg. No",
            "Ticket",
            "Station",
            "Operator",
            "Shift",
            "Axle Class",
            "GVM",
            "Permit"
          };
      String weighbridgeStationNo = request.getParameter("data");
      dataTable
          .nativeSQL(true)
          .select(
              "DATE_FORMAT(a.transaction_date; '%Y-%m-%d %H:%i'), a.vehicle_no, a.ticket_no, b.name, a.operator, a.wbt_shift,  a.axle_configuration, FORMAT(a.vehicleGVM; 0), a.permit_no ")
          .from(
              "weighing_transactions a LEFT JOIN weighbridge_stations  b ON b.id = a.weighbridge_no ")
          .where("a.axle_configuration = :vehicleClass") .setParameter("vehicleClass", weighbridgeStationNo.trim());

      // Set Date Filters
      setDateFilters(request, "a");
    }

    ExportService.init(dataTable, request, columns);

    return view.sendJSON(dataTable.showTable());
  }

  @RequestMapping(value = "/vehicle-class-transaction-report/xls", method = RequestMethod.GET)
  public void exportReportInExcel(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    ReportMetaData reportMetaData = new ReportMetaData();
    reportMetaData.setReportTitle("Weigh Station Transaction Report");
    generateDoc(
        request,
        response,
        "vehicle-classification-transaction-report"
            + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date()),
        "xls");
  }
}
