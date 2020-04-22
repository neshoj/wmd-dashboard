package opdwms.web.reports.controllers;

import opdwms.core.export.ExportExcelInterface;
import opdwms.core.template.datatables.DatatablesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

@Controller
public class AbstractExcelReportController {

  @Autowired
  protected DatatablesInterface dataTable;

  @Autowired
  @Qualifier("excelGridExportService")
  protected ExportExcelInterface excelGridExportService;

  /**
   * Set Date Range data filters
   *
   * @param request
   * @param transactionAlias
   */
  public void setDateFilters(HttpServletRequest request, String transactionAlias) {
    String startDate = request.getParameter("startDate");
    String endDate = request.getParameter("endDate");
    String query = "";

    // If the dates have not been defined, end here
    if (null == startDate) return;

    // When no date has been set
    if (StringUtils.isEmpty(startDate))
      startDate = Calendar.getInstance().get(Calendar.YEAR) + "-01-01";
    if (StringUtils.isEmpty(endDate))
      endDate = Calendar.getInstance().get(Calendar.YEAR) + "-12-31";

    // When the start date and end date are the same
    if (startDate.equals(endDate)) {
      query = String.format("DATE(%s.transaction_date) = :startDate ", transactionAlias);
      dataTable.where(query).setParameter("startDate", startDate);
    }

    // when the date range is defined
    else {
      startDate = startDate + " 00:00:00";
      endDate = endDate + " 23:59:00";
      query =
          String.format(
              "DATE(%s.transaction_date) BETWEEN :startDate AND :endDate ", transactionAlias);
      dataTable.where(query).setParameter("startDate", startDate).setParameter("endDate", endDate);
    }
  }

  /**
   * Set Date Range data filters
   *
   * @param request
   * @param paymentModeAlias
   */
  protected void setPaymentModeFilter(HttpServletRequest request, String paymentModeAlias) {
    String paymentMode = request.getParameter("paymentMode");
    String query = "";

    // If the payment mode have not been defined, end here
    if (StringUtils.isEmpty(paymentMode)) return;

    query = String.format("%s.code = :paymentModeCode ", paymentModeAlias);
    dataTable.where(query).setParameter("paymentModeCode", paymentMode.trim());
  }

  /**
   * Set Date Range data filters
   *
   * @param request
   * @param transactionAlias
   */
  protected void setPaymentMode(HttpServletRequest request, String transactionAlias) {
    String paymentMode = request.getParameter("paymentMode");

    // If the payment mode have not been defined, end here
    if (StringUtils.isEmpty(paymentMode)) return;

    dataTable
        .from(
            String.format(
                "LEFT JOIN payment_modes pm ON pm.id = %s.payment_mode_no", transactionAlias))
        .where("pm.code = :paymentModeCode")
        .setParameter("paymentModeCode", paymentMode.trim());
  }

  protected void setTaggingStatus(HttpServletRequest request, String transactionAlias) {
    String actionTaken = request.getParameter("actionTaken");

    // If the payment mode have not been defined, end here
    if (StringUtils.isEmpty(actionTaken)) return;

    dataTable
            .where(String.format("%s.tag_status = :status", transactionAlias))
            .setParameter("status", actionTaken.trim());
  }

  protected void setWeighbridgeStationNo(HttpServletRequest request, String transactionAlias) {
    String weighbridgeStationNo = request.getParameter("wbsNo");

    // If the payment mode have not been defined, end here
    if (StringUtils.isEmpty(weighbridgeStationNo)) return;

    dataTable
            .where(String.format("%s.weighbridge_no = :wbsNo", transactionAlias))
            .setParameter("wbsNo", weighbridgeStationNo.trim());
  }


  /**
   * Using the request parameter provided, generate the expected response document
   *
   * @param request
   * @param response
   * @param filename
   * @param format
   * @throws IOException
   */
  protected void generateDoc(
          HttpServletRequest request, HttpServletResponse response, String filename, String format)
      throws IOException {
    HttpSession session = request.getSession();
    ExportExcelInterface exportService;

    // The service to use
    exportService = excelGridExportService;

    // The columns in the result set
    String[] columns = (String[]) session.getAttribute("columns");
    if (null == columns) throw new IOException("The result column set has not been defined!");
    exportService.setColumns(columns);

    // Set the query
    String query = (String) session.getAttribute("requestSQL");
    if (null == query) throw new IOException("The request sql has not been defined!");
    exportService.setQuery(query).nativeSQL(true);

    // Set the parameters that have been defined
    if (null != session.getAttribute("requestParam")) {
      Map<String, Object> map = (Map<String, Object>) session.getAttribute("requestParam");
      for (Map.Entry<String, Object> p : map.entrySet())
        exportService.setParameter(p.getKey(), p.getValue());
    }
    if (null != session.getAttribute("requestParamList")) {
      Map<String, Collection> map =
          (Map<String, Collection>) session.getAttribute("requestParamList");
      for (Map.Entry<String, Collection> p : map.entrySet())
        exportService.setParameterList(p.getKey(), p.getValue());
    }

    // The parameters that will be passed to the service
    exportService.setResponse(response).setFileName(filename).generateDoc();
  }
}
