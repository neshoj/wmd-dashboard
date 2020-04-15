package opdwms.web.reports.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);
    public static final String GVM_OVERLOAD = "2";


    @Autowired
    public ReportService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    @Scheduled(cron = "${app.daily.cron.expression}")
    public void scheduledGVMReportTask() {
        logger.info("starting cron job");
//        Date date = new Date();
        String sampleDate = "2019-02-22";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date;
        try {
            date = formatter.parse(sampleDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);

        Date yesterday = calendar.getTime();
        generateVehiclesOverloadedOnGVMReport(yesterday);
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

