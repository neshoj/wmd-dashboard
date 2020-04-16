package opdwms.web.dashboard.services;

import opdwms.web.dashboard.DashboardServiceInterface;
import opdwms.web.dashboard.vm.DashboardStatistics;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @category Dashboard
 * @package Dev
 * @since Nov 05, 2018
 * @author Ignatius
 * @version 1.0.0
 */
@Service
@Transactional
public class DashboardService implements DashboardServiceInterface {

  @PersistenceContext private EntityManager entityManager;

  /**
   * Fetch dashboard statistics
   *
   * @param request
   * @return Dashboard Statistics
   */
  @Override
  public DashboardStatistics fetchStatistics(HttpServletRequest request) {
    String parentType = (String) request.getSession().getAttribute("_userParentType");
    Long parentNo = (Long) request.getSession().getAttribute("_userParentNo");
    Session session = entityManager.unwrap(Session.class);

    //        if( StringUtils.isEmpty( parentType ) )
    return statistics(session);
  }

  private DashboardStatistics statistics(Session session) {
    DashboardStatistics data = new DashboardStatistics();
    try {

      DateFormat df = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
      String startDate = df.format(new Date());

      DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");
      Date qStartDate = dateFormat.parse(startDate + " 00:00");
      Date qEndDate = dateFormat.parse(startDate + " 23:59");

      data.setWeighedVehicles(getWeighTransactionsDoneToday(session, qStartDate, qEndDate));

      data.setTaggedVehicle(getVehiclesTaggedToday(session, qStartDate, qEndDate));

    } catch (ParseException e) {
      e.printStackTrace();
    }
    return data;
  }

  private BigInteger getVehiclesTaggedToday(Session session, Date qStartDate, Date qEndDate) {
    Query rawHql;
    StringBuilder query = new StringBuilder();
    query
        .append("SELECT COUNT( * ) ")
        .append("FROM tagging_transactions a ")
        .append("WHERE DATE(a.transaction_date)  BETWEEN :startDate AND :endDate ");

    rawHql = session.createNativeQuery(query.toString())
            .setParameter("startDate", qStartDate)
            .setParameter("endDate", qEndDate);
    return (BigInteger) rawHql.uniqueResult();
  }

    BigInteger getWeighTransactionsDoneToday(Session session, Date qStartDate, Date qEndDate) {
    Query rawHql;
    StringBuilder query = new StringBuilder();
    query
        .append("SELECT COUNT( * ) ")
        .append("FROM weighing_transactions a ")
        .append("WHERE DATE(a.transaction_date) BETWEEN :startDate AND :endDate ");

    rawHql = session.createNativeQuery(query.toString())
            .setParameter("startDate", qStartDate)
            .setParameter("endDate", qEndDate);
    return (BigInteger) rawHql.uniqueResult();
  }

  /**
   * Fetch calendar data
   *
   * @param request
   * @return List<Object>
   */
  @Override
  public List<Object> fetchCalendarData(final HttpServletRequest request) throws Exception {
    Long parentNo = (Long) request.getSession().getAttribute("_userParentNo");
    String parentType = (String) request.getSession().getAttribute("_userParentType");
    String startDate = request.getParameter("start");
    String endDate = request.getParameter("end");
    List<Object> response = new ArrayList<>();
    Session session = entityManager.unwrap(Session.class);

    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");

    Date qStartDate = dateFormat.parse(startDate + " 00:00");
    Date qEndDate = dateFormat.parse(endDate + " 23:59");

    System.err.println(parentType);

    // Create a raw sql query
    Query rawHql;
    StringBuilder query = new StringBuilder();
    query
        .append("SELECT DATE(a.transaction_date), ")
        .append("COUNT( * ), ")
        .append("(SUM( FORMAT(a.vehicleGVM , 3)))  ")
        .append("FROM weighing_transactions a ")
        .append("WHERE DATE(a.transaction_date) BETWEEN :startDate AND :endDate ")
        .append("GROUP BY DATE(a.transaction_date) ");

    rawHql =
        session
            .createNativeQuery(query.toString())
            .setParameter("startDate", startDate)
            .setParameter("endDate", endDate);

    List<Object[]> data = rawHql.list();
    DateTime dtStartTime = new DateTime(startDate);
    DateTime dtEndTime = new DateTime(endDate);

    BigDecimal count = BigDecimal.ZERO;
    double totalGVM = 0;

    for (DateTime date = dtStartTime;
        date.isBefore(dtEndTime.plusDays(1));
        date = date.plusDays(1)) {
      for (Object[] obj : data) {
        String _date = (new SimpleDateFormat("yyyy-MM-dd")).format((java.util.Date) obj[0]);

        if (_date.equals(date.toString("yyyy-MM-dd"))) {
          count = BigDecimal.valueOf(((BigInteger) obj[1]).intValue());
          totalGVM = (Double) obj[2];

          Map<String, Object> node = new HashMap<>();
          node.put("start", _date);
          node.put("end", _date);
          node.put("title", String.format("Vehicles: %d", count.intValue()));
          node.put("url", "/weighing-transactions");
          node.put("backgroundColor", "#DDB65DFF");
          node.put("borderColor", "#DDB65DFF");
          response.add(node);

          Map<String, Object> node2 = new HashMap<>();
          node2.put("start", _date);
          node2.put("end", _date);
          node2.put("title", String.format("GVM: %,.2f", totalGVM));
          node.put("url", "/weighing-transactions");
          node2.put("backgroundColor", "#6E4C1EFF");
          node2.put("borderColor", "#6E4C1EFF");
          response.add(node2);

          break;
        }
      }
    }

    return response;
  }
}
