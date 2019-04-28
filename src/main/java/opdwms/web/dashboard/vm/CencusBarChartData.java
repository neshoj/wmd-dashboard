package opdwms.web.dashboard.vm;

public class CencusBarChartData {
    Long count;
    String axleConfiguration;

    public CencusBarChartData(Long count, String axleConfiguration) {
        this.count = count;
        this.axleConfiguration = axleConfiguration;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getAxleConfiguration() {
        return axleConfiguration;
    }

    public void setAxleConfiguration(String axleConfiguration) {
        this.axleConfiguration = axleConfiguration;
    }
}
