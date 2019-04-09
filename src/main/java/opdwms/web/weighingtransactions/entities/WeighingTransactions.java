package opdwms.web.weighingtransactions.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import opdwms.web.weighbridgestations.entities.WeighbridgeStations;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "weighing_transactions")
public class WeighingTransactions {

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
    
    @Column(name = "first_axle_legal_weight", precision = 11, scale = 2)
    private BigDecimal firstAxleLegalWeight;

    @Column(name = "first_axle_type", length = 20)
    private String firstAxleType;

    @Column(name = "first_axle_grouping", length = 20)
    private String firstAxleGrouping;

    //    Second Axle
    @Column(name = "second_axle_weight", precision = 11, scale = 2)
    private BigDecimal secondAxleWeight;
    
    @Column(name = "second_axle_legal_weight", precision = 11, scale = 2)
    private BigDecimal secondAxleLegalWeight;

    @Column(name = "second_axle_type", length = 20)
    private String secondAxleType;

    @Column(name = "second_axle_grouping", length = 20)
    private String secondAxleGrouping;

    //    Third Axle
    @Column(name = "third_axle_weight", precision = 11, scale = 2)
    private BigDecimal thirdAxleWeight;

    @Column(name = "third_axle_legal_weight", precision = 11, scale = 2)
    private BigDecimal thirdAxleLegalWeight;

    @Column(name = "third_axle_type", length = 20)
    private String thirdAxleType;

    @Column(name = "third_axle_grouping", length = 20)
    private String thirdAxleGrouping;

    //    Fourth Axle
    @Column(name = "fourth_axle_weight", precision = 11, scale = 2)
    private BigDecimal fourthAxleWeight;

    @Column(name = "fourth_axle_legal_weight", precision = 11, scale = 2)
    private BigDecimal fourthAxleLegalWeight;

    @Column(name = "fourth_axle_type", length = 20)
    private String fourthAxleType;

    @Column(name = "fourth_axle_grouping", length = 20)
    private String fourthAxleGrouping;

    //    Fifth Axle
    @Column(name = "fifth_axle_weight", precision = 11, scale = 2)
    private BigDecimal fifthAxleWeight;

    @Column(name = "fifth_axle_legal_weight", precision = 11, scale = 2)
    private BigDecimal fifthAxleLegalWeight;

    @Column(name = "fifth_axle_type", length = 20)
    private String fifthAxleType;

    @Column(name = "fifth_axle_grouping", length = 20)
    private String fifthAxleGrouping;

    //    Sixth Axle
    @Column(name = "sixth_axle_weight", precision = 11, scale = 2)
    private BigDecimal sixthAxleWeight;

    @Column(name = "sixth_axle_legal_weight", precision = 11, scale = 2)
    private BigDecimal sixthAxleLegalWeight;

    @Column(name = "sixth_axle_type", length = 20)
    private String sixthAxleType;

    @Column(name = "sixth_axle_grouping", length = 20)
    private String sixthAxleGrouping;

    //    Sixth Axle
    @Column(name = "seventh_axle_weight", precision = 11, scale = 2)
    private BigDecimal seventhAxleWeight;

    @Column(name = "seventh_axle_legal_weight", precision = 11, scale = 2)
    private BigDecimal seventhAxleLegalWeight;

    @Column(name = "seventh_axle_type", length = 20)
    private String seventhAxleType;

    @Column(name = "seventh_axle_grouping", length = 20)
    private String seventhAxleGrouping;


    @Column(name = "vehicleGVM", precision = 11, scale = 2)
    private BigDecimal vehicleGVM;

    @Column(name = "operator", length = 100)
    private String operator;

    @Column(name = "wbt_status", length = 20)
    private String status;

    @Column(name = "wbt_direction", length = 11)
    private String direction;

    @Column(name = "wbt_bu", length = 11)
    private String wbtbu;

    @Column(name = "wbt_shift", length = 20)
    private String operatorShift;

    @Column(name = "origin", length = 150)
    private String origin;

    @Column(name = "destination", length = 150)
    private String destination;

    @Column(name = "cargo", length = 150)
    private String cargo;

    @Column(name = "action_taken", length = 250)
    private String actionTaken;

