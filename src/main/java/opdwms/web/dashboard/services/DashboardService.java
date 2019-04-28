package opdwms.web.dashboard.services;

import opdwms.api.services.ProcessingInboundWeighingTransactions;
import opdwms.web.dashboard.DashboardServiceInterface;
import opdwms.web.dashboard.vm.DashboardStatistics;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @category    Dashboard
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Service
@Transactional
public class DashboardService implements DashboardServiceInterface {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Fetch dashboard statistics
     *
     * @param request
     * @return Dashboard Statistics
     */
    @Override
    public DashboardStatistics fetchStatistics(HttpServletRequest request){
        String parentType = (String)request.getSession().getAttribute("_userParentType");
        Long parentNo = (Long) request.getSession().getAttribute("_userParentNo");
        Session session = entityManager.unwrap( Session.class );

//        if( StringUtils.isEmpty( parentType ) )
            return statistics( session );


    }

    private DashboardStatistics statistics(Session session ){
        DashboardStatistics data = new DashboardStatistics();

        // Weighed vehicles
        data.setWeighedVehicles( (Long) session.createQuery("SELECT COUNT(a) FROM WeighingTransactions a")
                .uniqueResult());

        // Overload vehicles
        data.setOverloadVehicles( (Long) session.createQuery("SELECT COUNT(a) FROM WeighingTransactions a WHERE a.status = :flag ")
                .setParameter("flag", ProcessingInboundWeighingTransactions.GVM_OVERLOAD)
                .uniqueResult());

        // Overload vehicles
        data.setWeighedWithinLimits( (Long) session.createQuery("SELECT COUNT(a) FROM WeighingTransactions a WHERE a.status = :flag ")
                .setParameter("flag", ProcessingInboundWeighingTransactions.GVM_WITHIN)
                .uniqueResult());

        return data;
    }

    /**
     * Fetch calendar data
     *
     * @param request
     * @return List<Object>
     */
    public List<Object> fetchCalendarDatav2(final HttpServletRequest request){
        Long parentNo = (Long) request.getSession().getAttribute("_userParentNo");
        String parentType = (String)request.getSession().getAttribute("_userParentType");
        String startDate = request.getParameter("start");
        String endDate = request.getParameter("end");
        List<Object> response = new ArrayList<>();
        Session session = entityManager.unwrap( Session.class );

        String qStartDate = startDate;
        String qEndDate = endDate;

        //Create a raw sql query
        Query rawHql;
        StringBuilder query = new StringBuilder();
        if( StringUtils.isEmpty( parentType ) ){
            query
                    .append("SELECT a.transaction_time, ")
//                    .append("SUM( CASE WHEN b.code = :mpesa THEN a.amount ELSE 0 END ), ")
//                    .append("SUM( CASE WHEN b.code = :cash THEN a.amount ELSE 0 END ), ")
                    .append("SUM( a.amount ) ")
                    .append("FROM transactions a ")
                    .append("LEFT JOIN payment_modes b ON b.id = a.payment_mode_no ")
                    .append("WHERE DATE(a.transaction_time) BETWEEN :startDate AND :endDate ")
                    .append("GROUP BY a.transaction_time ");

            rawHql = session.createNativeQuery( query.toString() )
//                    .setParameter("mpesa", PaymentModes.M_PESA )
//                    .setParameter("cash", PaymentModes.CASH )
                    .setParameter("startDate", startDate )
                    .setParameter("endDate", endDate );
        }

        //When a merchant is the current user
        else{

            query
                    .append("SELECT a.transaction_time, ")
//                    .append("SUM( CASE WHEN b.code = :mpesa THEN a.amount ELSE 0 END ), ")
//                    .append("SUM( CASE WHEN b.code = :cash THEN a.amount ELSE 0 END ), ")
                    .append("SUM( a.amount ) ")
                    .append("FROM transactions a ")
                    .append("LEFT JOIN payment_modes b ON b.id = a.payment_mode_no ")
                    .append("LEFT JOIN terminals c ON c.id = a.terminal_no ")
                    .append("WHERE DATE(a.transaction_time) BETWEEN :startDate AND :endDate ")
                    .append("AND c.merchant_no = :merchantNo ")
                    .append("GROUP BY a.transaction_time ");

            rawHql = session.createNativeQuery( query.toString() )
                    .setParameter("merchantNo", parentNo )
//                    .setParameter("mpesa", PaymentModes.M_PESA )
//                    .setParameter("cash", PaymentModes.CASH )
                    .setParameter("startDate", startDate )
                    .setParameter("endDate", endDate );
        }

        System.err.println("----------------------------------------");
        System.err.println( rawHql.getQueryString() );
        System.err.println( rawHql.getParameters() );
        System.err.println("----------------------------------------");

        List<Object[]> data = rawHql.list();
        DateTime dtStartTime = new DateTime( startDate );
        DateTime dtEndTime = new DateTime( endDate );

        BigDecimal mpesa = BigDecimal.ZERO;
        BigDecimal cash = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (DateTime date = dtStartTime; date.isBefore(dtEndTime.plusDays(1)); date = date.plusDays(1)) {
            for (Object[] obj : data) {
                String _date = (new SimpleDateFormat("yyyy-MM-dd")).format( (Date) obj[0] );

                if ( _date.equals( date.toString("yyyy-MM-dd") )) {
                    mpesa = (BigDecimal) obj[1];
                    cash = (BigDecimal) obj[2];
                    totalAmount = (BigDecimal) obj[3];

//                    Map<String, Object> node = new HashMap<>();
//                    node.put("start", _date);
//                    node.put("end", _date);
//                    node.put("title", String.format("M-Pesa: %,.2f", mpesa.setScale(2, RoundingMode.HALF_UP)));
//                    response.add( node );
//
//                    Map<String, Object> node2 = new HashMap<>();
//                    node2.put("start", _date);
//                    node2.put("end", _date);
//                    node2.put("title", String.format("Cash: %,.2f", cash.setScale(2, RoundingMode.HALF_UP)));
//                    response.add( node2 );

                    Map<String, Object> node3 = new HashMap<>();
                    node3.put("start", _date);
                    node3.put("end", _date);
                    node3.put("title", String.format("Total: %,.2f", totalAmount.setScale(2, RoundingMode.HALF_UP)));
                    node3.put("backgroundColor", "#967adc");
                    response.add ( node3 );

                    break;
                }
            }
        }

        return response;
    }

