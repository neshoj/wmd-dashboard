package opdwms.web.reports.services;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
    private static final String GVM_OVERLOAD = "2";


    @Autowired
    public ReportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Scheduled(cron = "${app.dailytrucksweighed.cron.expression}")
    public void scheduledTrucksWeighedDailyTask(){

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);

        Date yesterday = calendar.getTime();
        this.generateTrucksWeighedDailyReport(yesterday);
    }


    private void generateTrucksWeighedDailyReport(Date date) {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String reportDate = formatter.format(date);
            String weighedTrucksQuery = "SELECT COUNT(id) from weighing_transactions where cast(transaction_date AS DATE) ='" + reportDate + "'";
           logger.debug("weighedTrucksQuery: {}", weighedTrucksQuery);

            Integer weighedScale_N = jdbcTemplate.queryForObject(weighedTrucksQuery, Integer.class);
            Integer manuallyWeighed_M = 0, weighedHSWIM_Q = 0;
            Integer totalWeighed_X = manuallyWeighed_M + weighedScale_N;

            //TODO census
            Integer vehicles_J = 0, vehicles_V = 0, vehicles_W = 0;
            Integer trafficCensus_K = vehicles_J + vehicles_V + vehicles_W;
            Integer totalTraffic_T = trafficCensus_K + totalWeighed_X;


            //Overloaded
            String totalOverloadedQuery = "SELECT COUNT(id) from weighing_transactions where status='" + GVM_OVERLOAD +
                    "' AND cast(transaction_date AS DATE) ='" + reportDate + "'";
            logger.debug("totalOverloadedQuery: {}",totalOverloadedQuery);
            Integer totalOverloaded = jdbcTemplate.queryForObject(totalOverloadedQuery, Integer.class);

            Integer totalCharged = 0;
            Double percentageTotalCharged = 0.00;
            Integer warned_A = 0;
            Double percentageWarned = 0.00;
            Integer warnedAndChargedInCourt_Y = 0;
            Integer chargedButRedistributed_R = 0;

            String specialReleaseTrucksQuery = "SELECT COUNT(id) from weighing_transactions where permit_no=0" +
                    " AND cast(transaction_date AS DATE) ='" + reportDate + "'";
            logger.debug("specialReleaseTrucksQuery: {}", specialReleaseTrucksQuery);
            Integer specialReleaseTrucks_G = jdbcTemplate.queryForObject(specialReleaseTrucksQuery, Integer.class);
            Integer impoundedAndProhibited_P = 0;
            Integer casesClearedInCourts_B = 0;
            Integer transgressions_L = 0;

            //Excemptions permits;
            Integer notWeighed = 0;
            Integer permitsWeighed = 0;
            Integer totalExemptionPermits = notWeighed + permitsWeighed;


            JSONObject rootObject = new JSONObject();
            rootObject.put("weighedHSWIM_Q", weighedHSWIM_Q);
            rootObject.put("weighedScale_N", weighedScale_N);
            rootObject.put("manuallyWeighed_M", manuallyWeighed_M);
            rootObject.put("totalWeighed_X", totalWeighed_X);
            rootObject.put("vehicles_J", vehicles_J);
            rootObject.put("vehicles_V", vehicles_V);
            rootObject.put("vehicles_W", vehicles_W);
            rootObject.put("trafficCensus_K", trafficCensus_K);
            rootObject.put("totalTraffic_T", totalTraffic_T);
            rootObject.put("totalOverloaded", totalOverloaded);
            rootObject.put("totalCharged", totalCharged);
            rootObject.put("percentageTotalCharged", percentageTotalCharged);
            rootObject.put("warned_A", warned_A);
            rootObject.put("percentageWarned", percentageWarned);
            rootObject.put("warnedAndChargedInCourt_Y", warnedAndChargedInCourt_Y);
            rootObject.put("chargedButRedistributed_R", chargedButRedistributed_R);
            rootObject.put("specialReleaseTrucks_G", specialReleaseTrucks_G);
            rootObject.put("impoundedAndProhibited_P", impoundedAndProhibited_P);
            rootObject.put("casesClearedInCourts_B", casesClearedInCourts_B);
            rootObject.put("transgressions_L", transgressions_L);
            rootObject.put("notWeighed", notWeighed);
            rootObject.put("permitsWeighed", permitsWeighed);
            rootObject.put("totalExemptionPermits", totalExemptionPermits);


            String checkReportSql = "SELECT * FROM vehicles_weighed_daily_reports WHERE date='" + reportDate + "' ";
            logger.debug("checkReportSql: {}", checkReportSql);
            List<Map<String, Object>> checkReportRes = jdbcTemplate.queryForList(checkReportSql);
            if (checkReportRes.isEmpty()) {
                logger.debug("No existing report found, creating new one");
                String insertionSql = "INSERT INTO vehicles_weighed_daily_reports(date,report_json) VALUES (?,?)";
                logger.debug("Making new generateTrucksWeighedDailyReport entry:  {}", insertionSql);
                Date finalReportDate = formatter.parse(reportDate);
                KeyHolder holder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(insertionSql, Statement.RETURN_GENERATED_KEYS);
                    ps.setDate(1, new java.sql.Date(finalReportDate.getTime()));
                    ps.setString(2, rootObject.toString());

                    return ps;
                }, holder);
            } else {
                logger.debug("Found existing reports of size {}", checkReportRes.size());
                Integer id = (int) checkReportRes.get(0).get("id");
                String updateReportSql = "UPDATE vehicles_weighed_daily_reports SET report_json=? WHERE id=? ";
                logger.debug("updating existing daily report: {}", updateReportSql);
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(updateReportSql, Statement.NO_GENERATED_KEYS);
                    ps.setString(1, rootObject.toString());
                    ps.setInt(2, id);
                    return ps;
                });
            }

        } catch (Exception e) {
            logger.error("generateTrucksWeighedDailyReport task failed: ", e);
            e.printStackTrace();
        }
    }

    private void generateVehiclesOverloadedOnGVMReport(Date date) {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String reportDate = formatter.format(date);
            //get station names first
            String getStationsQuery = "SELECT name FROM weighbridge_stations";

//        String getStationsQuery = "SELECT  DISTINCT ws.name FROM weighing_transactions WT LEFT JOIN weighbridge_stations ws ON WT.weighbridge_no = ws.id WHERE cast(wt.transaction_date AS DATE) = '2019-10-30'";
            List<String> list = jdbcTemplate.queryForList(getStationsQuery)
                    .stream()
                    .map(w -> (String) w.entrySet().iterator().next().getValue())
                    .collect(Collectors.toList());

            StringBuilder finalQueryBuilder = new StringBuilder();

            for (String station : list) {
                String stationSubQuery = "coalesce((SELECT count(ws.name) FROM weighbridge_stations ws2 where ws.id=ws2.id and ws.name='" + station + "'),0) AS '" + station + "'";
                finalQueryBuilder.append(",")
                        .append(stationSubQuery);
            }

            String query = "SELECT wt.axle_configuration" + finalQueryBuilder.toString() +
                    " FROM weighing_transactions wt" +
                    " LEFT JOIN weighbridge_stations ws ON wt.weighbridge_no = ws.id" +
                    " WHERE cast(wt.transaction_date AS DATE) = '" + reportDate + "' AND wt.status='" + GVM_OVERLOAD + "' GROUP BY wt.axle_configuration";

            System.out.println("final query: " + query);
            List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
            System.out.println("list size: " + result.size());

            JSONObject rootObject = new JSONObject();

            for (Map<String, Object> row : result) {
                JSONObject object = new JSONObject();
                String axleConfig = "";
                for (Map.Entry<String, Object> entry : row.entrySet()) {

                    if (entry.getKey().equals("axle_configuration")) {
                        axleConfig = (String) entry.getValue();
                    } else {
                        object.put((entry.getKey()), entry.getValue());
                    }
                }
                rootObject.put(axleConfig, object);
            }
            logger.debug("Generated report. Json value: {}", rootObject.toString());
            logger.debug("Generated report. Map value: {}", StringUtils.join(result));

            //update report or create new record

            //check if previous record exists
            String checkReportSql = "SELECT * FROM vehicles_overloaded_vhm_daily_reports WHERE date='" + reportDate + "' ";
            List<Map<String, Object>> checkReportRes = jdbcTemplate.queryForList(checkReportSql);

            if (checkReportRes.isEmpty()) {
                logger.debug("No existing report found, creating new one");
                String insertionSql = "INSERT INTO vehicles_overloaded_vhm_daily_reports(date,report_json,report_map) VALUES (?,?,?)";
                Date finalReportDate = formatter.parse(reportDate);
                System.out.println("report date: " + finalReportDate);
                KeyHolder holder = new GeneratedKeyHolder();
                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(insertionSql, Statement.RETURN_GENERATED_KEYS);
                    ps.setDate(1, new java.sql.Date(finalReportDate.getTime()));
                    ps.setString(2, rootObject.toString());
                    ps.setString(3, StringUtils.join(result));
                    return ps;
                }, holder);
            } else {
                logger.debug("Found existing reports of size {}", checkReportRes.size());
                Integer id = (int) checkReportRes.get(0).get("id");
                String updateReportSql = "UPDATE vehicles_overloaded_vhm_daily_reports SET report_json=?,report_map=? WHERE id=? ";

                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(updateReportSql, Statement.NO_GENERATED_KEYS);
                    ps.setString(1, rootObject.toString());
                    ps.setString(2, StringUtils.join(result));
                    ps.setInt(3, id);
                    return ps;
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