    @Column(name = "permit_no", length = 50)
    private String permitNo;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "weighbridge_no")
    private Long weighbridgeNo;

    @JoinColumn(name = "weighbridge_no", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private WeighbridgeStations weighbridgeStationsLink;

    public Long getId() {
        return id;
    }

    public WeighingTransactions setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public WeighingTransactions setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
        return this;
    }

    public String getStationCode() {
        return stationCode;
    }

    public WeighingTransactions setStationCode(String stationCode) {
        this.stationCode = stationCode;
        return this;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public WeighingTransactions setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public WeighingTransactions setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
        return this;
    }

    public String getAxleConfiguration() {
        return axleConfiguration;
    }

    public WeighingTransactions setAxleConfiguration(String axleConfiguration) {
        this.axleConfiguration = axleConfiguration;
        return this;
    }

    public BigDecimal getFirstAxleWeight() {
        return firstAxleWeight;
    }

    public WeighingTransactions setFirstAxleWeight(BigDecimal firstAxleWeight) {
        this.firstAxleWeight = firstAxleWeight;
        return this;
    }

    public BigDecimal getFirstAxleLegalWeight() {
        return firstAxleLegalWeight;
    }

    public WeighingTransactions setFirstAxleLegalWeight(BigDecimal firstAxleLegalWeight) {
        this.firstAxleLegalWeight = firstAxleLegalWeight;
        return this;
    }

    public String getFirstAxleType() {
        return firstAxleType;
    }

    public WeighingTransactions setFirstAxleType(String firstAxleType) {
        this.firstAxleType = firstAxleType;
        return this;
    }

    public String getFirstAxleGrouping() {
        return firstAxleGrouping;
    }

    public WeighingTransactions setFirstAxleGrouping(String firstAxleGrouping) {
        this.firstAxleGrouping = firstAxleGrouping;
        return this;
    }

    public BigDecimal getSecondAxleWeight() {
        return secondAxleWeight;
    }

    public WeighingTransactions setSecondAxleWeight(BigDecimal secondAxleWeight) {
        this.secondAxleWeight = secondAxleWeight;
        return this;
    }

    public BigDecimal getSecondAxleLegalWeight() {
        return secondAxleLegalWeight;
    }

    public WeighingTransactions setSecondAxleLegalWeight(BigDecimal secondAxleLegalWeight) {
        this.secondAxleLegalWeight = secondAxleLegalWeight;
        return this;
    }

    public String getSecondAxleType() {
        return secondAxleType;
    }

    public WeighingTransactions setSecondAxleType(String secondAxleType) {
        this.secondAxleType = secondAxleType;
        return this;
    }

    public String getSecondAxleGrouping() {
        return secondAxleGrouping;
    }

    public WeighingTransactions setSecondAxleGrouping(String secondAxleGrouping) {
        this.secondAxleGrouping = secondAxleGrouping;
        return this;
    }

    public BigDecimal getThirdAxleWeight() {
        return thirdAxleWeight;
    }

    public WeighingTransactions setThirdAxleWeight(BigDecimal thirdAxleWeight) {
        this.thirdAxleWeight = thirdAxleWeight;
        return this;
    }

    public BigDecimal getThirdAxleLegalWeight() {
        return thirdAxleLegalWeight;
    }

    public WeighingTransactions setThirdAxleLegalWeight(BigDecimal thirdAxleLegalWeight) {
        this.thirdAxleLegalWeight = thirdAxleLegalWeight;
        return this;
    }

    public String getThirdAxleType() {
        return thirdAxleType;
    }

    public WeighingTransactions setThirdAxleType(String thirdAxleType) {
        this.thirdAxleType = thirdAxleType;
        return this;
    }

    public String getThirdAxleGrouping() {
        return thirdAxleGrouping;
    }

    public WeighingTransactions setThirdAxleGrouping(String thirdAxleGrouping) {
        this.thirdAxleGrouping = thirdAxleGrouping;
        return this;
    }

    public BigDecimal getFourthAxleWeight() {
        return fourthAxleWeight;
    }

    public WeighingTransactions setFourthAxleWeight(BigDecimal fourthAxleWeight) {
        this.fourthAxleWeight = fourthAxleWeight;
        return this;
    }

    public BigDecimal getFourthAxleLegalWeight() {
        return fourthAxleLegalWeight;
    }

    public WeighingTransactions setFourthAxleLegalWeight(BigDecimal fourthAxleLegalWeight) {
        this.fourthAxleLegalWeight = fourthAxleLegalWeight;
        return this;
    }

    public String getFourthAxleType() {
        return fourthAxleType;
    }

    public WeighingTransactions setFourthAxleType(String fourthAxleType) {
        this.fourthAxleType = fourthAxleType;
        return this;
    }

    public String getFourthAxleGrouping() {
        return fourthAxleGrouping;
    }

    public WeighingTransactions setFourthAxleGrouping(String fourthAxleGrouping) {
        this.fourthAxleGrouping = fourthAxleGrouping;
        return this;
    }

    public BigDecimal getFifthAxleWeight() {
        return fifthAxleWeight;
    }

    public WeighingTransactions setFifthAxleWeight(BigDecimal fifthAxleWeight) {
        this.fifthAxleWeight = fifthAxleWeight;
        return this;
    }

    public BigDecimal getFifthAxleLegalWeight() {
        return fifthAxleLegalWeight;
    }

    public WeighingTransactions setFifthAxleLegalWeight(BigDecimal fifthAxleLegalWeight) {
        this.fifthAxleLegalWeight = fifthAxleLegalWeight;
        return this;
    }

    public String getFifthAxleType() {
        return fifthAxleType;
    }

    public WeighingTransactions setFifthAxleType(String fifthAxleType) {
        this.fifthAxleType = fifthAxleType;
        return this;
    }

    public String getFifthAxleGrouping() {
        return fifthAxleGrouping;
    }

    public WeighingTransactions setFifthAxleGrouping(String fifthAxleGrouping) {
        this.fifthAxleGrouping = fifthAxleGrouping;
        return this;
    }

    public BigDecimal getSixthAxleWeight() {
        return sixthAxleWeight;
    }

    public WeighingTransactions setSixthAxleWeight(BigDecimal sixthAxleWeight) {
        this.sixthAxleWeight = sixthAxleWeight;
        return this;
    }

    public BigDecimal getSixthAxleLegalWeight() {
        return sixthAxleLegalWeight;
    }

    public WeighingTransactions setSixthAxleLegalWeight(BigDecimal sixthAxleLegalWeight) {
        this.sixthAxleLegalWeight = sixthAxleLegalWeight;
        return this;
    }

    public String getSixthAxleType() {
        return sixthAxleType;
    }

    public WeighingTransactions setSixthAxleType(String sixthAxleType) {
        this.sixthAxleType = sixthAxleType;
        return this;
    }

    public String getSixthAxleGrouping() {
        return sixthAxleGrouping;
    }

    public WeighingTransactions setSixthAxleGrouping(String sixthAxleGrouping) {
        this.sixthAxleGrouping = sixthAxleGrouping;
        return this;
    }

    public BigDecimal getSeventhAxleWeight() {
        return seventhAxleWeight;
    }

    public WeighingTransactions setSeventhAxleWeight(BigDecimal seventhAxleWeight) {
        this.seventhAxleWeight = seventhAxleWeight;
        return this;
    }

    public BigDecimal getSeventhAxleLegalWeight() {
        return seventhAxleLegalWeight;
    }

    public WeighingTransactions setSeventhAxleLegalWeight(BigDecimal seventhAxleLegalWeight) {
        this.seventhAxleLegalWeight = seventhAxleLegalWeight;
        return this;
    }

    public String getSeventhAxleType() {
        return seventhAxleType;
    }

    public WeighingTransactions setSeventhAxleType(String seventhAxleType) {
        this.seventhAxleType = seventhAxleType;
        return this;
    }

    public String getSeventhAxleGrouping() {
        return seventhAxleGrouping;
    }

    public WeighingTransactions setSeventhAxleGrouping(String seventhAxleGrouping) {
        this.seventhAxleGrouping = seventhAxleGrouping;
        return this;
    }

    public BigDecimal getVehicleGVM() {
        return vehicleGVM;
    }

    public WeighingTransactions setVehicleGVM(BigDecimal vehicleGVM) {
        this.vehicleGVM = vehicleGVM;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public WeighingTransactions setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public WeighingTransactions setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getDirection() {
        return direction;
    }

    public WeighingTransactions setDirection(String direction) {
        this.direction = direction;
        return this;
    }

    public String getWbtbu() {
        return wbtbu;
    }

    public WeighingTransactions setWbtbu(String wbtbu) {
        this.wbtbu = wbtbu;
        return this;
    }

    public String getOperatorShift() {
        return operatorShift;
    }

    public WeighingTransactions setOperatorShift(String operatorShift) {
        this.operatorShift = operatorShift;
        return this;
    }

    public String getOrigin() {
        return origin;
    }

    public WeighingTransactions setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public WeighingTransactions setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getCargo() {
        return cargo;
    }

    public WeighingTransactions setCargo(String cargo) {
        this.cargo = cargo;
        return this;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public WeighingTransactions setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
        return this;
    }

    public String getPermitNo() {
        return permitNo;
    }

    public WeighingTransactions setPermitNo(String permitNo) {
        this.permitNo = permitNo;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public WeighingTransactions setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public Long getWeighbridgeNo() {
        return weighbridgeNo;
    }

    public WeighingTransactions setWeighbridgeNo(Long weighbridgeNo) {
        this.weighbridgeNo = weighbridgeNo;
        return this;
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
        if (!(object instanceof WeighingTransactions)) {
            return false;
        }
        WeighingTransactions other = (WeighingTransactions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