    /**
     * Fetch calendar data
     *
     * @param request
     * @return List<Object>
     */
    @Override
    public List<Object> fetchCalendarData(final HttpServletRequest request) throws Exception{
        Long parentNo = (Long) request.getSession().getAttribute("_userParentNo");
        String parentType = (String)request.getSession().getAttribute("_userParentType");
        String startDate = request.getParameter("start");
        String endDate = request.getParameter("end");
        List<Object> response = new ArrayList<>();
        Session session = entityManager.unwrap( Session.class );

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm");

        Date qStartDate = dateFormat.parse( startDate + " 00:00" );
        Date qEndDate = dateFormat.parse( endDate + " 23:59" );

        System.err.println( parentType );

        //Create a raw sql query
        Query rawHql;
        StringBuilder query = new StringBuilder();
        if( StringUtils.isEmpty( parentType ) ){
            query
                    .append("SELECT DATE(a.transaction_time), ")
//                    .append("SUM( CASE WHEN b.code = 'm-pesa' THEN a.amount ELSE 0 END ) AS mpesa, ")
//                    .append("SUM( CASE WHEN b.code = 'cash' THEN a.amount ELSE 0 END ) AS cash, ")
                    .append("SUM( a.amount ) total ")
                    .append("FROM transactions a ")
                    .append("LEFT JOIN payment_modes b on a.payment_mode_no = b.id ")
                    .append("WHERE DATE( a.transaction_time ) BETWEEN :startDate AND :endDate ")
                    .append("GROUP BY DATE(a.transaction_time) ");

            rawHql = session
                    .createNativeQuery( query.toString() )
//                    .setParameter("mpesa", PaymentModes.M_PESA )
//                    .setParameter("cash", PaymentModes.CASH )
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate )

            ;
        }

        //When a merchant is the current user
        else{

            query
                    .append("SELECT DATE(a.transaction_time), ")
//                    .append("SUM( CASE WHEN b.code = :mpesa THEN a.amount ELSE 0 END ), ")
//                    .append("SUM( CASE WHEN b.code = :cash THEN a.amount ELSE 0 END ), ")
                    .append("SUM( a.amount ) ")
                    .append("FROM transactions a ")
                    .append("LEFT JOIN payment_modes b ON b.id = a.payment_mode_no ")
                    .append("LEFT JOIN terminals c ON c.id = a.terminal_no ")
                    .append("WHERE DATE(a.transaction_time) BETWEEN :startDate AND :endDate ")
                    .append("AND c.merchant_no = :merchantNo ")
                    .append("GROUP BY DATE(a.transaction_time) ");

            rawHql = session.createNativeQuery( query.toString() )
                    .setParameter("merchantNo", parentNo )
//                    .setParameter("mpesa", PaymentModes.M_PESA )
//                    .setParameter("cash", PaymentModes.CASH )
                    .setParameter("startDate", startDate )
                    .setParameter("endDate", endDate );
        }

        List<Object[]> data = rawHql.list();
        DateTime dtStartTime = new DateTime( startDate );
        DateTime dtEndTime = new DateTime( endDate );

        BigDecimal mpesa = BigDecimal.ZERO;
        BigDecimal cash = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (DateTime date = dtStartTime; date.isBefore(dtEndTime.plusDays(1)); date = date.plusDays(1)) {
            for (Object[] obj : data) {
                String _date = (new SimpleDateFormat("yyyy-MM-dd")).format( (Date) obj[0] );

                if ( _date.equals( date.toString("yyyy-MM-dd") )) {
                    mpesa = (BigDecimal) obj[1];
                    cash = (BigDecimal) obj[2];
                    totalAmount = (BigDecimal) obj[3];

//                    Map<String, Object> node = new HashMap<>();
//                    node.put("start", _date);
//                    node.put("end", _date);
//                    node.put("title", String.format("M-Pesa: %,.2f", mpesa.setScale(2, RoundingMode.HALF_UP)));
//                    response.add( node );
//
//                    Map<String, Object> node2 = new HashMap<>();
//                    node2.put("start", _date);
//                    node2.put("end", _date);
//                    node2.put("title", String.format("Cash: %,.2f", cash.setScale(2, RoundingMode.HALF_UP)));
//                    response.add( node2 );

                    Map<String, Object> node3 = new HashMap<>();
                    node3.put("start", _date);
                    node3.put("end", _date);
                    node3.put("title", String.format("Total: %,.2f", totalAmount.setScale(2, RoundingMode.HALF_UP)));
                    node3.put("backgroundColor", "#967adc");
                    response.add ( node3 );

                    break;
                }
            }
        }

        return response;
    }


}
