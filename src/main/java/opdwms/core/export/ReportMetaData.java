package opdwms.core.export;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportMetaData {
    private String date = new SimpleDateFormat("YYY-MM-dd HH:mm a").format(new Date());
    private String stationName = "N/A";
    private String reportTitle = "N/A";

    public String getDate() {
        return date;
    }

    public ReportMetaData setDate(String date) {
        this.date = date;
        return this;
    }

    public String getStationName() {
        return stationName;
    }

    public ReportMetaData setStationName(String stationName) {
        this.stationName = stationName;
        return this;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public ReportMetaData setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
        return this;
    }

}
