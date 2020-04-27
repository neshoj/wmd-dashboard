package opdwms.web.weighingtransactions.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "kenwei_temp_axle_transactions")
public class KENWEITempAxleTransactions implements Serializable {

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

  @Column(name = "actual_mass", precision = 11, scale = 2)
  private BigDecimal actualMass;

  @Column(name = "allowed_mass", precision = 4, scale = 2)
  private BigDecimal allowedMass;

  @Column(name = "acceptable_tolerance", precision = 11, scale = 2)
  private BigDecimal acceptableTolerance;

  @Column(name = "overload", precision = 11, scale = 2)
  private BigDecimal overload;

  @Column(name = "axle_configuration_no", length = 10)
  private String axleConfigurationNo;

  @Column(name = "axle_configuration", length = 15)
  private String axleConfiguration;

  @Column(name = "axle_unit")
  private int axleUnit;

  @Column(name = "axle_unit_position")
  private int axleUnitPosition;

  @Column(name = "status")
  private String status;

  @Column(name = "temp_transaction_no")
  private Long tempTransactionNo;

  @JoinColumn(
      name = "temp_transaction_no",
      referencedColumnName = "ID",
      insertable = false,
      updatable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private KENWEITempWeighingTransactions kenweiTempWeighingTransactionsLink;

  public Long getId() {
    return id;
  }

  public KENWEITempAxleTransactions setId(Long id) {
    this.id = id;
    return this;
  }

  public String getTicketNo() {
    return ticketNo;
  }

  public KENWEITempAxleTransactions setTicketNo(String ticketNo) {
    this.ticketNo = ticketNo;
    return this;
  }

  public String getStationCode() {
    return stationCode;
  }

  public KENWEITempAxleTransactions setStationCode(String stationCode) {
    this.stationCode = stationCode;
    return this;
  }

  public Date getTransactionDate() {
    return transactionDate;
  }

  public KENWEITempAxleTransactions setTransactionDate(Date transactionDate) {
    this.transactionDate = transactionDate;
    return this;
  }

  public String getVehicleNo() {
    return vehicleNo;
  }

  public KENWEITempAxleTransactions setVehicleNo(String vehicleNo) {
    this.vehicleNo = vehicleNo;
    return this;
  }

  public BigDecimal getActualMass() {
    return actualMass;
  }

  public KENWEITempAxleTransactions setActualMass(BigDecimal actualMass) {
    this.actualMass = actualMass;
    return this;
  }

  public BigDecimal getAllowedMass() {
    return allowedMass;
  }

  public KENWEITempAxleTransactions setAllowedMass(BigDecimal allowedMass) {
    this.allowedMass = allowedMass;
    return this;
  }

  public BigDecimal getAcceptableTolerance() {
    return acceptableTolerance;
  }

  public KENWEITempAxleTransactions setAcceptableTolerance(BigDecimal acceptableTolerance) {
    this.acceptableTolerance = acceptableTolerance;
    return this;
  }

  public BigDecimal getOverload() {
    return overload;
  }

  public KENWEITempAxleTransactions setOverload(BigDecimal overload) {
    this.overload = overload;
    return this;
  }

  public String getAxleConfigurationNo() {
    return axleConfigurationNo;
  }

  public KENWEITempAxleTransactions setAxleConfigurationNo(String axleConfigurationNo) {
    this.axleConfigurationNo = axleConfigurationNo;
    return this;
  }

  public String getAxleConfiguration() {
    return axleConfiguration;
  }

  public KENWEITempAxleTransactions setAxleConfiguration(String axleConfiguration) {
    this.axleConfiguration = axleConfiguration;
    return this;
  }

  public int getAxleUnit() {
    return axleUnit;
  }

  public KENWEITempAxleTransactions setAxleUnit(int axleUnit) {
    this.axleUnit = axleUnit;
    return this;
  }

  public int getAxleUnitPosition() {
    return axleUnitPosition;
  }

  public KENWEITempAxleTransactions setAxleUnitPosition(int axleUnitPosition) {
    this.axleUnitPosition = axleUnitPosition;
    return this;
  }

  public String getStatus() {
    return status;
  }

  public KENWEITempAxleTransactions setStatus(String status) {
    this.status = status;
    return this;
  }

  public Long getTempTransactionNo() {
    return tempTransactionNo;
  }

  public KENWEITempAxleTransactions setTempTransactionNo(Long tempTransactionNo) {
    this.tempTransactionNo = tempTransactionNo;
    return this;
  }

  @JsonIgnore
  public KENWEITempWeighingTransactions getKenweiTempWeighingTransactionsLink() {
    return kenweiTempWeighingTransactionsLink;
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
    if (!(object instanceof KENWEITempAxleTransactions)) {
      return false;
    }
    KENWEITempAxleTransactions other = (KENWEITempAxleTransactions) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }
}
