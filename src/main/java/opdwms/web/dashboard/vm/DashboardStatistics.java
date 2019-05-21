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
    private long percentageOverload = 0 ;
    private long percentageWithinLimit = 0 ;

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

    public long getPercentageOverload() {
        return percentageOverload;
    }

    public void setPercentageOverload(long percentageOverload) {
        this.percentageOverload = percentageOverload;
    }

    public long getPercentageWthinLimit() {
        return percentageWithinLimit;
    }

    public void setPercentageWthinLimit(long percentageWthinLimit) {
        this.percentageWithinLimit = percentageWthinLimit;
    }

    public void setPercentage() {
        this.percentageOverload = overloadVehicles != 0 ? (overloadVehicles * 100)/ weighedVehicles : 0;
        this.percentageWithinLimit = percentageOverload != 0 ?100 - percentageOverload : 0;
    }
}
