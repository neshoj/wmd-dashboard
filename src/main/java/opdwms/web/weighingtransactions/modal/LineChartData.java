package opdwms.web.weighingtransactions.modal;


public class LineChartData {
    public String month;
    public Long count;

    public LineChartData(String month, Long count) {
        this.month = month;
        this.count = count;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
