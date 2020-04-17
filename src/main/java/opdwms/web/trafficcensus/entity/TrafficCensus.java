package opdwms.web.trafficcensus.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import opdwms.web.weighbridgestations.entities.WeighbridgeStations;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "traffic_census")
public class TrafficCensus {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "wb_station", length = 150)
  private String stations;

  @Column(name = "transaction_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date transactionDate;

  @Column(name = "census_hour")
  private String censusHour;

  @Column(name = "trucks_greater_than_7t")
  private String trucksGreaterThanSevenTonne;

  @Column(name = "trucks_between_3_5t_and_less_7t")
  private String trucksBetweenThreePointFiveAndLessThanSevenTonnest;

  @Column(name = "buses")
  private String buses;

  @Column(name = "weighbridge_no")
  private Long weighbridgeNo;

  @JoinColumn(
      name = "weighbridge_no",
      referencedColumnName = "ID",
      insertable = false,
      updatable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private WeighbridgeStations weighbridgeStationsLink;

  public Long getId() {
    return id;
  }

  public TrafficCensus setId(Long id) {
    this.id = id;
    return this;
  }

  public String getStations() {
    return stations;
  }

  public TrafficCensus setStations(String stations) {
    this.stations = stations;
    return this;
  }

  public Date getTransactionDate() {
    return transactionDate;
  }

  public TrafficCensus setTransactionDate(Date transactionDate) {
    this.transactionDate = transactionDate;
    return this;
  }

  public String getCensusHour() {
    return censusHour;
  }

  public TrafficCensus setCensusHour(String censusHour) {
    this.censusHour = censusHour;
    return this;
  }

  public String getTrucksGreaterThanSevenTonne() {
    return trucksGreaterThanSevenTonne;
  }

  public TrafficCensus setTrucksGreaterThanSevenTonne(String trucksGreaterThanSevenTonne) {
    this.trucksGreaterThanSevenTonne = trucksGreaterThanSevenTonne;
    return this;
  }

  public String getTrucksBetweenThreePointFiveAndLessThanSevenTonnest() {
    return trucksBetweenThreePointFiveAndLessThanSevenTonnest;
  }

  public TrafficCensus setTrucksBetweenThreePointFiveAndLessThanSevenTonnest(
      String trucksBetweenThreePointFiveAndLessThanSevenTonnest) {
    this.trucksBetweenThreePointFiveAndLessThanSevenTonnest =
        trucksBetweenThreePointFiveAndLessThanSevenTonnest;
    return this;
  }

  public String getBuses() {
    return buses;
  }

  public TrafficCensus setBuses(String buses) {
    this.buses = buses;
    return this;
  }

  public Long getWeighbridgeNo() {
    return weighbridgeNo;
  }

  public TrafficCensus setWeighbridgeNo(Long weighbridgeNo) {
    this.weighbridgeNo = weighbridgeNo;
    return this;
  }

  @JsonIgnore
  public WeighbridgeStations getWeighbridgeStationsLink() {
    return weighbridgeStationsLink;
  }
}
