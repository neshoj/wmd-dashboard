package opdwms.web.dashboard.vm;

/**
 * @category    Dashboard
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public class DashboardStatistics {
    private long weighedVehicles = 0;
    private long overloadVehicles = 2;
    private long weighedWithinLimits = 3 ;

    public long getWeighedVehicles() {
        return weighedVehicles;
    }

    public void setWeighedVehicles(long weighedVehicles) {
        this.weighedVehicles = weighedVehicles;
    }

    public long getOverloadVehicles() {
        return overloadVehicles;
    }

    public void setOverloadVehicles(long overloadVehicles) {
        this.overloadVehicles = overloadVehicles;
    }

    public long getWeighedWithinLimits() {
        return weighedWithinLimits;
    }

    public void setWeighedWithinLimits(long weighedWithinLimits) {
        this.weighedWithinLimits = weighedWithinLimits;
    }
}
