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
  private BigInteger census = BigInteger.ZERO;
  private BigInteger taggedVehicle = BigInteger.ZERO;
  private BigInteger hardware = BigInteger.ZERO;
  private BigInteger stations = BigInteger.ZERO;

  public BigInteger getWeighedVehicles() {
    return weighedVehicles;
  }

  public void setWeighedVehicles(BigInteger weighedVehicles) {
    this.weighedVehicles = weighedVehicles;
  }

  public BigInteger getCensus() {
    return census;
  }

  public DashboardStatistics setCensus(BigInteger census) {
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

  public BigInteger getHardware() {
    return hardware;
  }

  public DashboardStatistics setHardware(BigInteger hardware) {
    this.hardware = hardware;
    return this;
  }

  public BigInteger getStations() {
    return stations;
  }

  public DashboardStatistics setStations(BigInteger stations) {
    this.stations = stations;
    return this;
  }
}
