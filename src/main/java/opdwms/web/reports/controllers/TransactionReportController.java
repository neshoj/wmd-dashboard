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
public class TransactionReportController extends AbstractReportController {

    @Autowired
    private WeighbridgeStationsRepository weighbridgeStationsRepository;

    @RequestMapping(value = "/transaction-report")
    public ModelAndView index(HttpServletRequest request) {
        String parentType = (String) request.getSession().getAttribute("_userParentType");
        View view = new View("reports/transaction-reports");

        if (AjaxUtils.isAjaxRequest(request)) {
            return fetchTableData(request, view);
        }

        return view
                .addAttribute("weighbridgestations", weighbridgeStationsRepository.findAllByFlag(AppConstants.STATUS_ACTIVERECORD))
                .getView();
    }

    @RequestMapping(value = "/transaction-report/{format}", method = RequestMethod.GET)
    public void export(
            HttpServletRequest request,
            HttpServletResponse response,
            @PathVariable("format") String format) throws IOException {
        ReportMetaData reportMetaData = new ReportMetaData();
        List<WeighbridgeStations> stationsRepositoryAllByFlag = weighbridgeStationsRepository.findAllByFlag(AppConstants.STATUS_ACTIVERECORD);
        if (stationsRepositoryAllByFlag.size() > 0) {
            WeighbridgeStations weighbridgeStations = stationsRepositoryAllByFlag.get(0);
            reportMetaData.setStationName(weighbridgeStations.getName())
                    .setStationCode(weighbridgeStations.getStationCode())
                    .setWbsLocation(weighbridgeStations.getLocation())
                    .setReportTitle("TRANSACTION BASED REPORT");
        }
        generateDoc(request, response, "transaction-report_" + new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss").format(new Date()), format, "DailyReport", reportMetaData);


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
                    "No Of Transactions",
                    "Total GVM (kg)"
            };

            dataTable
                    .nativeSQL(true)
                    .select("DATE_FORMAT(a.transaction_date; '%Y-%m-%d'), COALESCE(COUNT(a.id); 0), FORMAT(sum(a.vehicleGVM) ; 0), DATE_FORMAT(a.transaction_date; '%Y-%m-%d') ")
                    .from("weighing_transactions a ")
                    .groupBy("DATE_FORMAT(a.transaction_date, '%Y-%m-%d')");

            //Set Date Filters
            setDateFilters(request, "a");

            setWeighActionVerdict(request, "a");


            setWeighbridgeStationNo(request, "a");

        }

        //When Operator ID is defined
        else {

            // Define the columns
            columns = new String[]{
                    "Date",
                    "Ticket",
                    "Station",
                    "Reg. No",
                    "Axle Class",
                    "GVM",
                    "1st Axle(Kg)",
                    "2nd Axle(Kg)",
                    "3rd Axle(Kg)",
                    "4th Axle(Kg)",
                    "5th Axle(Kg)",
                    "6th Axle(Kg)",
                    "7th Axle(Kg)",
                    "Operator",
                    "Shift",
                    "Action",
                    "Permit"
            };

            dataTable
                    .nativeSQL(true)
                    .select("DATE_FORMAT(a.transaction_date; '%Y-%m-%d %H:%i'), a.ticket_no, b.name, a.vehicle_no, a.axle_configuration, a.vehicleGVM, a.first_axle_weight, a.second_axle_weight, a.third_axle_weight, a.fourth_axle_weight, a.fifth_axle_weight, a.sixth_axle_weight, a.seventh_axle_weight, a.operator, a.wbt_shift, a.action_taken, a.permit_no ")
                    .from("weighing_transactions a LEFT JOIN weighbridge_stations  b ON b.id = a.weighbridge_no ")
                    .where("DATE( a.transaction_date) = :createdOn ")
                    .setParameter("createdOn", transactionDate);

            //Set Date Filters
//            setDateFilters(request, "a");

            setWeighActionVerdict(request, "a");

            setWeighbridgeStationNo(request, "a");
        }

        ExportService.init(dataTable, request, columns);

        return view.sendJSON(dataTable.showTable());

    }
}
