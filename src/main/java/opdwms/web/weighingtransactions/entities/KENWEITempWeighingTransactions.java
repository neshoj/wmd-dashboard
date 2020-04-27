package opdwms.web.weighingtransactions.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import opdwms.web.weighbridgestations.entities.WeighbridgeStations;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "kenwei_temp_weighing_transactions")
public class KENWEITempWeighingTransactions implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "ticket_no", length = 100)
  private String ticketNo;

  @Column(name = "station_code", length = 100)
  private String stationCode;

  @Column(name = "transaction_date")
  @Temporal(TemporalType.TIMESTAMP)
  private Date transactionDate;

  @Column(name = "vehicle_no", length = 100)
  private String vehicleNo;

  @Column(name = "axle_configuration", length = 100)
  private String axleConfiguration;

  //    First Axle
  @Column(name = "first_axle_weight", precision = 11, scale = 2)
  private BigDecimal firstAxleWeight;

  @Column(name = "first_axle_weight_exceeded_percent", precision = 4, scale = 2)
  private BigDecimal firstAxleWeightExceededPercent;

  @Column(name = "first_axle_weight_exceeded_weight", precision = 11, scale = 2)
  private BigDecimal firstAxleWeightExceededValue;

  @Column(name = "first_axle_legal_weight", precision = 11, scale = 2)
  private BigDecimal firstAxleLegalWeight;

  //    Second Axle
  @Column(name = "second_axle_weight", precision = 11, scale = 2)
  private BigDecimal secondAxleWeight;

  @Column(name = "second_axle_weight_exceeded_percent", precision = 4, scale = 2)
  private BigDecimal secondAxleWeightExceededPercent;

  @Column(name = "second_axle_weight_exceeded_weight", precision = 11, scale = 2)
  private BigDecimal secondAxleWeightExceededValue;

  @Column(name = "second_axle_legal_weight", precision = 11, scale = 2)
  private BigDecimal secondAxleLegalWeight;

  //    Third Axle
  @Column(name = "third_axle_weight", precision = 11, scale = 2)
  private BigDecimal thirdAxleWeight;

  @Column(name = "third_axle_weight_exceeded_percent", precision = 4, scale = 2)
  private BigDecimal thirdAxleWeightExceededPercent;

  @Column(name = "third_axle_weight_exceeded_weight", precision = 11, scale = 2)
  private BigDecimal thirdAxleWeightExceededValue;

  @Column(name = "third_axle_legal_weight", precision = 11, scale = 2)
  private BigDecimal thirdAxleLegalWeight;

  //    Fourth Axle
  @Column(name = "fourth_axle_weight", precision = 11, scale = 2)
  private BigDecimal fourthAxleWeight;

  @Column(name = "fourth_axle_weight_exceeded_percent", precision = 4, scale = 2)
  private BigDecimal fourthAxleWeightExceededPercent;

  @Column(name = "fourth_axle_weight_exceeded_weight", precision = 11, scale = 2)
  private BigDecimal fourthAxleWeightExceededValue;

  @Column(name = "fourth_axle_legal_weight", precision = 11, scale = 2)
  private BigDecimal fourthAxleLegalWeight;

  //    Fifth Axle
  @Column(name = "fifth_axle_weight", precision = 11, scale = 2)
  private BigDecimal fifthAxleWeight;

  @Column(name = "fifth_axle_weight_exceeded_percent", precision = 4, scale = 2)
  private BigDecimal fifthAxleWeightExceededPercent;

  @Column(name = "fifth_axle_weight_exceeded_weight", precision = 11, scale = 2)
  private BigDecimal fifthAxleWeightExceededValue;

  @Column(name = "fifth_axle_legal_weight", precision = 11, scale = 2)
  private BigDecimal fifthAxleLegalWeight;

  //    Sixth Axle
  @Column(name = "sixth_axle_weight", precision = 11, scale = 2)
  private BigDecimal sixthAxleWeight;

  @Column(name = "sixth_axle_weight_exceeded_percent", precision = 4, scale = 2)
  private BigDecimal sixthAxleWeightExceededPercent;

  @Column(name = "sixth_axle_weight_exceeded_weight", precision = 11, scale = 2)
  private BigDecimal sixthAxleWeightExceededValue;

  @Column(name = "sixth_axle_legal_weight", precision = 11, scale = 2)
  private BigDecimal sixthAxleLegalWeight;

  //    Seventh axle
  @Column(name = "seventh_axle_weight", precision = 11, scale = 2)
  private BigDecimal seventhAxleWeight;

  @Column(name = "seventh_axle_weight_exceeded_percent", precision = 4, scale = 2)
  private BigDecimal seventhAxleWeightExceededPercent;

  @Column(name = "seventh_axle_weight_exceeded_weight", precision = 11, scale = 2)
  private BigDecimal seventhAxleWeightExceededValue;

  @Column(name = "seventh_axle_legal_weight", precision = 11, scale = 2)
  private BigDecimal seventhAxleLegalWeight;

  @Column(name = "gvw_exceeded_percent", precision = 4, scale = 2)
  private BigDecimal gvwExceededPercent;

  @Column(name = "gvw_exceeded_weight", precision = 11, scale = 2)
  private BigDecimal gvwExceededWeight;

  @Column(name = "gross_vehicle_mass", precision = 11, scale = 2)
  private BigDecimal vehicleGVM;

  @Column(name = "operator", length = 100)
  private String operator;

  @Column(name = "flag", length = 20)
  private String flag;

  @Column(name = "axle_weight_status", length = 2)
  private String axleWeightStatus;

  @Column(name = "status", length = 2)
  private int status;

  @Column(name = "axle_count", length = 2)
  private int axleCount;

  @Column(name = "origin", length = 150)
  private String origin;

  @Column(name = "destination", length = 150)
  private String destination;

  @Column(name = "cargo", length = 150)
  private String cargo;

  @Column(name = "action_taken", length = 250)
  private String actionTaken;

  @Column(name = "created_on")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdOn;

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

  public KENWEITempWeighingTransactions setId(Long id) {
    this.id = id;
    return this;
  }

  public String getTicketNo() {
    return ticketNo;
  }

  public KENWEITempWeighingTransactions setTicketNo(String ticketNo) {
    this.ticketNo = ticketNo;
    return this;
  }

  public String getStationCode() {
    return stationCode;
  }

  public KENWEITempWeighingTransactions setStationCode(String stationCode) {
    this.stationCode = stationCode;
    return this;
  }

  public Date getTransactionDate() {
    return transactionDate;
  }

  public KENWEITempWeighingTransactions setTransactionDate(Date transactionDate) {
    this.transactionDate = transactionDate;
    return this;
  }

  public String getVehicleNo() {
    return vehicleNo;
  }

  public KENWEITempWeighingTransactions setVehicleNo(String vehicleNo) {
    this.vehicleNo = vehicleNo;
    return this;
  }

  public String getAxleConfiguration() {
    return axleConfiguration;
  }

  public KENWEITempWeighingTransactions setAxleConfiguration(String axleConfiguration) {
    this.axleConfiguration = axleConfiguration;
    return this;
  }

  public BigDecimal getFirstAxleWeight() {
    return firstAxleWeight;
  }

  public KENWEITempWeighingTransactions setFirstAxleWeight(BigDecimal firstAxleWeight) {
    this.firstAxleWeight = firstAxleWeight;
    return this;
  }

  public BigDecimal getFirstAxleLegalWeight() {
    return firstAxleLegalWeight;
  }

  public KENWEITempWeighingTransactions setFirstAxleLegalWeight(BigDecimal firstAxleLegalWeight) {
    this.firstAxleLegalWeight = firstAxleLegalWeight;
    return this;
  }

  public BigDecimal getSecondAxleWeight() {
    return secondAxleWeight;
  }

  public KENWEITempWeighingTransactions setSecondAxleWeight(BigDecimal secondAxleWeight) {
    this.secondAxleWeight = secondAxleWeight;
    return this;
  }

  public BigDecimal getSecondAxleLegalWeight() {
    return secondAxleLegalWeight;
  }

  public KENWEITempWeighingTransactions setSecondAxleLegalWeight(BigDecimal secondAxleLegalWeight) {
    this.secondAxleLegalWeight = secondAxleLegalWeight;
    return this;
  }

  public BigDecimal getThirdAxleWeight() {
    return thirdAxleWeight;
  }

  public KENWEITempWeighingTransactions setThirdAxleWeight(BigDecimal thirdAxleWeight) {
    this.thirdAxleWeight = thirdAxleWeight;
    return this;
  }

  public BigDecimal getThirdAxleLegalWeight() {
    return thirdAxleLegalWeight;
  }

  public KENWEITempWeighingTransactions setThirdAxleLegalWeight(BigDecimal thirdAxleLegalWeight) {
    this.thirdAxleLegalWeight = thirdAxleLegalWeight;
    return this;
  }

  public BigDecimal getFourthAxleWeight() {
    return fourthAxleWeight;
  }

  public KENWEITempWeighingTransactions setFourthAxleWeight(BigDecimal fourthAxleWeight) {
    this.fourthAxleWeight = fourthAxleWeight;
    return this;
  }

  public BigDecimal getFourthAxleLegalWeight() {
    return fourthAxleLegalWeight;
  }

  public KENWEITempWeighingTransactions setFourthAxleLegalWeight(BigDecimal fourthAxleLegalWeight) {
    this.fourthAxleLegalWeight = fourthAxleLegalWeight;
    return this;
  }

  public BigDecimal getFifthAxleWeight() {
    return fifthAxleWeight;
  }

  public KENWEITempWeighingTransactions setFifthAxleWeight(BigDecimal fifthAxleWeight) {
    this.fifthAxleWeight = fifthAxleWeight;
    return this;
  }

  public BigDecimal getFifthAxleLegalWeight() {
    return fifthAxleLegalWeight;
  }

  public KENWEITempWeighingTransactions setFifthAxleLegalWeight(BigDecimal fifthAxleLegalWeight) {
    this.fifthAxleLegalWeight = fifthAxleLegalWeight;
    return this;
  }

  public BigDecimal getSixthAxleWeight() {
    return sixthAxleWeight;
  }

  public KENWEITempWeighingTransactions setSixthAxleWeight(BigDecimal sixthAxleWeight) {
    this.sixthAxleWeight = sixthAxleWeight;
    return this;
  }

  public BigDecimal getSixthAxleLegalWeight() {
    return sixthAxleLegalWeight;
  }

  public KENWEITempWeighingTransactions setSixthAxleLegalWeight(BigDecimal sixthAxleLegalWeight) {
    this.sixthAxleLegalWeight = sixthAxleLegalWeight;
    return this;
  }

  public BigDecimal getSeventhAxleWeight() {
    return seventhAxleWeight;
  }

  public KENWEITempWeighingTransactions setSeventhAxleWeight(BigDecimal seventhAxleWeight) {
    this.seventhAxleWeight = seventhAxleWeight;
    return this;
  }

  public BigDecimal getSeventhAxleLegalWeight() {
    return seventhAxleLegalWeight;
  }

  public KENWEITempWeighingTransactions setSeventhAxleLegalWeight(
      BigDecimal seventhAxleLegalWeight) {
    this.seventhAxleLegalWeight = seventhAxleLegalWeight;
    return this;
  }

  public BigDecimal getVehicleGVM() {
    return vehicleGVM;
  }

  public KENWEITempWeighingTransactions setVehicleGVM(BigDecimal vehicleGVM) {
    this.vehicleGVM = vehicleGVM;
    return this;
  }

  public String getOperator() {
    return operator;
  }

  public KENWEITempWeighingTransactions setOperator(String operator) {
    this.operator = operator;
    return this;
  }

  public String getOrigin() {
    return origin;
  }

  public KENWEITempWeighingTransactions setOrigin(String origin) {
    this.origin = origin;
    return this;
  }

  public String getDestination() {
    return destination;
  }

  public KENWEITempWeighingTransactions setDestination(String destination) {
    this.destination = destination;
    return this;
  }

  public String getCargo() {
    return cargo;
  }

  public KENWEITempWeighingTransactions setCargo(String cargo) {
    this.cargo = cargo;
    return this;
  }

  public String getActionTaken() {
    return actionTaken;
  }

  public KENWEITempWeighingTransactions setActionTaken(String actionTaken) {
    this.actionTaken = actionTaken;
    return this;
  }

  public Date getCreatedOn() {
    return createdOn;
  }

  public KENWEITempWeighingTransactions setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
    return this;
  }

  public Long getWeighbridgeNo() {
    return weighbridgeNo;
  }

  public KENWEITempWeighingTransactions setWeighbridgeNo(Long weighbridgeNo) {
    this.weighbridgeNo = weighbridgeNo;
    return this;
  }

  public BigDecimal getFirstAxleWeightExceededPercent() {
    return firstAxleWeightExceededPercent;
  }

  public KENWEITempWeighingTransactions setFirstAxleWeightExceededPercent(
      BigDecimal firstAxleWeightExceededPercent) {
    this.firstAxleWeightExceededPercent = firstAxleWeightExceededPercent;
    return this;
  }

  public BigDecimal getFirstAxleWeightExceededValue() {
    return firstAxleWeightExceededValue;
  }

  public KENWEITempWeighingTransactions setFirstAxleWeightExceededValue(
      BigDecimal firstAxleWeightExceededValue) {
    this.firstAxleWeightExceededValue = firstAxleWeightExceededValue;
    return this;
  }

  public BigDecimal getSecondAxleWeightExceededPercent() {
    return secondAxleWeightExceededPercent;
  }

  public KENWEITempWeighingTransactions setSecondAxleWeightExceededPercent(
      BigDecimal secondAxleWeightExceededPercent) {
    this.secondAxleWeightExceededPercent = secondAxleWeightExceededPercent;
    return this;
  }

  public BigDecimal getSecondAxleWeightExceededValue() {
    return secondAxleWeightExceededValue;
  }

  public KENWEITempWeighingTransactions setSecondAxleWeightExceededValue(
      BigDecimal secondAxleWeightExceededValue) {
    this.secondAxleWeightExceededValue = secondAxleWeightExceededValue;
    return this;
  }

  public BigDecimal getThirdAxleWeightExceededPercent() {
    return thirdAxleWeightExceededPercent;
  }

  public KENWEITempWeighingTransactions setThirdAxleWeightExceededPercent(
      BigDecimal thirdAxleWeightExceededPercent) {
    this.thirdAxleWeightExceededPercent = thirdAxleWeightExceededPercent;
    return this;
  }

  public BigDecimal getThirdAxleWeightExceededValue() {
    return thirdAxleWeightExceededValue;
  }

  public KENWEITempWeighingTransactions setThirdAxleWeightExceededValue(
      BigDecimal thirdAxleWeightExceededValue) {
    this.thirdAxleWeightExceededValue = thirdAxleWeightExceededValue;
    return this;
  }

  public BigDecimal getFourthAxleWeightExceededPercent() {
    return fourthAxleWeightExceededPercent;
  }

  public KENWEITempWeighingTransactions setFourthAxleWeightExceededPercent(
      BigDecimal fourthAxleWeightExceededPercent) {
    this.fourthAxleWeightExceededPercent = fourthAxleWeightExceededPercent;
    return this;
  }

  public BigDecimal getFourthAxleWeightExceededValue() {
    return fourthAxleWeightExceededValue;
  }

  public KENWEITempWeighingTransactions setFourthAxleWeightExceededValue(
      BigDecimal fourthAxleWeightExceededValue) {
    this.fourthAxleWeightExceededValue = fourthAxleWeightExceededValue;
    return this;
  }

  public BigDecimal getFifthAxleWeightExceededPercent() {
    return fifthAxleWeightExceededPercent;
  }

  public KENWEITempWeighingTransactions setFifthAxleWeightExceededPercent(
      BigDecimal fifthAxleWeightExceededPercent) {
    this.fifthAxleWeightExceededPercent = fifthAxleWeightExceededPercent;
    return this;
  }

  public BigDecimal getFifthAxleWeightExceededValue() {
    return fifthAxleWeightExceededValue;
  }

  public KENWEITempWeighingTransactions setFifthAxleWeightExceededValue(
      BigDecimal fifthAxleWeightExceededValue) {
    this.fifthAxleWeightExceededValue = fifthAxleWeightExceededValue;
    return this;
  }

  public BigDecimal getSixthAxleWeightExceededPercent() {
    return sixthAxleWeightExceededPercent;
  }

  public KENWEITempWeighingTransactions setSixthAxleWeightExceededPercent(
      BigDecimal sixthAxleWeightExceededPercent) {
    this.sixthAxleWeightExceededPercent = sixthAxleWeightExceededPercent;
    return this;
  }

  public BigDecimal getSixthAxleWeightExceededValue() {
    return sixthAxleWeightExceededValue;
  }

  public KENWEITempWeighingTransactions setSixthAxleWeightExceededValue(
      BigDecimal sixthAxleWeightExceededValue) {
    this.sixthAxleWeightExceededValue = sixthAxleWeightExceededValue;
    return this;
  }

  public BigDecimal getSeventhAxleWeightExceededPercent() {
    return seventhAxleWeightExceededPercent;
  }

  public KENWEITempWeighingTransactions setSeventhAxleWeightExceededPercent(
      BigDecimal seventhAxleWeightExceededPercent) {
    this.seventhAxleWeightExceededPercent = seventhAxleWeightExceededPercent;
    return this;
  }

  public BigDecimal getSeventhAxleWeightExceededValue() {
    return seventhAxleWeightExceededValue;
  }

  public KENWEITempWeighingTransactions setSeventhAxleWeightExceededValue(
      BigDecimal seventhAxleWeightExceededValue) {
    this.seventhAxleWeightExceededValue = seventhAxleWeightExceededValue;
    return this;
  }

  public BigDecimal getGvwExceededPercent() {
    return gvwExceededPercent;
  }

  public KENWEITempWeighingTransactions setGvwExceededPercent(BigDecimal gvwExceededPercent) {
    this.gvwExceededPercent = gvwExceededPercent;
    return this;
  }

  public BigDecimal getGvwExceededWeight() {
    return gvwExceededWeight;
  }

  public KENWEITempWeighingTransactions setGvwExceededWeight(BigDecimal gvwExceededWeight) {
    this.gvwExceededWeight = gvwExceededWeight;
    return this;
  }

  public String getAxleWeightStatus() {
    return axleWeightStatus;
  }

  public void setAxleWeightStatus(String axleWeightStatus) {
    this.axleWeightStatus = axleWeightStatus;
  }

  public String getFlag() {
    return flag;
  }

  public void setFlag(String flag) {
    this.flag = flag;
  }

  @JsonIgnore
  public WeighbridgeStations getWeighbridgeStationsLink() {
    return weighbridgeStationsLink;
  }

  @Override
  public int hashCode() {
    int hash = 0;
    hash += (id != null ? id.hashCode() : 0);
    return hash;
  }

  @Override
  public boolean equals(Object object) {
    // TODO: Warning - this method won't work in the case the id fields are not set
    if (!(object instanceof KENWEITempWeighingTransactions)) {
      return false;
    }
    KENWEITempWeighingTransactions other = (KENWEITempWeighingTransactions) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }
}
