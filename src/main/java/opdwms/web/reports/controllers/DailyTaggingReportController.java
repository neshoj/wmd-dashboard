package opdwms.web.reports.controllers;

import opdwms.core.export.ExportService;
import opdwms.core.export.ReportMetaData;
import opdwms.core.template.AjaxUtils;
import opdwms.core.template.AppConstants;
import opdwms.core.template.View;
import opdwms.web.weighbridgestations.entities.WeighbridgeStations;
import opdwms.web.weighbridgestations.repository.WeighbridgeStationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class DailyTaggingReportController extends AbstractReportController {

    @Autowired
    private WeighbridgeStationsRepository weighbridgeStationsRepository;

    @RequestMapping(value = "/daily-tagging-report")
    public ModelAndView index(HttpServletRequest request) {
        String parentType = (String) request.getSession().getAttribute("_userParentType");
        View view = new View("reports/daily-tagging-report");

        if (AjaxUtils.isAjaxRequest(request)) {
            return fetchTableData(request, view);
        }

        return view
                .addAttribute("weighbridgestations", weighbridgeStationsRepository.findAllByFlag(AppConstants.STATUS_ACTIVERECORD))
                .getView();
    }

    @RequestMapping(value = "/daily-tagging-report/{format}", method = RequestMethod.GET)
    public void export(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("format") String format) throws IOException {
        ReportMetaData reportMetaData = new ReportMetaData();
        List<WeighbridgeStations> stationsRepositoryAllByFlag = weighbridgeStationsRepository.findAllByFlag(AppConstants.STATUS_ACTIVERECORD);
        if (stationsRepositoryAllByFlag.size() > 0) {
            WeighbridgeStations weighbridgeStations = stationsRepositoryAllByFlag.get(0);
            reportMetaData.setStationName(weighbridgeStations.getName())
                    .setReportTitle("DAILY TAGGING REPORT");
        }
        generateDoc(request, response, "daily-tagging-report_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date()), format, "DailyTaggingReport", reportMetaData);


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
            columns = new String[]{
                    "Date",
                    "Vehicles Tagged"
            };

            dataTable
                    .nativeSQL(true)
                    .select("DATE_FORMAT(a.transaction_date; '%Y-%m-%d'), COALESCE(COUNT(a.id); 0), DATE_FORMAT(a.transaction_date; '%Y-%m-%d') ")
                    .from("tagging_transactions a ")
                    .groupBy("DATE_FORMAT(a.transaction_date, '%Y-%m-%d')");

            //Set Date Filters
            setDateFilters(request, "a");

            setTaggingStatus(request, "a");

            setWeighbridgeStationNo(request, "a");

        }

        //When Operator ID is defined
        else {

            // Define the columns
            columns = new String[]{
                    "Date",
                    "Reg. No",
                    "Ticket No.",
                    "Transgression",
                    "Tag Location",
                    "Tagging System"
            };

            dataTable
                    .nativeSQL(true)
                    .select("DATE_FORMAT(a.transaction_date; '%Y-%m-%d %H:%i'), a.vehicle_no, a.tag_reference, a.transgression, a.weighbridge, a.tagging_system ")
                    .from("tagging_transactions a LEFT JOIN weighbridge_stations  b ON b.id = a.weighbridge_no ")
                    .where("DATE( a.transaction_date) = :createdOn ")
                    .setParameter("createdOn", transactionDate);

            //Set Date Filters
            setDateFilters(request, "a");
            // Tagging Status
            setTaggingStatus(request, "a");
            // Select Station
            setWeighbridgeStationNo(request, "a");
        }

        ExportService.init(dataTable, request, columns);

        return view.sendJSON(dataTable.showTable());

    }
}
