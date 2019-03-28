package atlas.web.dashboard.vm;

import java.math.BigDecimal;

/**
 * @category    Dashboard
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public class DashboardStatitics {

    private long users = 0;
    private long merchants = 0;
    private long weighbridgeStations = 0;
    private long weighbridges = 0;
    private BigDecimal total = BigDecimal.ZERO;
    private String amount = "0.00";


    public long getUsers() {
        return users;
    }

    public void setUsers(long users) {
        this.users = users;
    }

    public long getMerchants() {
        return merchants;
    }

    public void setMerchants(long merchants) {
        this.merchants = merchants;
    }

    public long getWeighbridgeStations() {
        return weighbridgeStations;
    }

    public void setWeighbridgeStations(long weighbridgeStations) {
        this.weighbridgeStations = weighbridgeStations;
    }

    public long getWeighbridges() {
        return weighbridges;
    }

    public void setWeighbridges(long weighbridges) {
        this.weighbridges = weighbridges;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
        this.amount = String.format("%,.2f", total);
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
