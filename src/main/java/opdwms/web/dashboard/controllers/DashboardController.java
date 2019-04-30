package opdwms.web.dashboard.controllers;

import opdwms.api.services.ProcessingInboundWeighingTransactions;
import opdwms.core.template.AjaxUtils;
import opdwms.core.template.AppConstants;
import opdwms.core.template.View;
import opdwms.core.template.datatables.DatatablesInterface;
import opdwms.web.dashboard.DashboardServiceInterface;
import opdwms.web.dashboard.vm.CencusBarChartData;
import opdwms.web.dashboard.vm.ChartDataPie;
import opdwms.web.usermanager.entities.UserTypes;
import opdwms.web.weighbridgestations.repository.WeighbridgeStationsRepository;
import opdwms.web.weighingtransactions.entities.TaggingTransactions;
import opdwms.web.weighingtransactions.modal.LineChartData;
import opdwms.web.weighingtransactions.repositories.WeighbridgeTransactionsRepository;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Ignatius
 * @version 1.0.0
 * @category Dashboard
 * @package Dev
 * @since Nov 05, 2018
 */
@Controller
public class DashboardController {

    @Autowired
    private DatatablesInterface dataTable;
    @Autowired
    private WeighbridgeStationsRepository weighbridgeStationsRepository;
    @Autowired
    private WeighbridgeTransactionsRepository weighbridgeTransactionsRepository;
    @Autowired
    private DashboardServiceInterface dashboardService;

    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request) throws Exception {
        View view = new View("dashboard/default-view");
        String parentType = (String) request.getSession().getAttribute("_userParentType");

        if (
                UserTypes.AEA_ADMIN.equals(parentType) || UserTypes.KENHA_ADMIN.equals(parentType) ||
                        UserTypes.AEA_OPERATIONS_MANAGER.equals(parentType) || UserTypes.KENHA_AXLE_LOAD_CONTROL_OFFICER.equals(parentType) ||
                        UserTypes.AEA_WEIGHBRIDGE_MANAGER.equals(parentType)
        )
            view = new View("dashboard/default-view");

        if (AjaxUtils.isAjaxRequest(request)) {

            String action = request.getParameter("action");
            //When creating a record
            if (null != action && "fetch-line-chart".equals(action))
                return view.sendJSON(fetchLineChartData(request));

            String calendarStart = request.getParameter("start");

            if (null != calendarStart)
                return view.sendJSON(dashboardService.fetchCalendarData(request));

                //When fetching table data
            else
                return fetchTableInfo(request, view);

        }

        return view
                .addAttribute("data", dashboardService.fetchStatistics(request))
                .addAttribute("weighbridges", weighbridgeStationsRepository.findAllByFlag(AppConstants.STATUS_ACTIVERECORD))
                .addAttribute("chartData", fetchLineChartData(request))
                .getView();
    }


    private Map<String, Object> fetchLineChartData(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
            Date parse = sdf.parse(new LocalDate().minusMonths(6).withDayOfMonth(1).toString("YYYY-MM-DD"));

        Collection<LineChartData> overloadData = weighbridgeTransactionsRepository.fetchWeighingCountBasedOnStatusGroupedByMonthlyDate(ProcessingInboundWeighingTransactions.GVM_OVERLOAD,parse);
        Collection<LineChartData> withinLimitData = weighbridgeTransactionsRepository.fetchWeighingCountBasedOnStatusGroupedByMonthlyDate(ProcessingInboundWeighingTransactions.GVM_WITHIN,parse);

        ArrayList<ChartDataPie> pieChartData = new ArrayList<>();
        pieChartData.add(new ChartDataPie("Within Limit", weighbridgeTransactionsRepository.fetchCountOfWeighingBasedOnStatus(ProcessingInboundWeighingTransactions.GVM_WITHIN)));
        pieChartData.add(new ChartDataPie("Within tolerance", weighbridgeTransactionsRepository.fetchCountOfWeighingBasedOnStatus(ProcessingInboundWeighingTransactions.GVM_WITHIN_PERMISSIBLE)));
        pieChartData.add(new ChartDataPie("Overloads", weighbridgeTransactionsRepository.fetchCountOfWeighingBasedOnStatus(ProcessingInboundWeighingTransactions.GVM_OVERLOAD)));

        List<CencusBarChartData> censusBarChartData = weighbridgeTransactionsRepository.fetchAxleConfigurationCensus();

        Long clearTags = weighbridgeTransactionsRepository.fetchCountOfTaggingBasedOnStatus(TaggingTransactions.CLEARED_TAGS);
        Long pendingTags = weighbridgeTransactionsRepository.fetchCountOfTaggingBasedOnStatus(TaggingTransactions.PENDING_TAGS);

        map.put("censusBarChartData", censusBarChartData);
        map.put("pieChartData", pieChartData);
        map.put("withinLimit", withinLimitData);
        map.put("overload", overloadData);
        map.put("clearTags", clearTags);
        map.put("pendingTags", pendingTags);
        map.put("status", "00");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return map;
    }

    /**
     * Fetch calendar data
     *
     * @param request
     * @param view
     * @return ModelAndView
     */
    private ModelAndView fetchTableInfo(HttpServletRequest request, View view) {
        Long parentNo = (Long) request.getSession().getAttribute("_userParentNo");
        String parentType = (String) request.getSession().getAttribute("_userParentType");

        if (StringUtils.isEmpty(parentType)) {
            dataTable
                    .nativeSQL(true)
                    .select(" a.name, SUM(c.amount)")
                    .from("merchants a")
                    .from("LEFT JOIN terminals b ON b.merchant_no = a.id ")
                    .from("LEFT JOIN transactions c ON c.terminal_no = b.id ")
                    .groupBy("a.name ")
            ;
        }

        //When serving merchants
        else {
            dataTable
                    .nativeSQL(true)
                    .select(" a.name, FORMAT( COALESCE( SUM(b.amount); 0) ; 2)")
                    .from("outlets a")
                    .from("LEFT JOIN transactions b ON b.outlet_no = a.id ")
                    .where("a.merchant_no = :merchantNo")
                    .groupBy("a.name ")
                    .setParameter("merchantNo", parentNo)
            ;
        }

        return view.sendJSON(dataTable.showTable());
    }


}
