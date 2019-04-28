package opdwms.web.dashboard.vm;

public class ChartDataPie {
    String label;
    Long data;

    public ChartDataPie(String label, Long data) {
        this.label = label;
        this.data = data;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getData() {
        return data;
    }

    public void setData(Long data) {
        this.data = data;
    }
}
