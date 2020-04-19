package opdwms.web.dashboard;

import opdwms.web.dashboard.vm.CensusStats;
import opdwms.web.dashboard.vm.DashboardStatistics;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @category Dashboard
 * @package Dev
 * @since Nov 05, 2018
 * @author Ignatius
 * @version 1.0.0
 */
public interface DashboardServiceInterface {

  DashboardStatistics fetchStatistics(HttpServletRequest request);

    CensusStats fetchCensusStatistics(HttpServletRequest request);

  List<Object> fetchCalendarData(final HttpServletRequest request) throws Exception;
}
