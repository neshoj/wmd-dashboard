package opdwms.web.dashboard.vm;

import java.math.BigInteger;

/**
 * @category Dashboard
 * @package Dev
 * @since Nov 05, 2018
 * @author Ignatius
 * @version 1.0.0
 */
public class DashboardStatistics {
  private BigInteger weighedVehicles = BigInteger.ZERO;
  private long census = 2;
  private BigInteger taggedVehicle = BigInteger.ZERO;
  private long hardware = 0;
  private long stations = 0;

  public BigInteger getWeighedVehicles() {
    return weighedVehicles;
  }

  public void setWeighedVehicles(BigInteger weighedVehicles) {
    this.weighedVehicles = weighedVehicles;
  }

  public long getCensus() {
    return census;
  }

  public DashboardStatistics setCensus(long census) {
    this.census = census;
    return this;
  }

  public BigInteger getTaggedVehicle() {
    return taggedVehicle;
  }

  public DashboardStatistics setTaggedVehicle(BigInteger taggedVehicle) {
    this.taggedVehicle = taggedVehicle;
    return this;
  }

  public long getHardware() {
    return hardware;
  }

  public DashboardStatistics setHardware(long hardware) {
    this.hardware = hardware;
    return this;
  }

  public long getStations() {
    return stations;
  }

  public DashboardStatistics setStations(long stations) {
    this.stations = stations;
    return this;
  }
}
