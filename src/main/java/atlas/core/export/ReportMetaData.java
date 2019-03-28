package atlas.core.export;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportMetaData {
    private String date = new SimpleDateFormat("yyyy-mm-dd hh:mm a").format(new Date());
    private String wbsLocation = "N/A";
    private String stationName = "N/A";
    private String stationCode = "N/A";
    private String reportTitle = "N/A";

    public String getDate() {
        return date;
    }

    public ReportMetaData setDate(String date) {
        this.date = date;
        return this;
    }

    public String getWbsLocation() {
        return wbsLocation;
    }

    public ReportMetaData setWbsLocation(String wbsLocation) {
        this.wbsLocation = wbsLocation;
        return this;
    }

    public String getStationName() {
        return stationName;
    }

    public ReportMetaData setStationName(String stationName) {
        this.stationName = stationName;
        return this;
    }

    public String getStationCode() {
        return stationCode;
    }

    public ReportMetaData setStationCode(String stationCode) {
        this.stationCode = stationCode;
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
