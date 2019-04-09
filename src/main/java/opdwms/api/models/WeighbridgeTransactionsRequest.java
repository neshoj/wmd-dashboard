package opdwms.api.models;

import java.math.BigInteger;
import java.util.Date;

public class WeighbridgeTransactionsRequest {
    private String ticketNo;
    private String stationCode;
    private Date transactionDate;
    private String vehicleNo;
    private String axleConfiguration;

    private Double firstAxleWeight;
    private Double firstAxleLegalWeight;
    private String firstAxleType;
    private String firstAxleGrouping;

    private Double secondAxleWeight;
    private Double secondAxleLegalWeight;
    private String secondAxleType;
    private String secondAxleGrouping;

    private Double thirdAxleWeight;
    private Double thirdAxleLegalWeight;
    private String thirdAxleType;
    private String thirdAxleGrouping;

    private Double fourthAxleWeight;
    private Double fourthAxleLegalWeight;
    private String fourthAxleType;
    private String fourthAxleGrouping;

    private Double fifthAxleWeight;
    private Double fifthAxleLegalWeight;
    private String fifthAxleType;
    private String fifthAxleGrouping;

    private Double sixthAxleWeight;
    private Double sixthAxleLegalWeight;
    private String sixthAxleType;
    private String sixthAxleGrouping;

    private Double seventhAxleWeight;
    private Double seventhAxleLegalWeight;
    private String seventhAxleType;
    private String seventhAxleGrouping;

    private Double WBT_1_Gross;
    private String WBT_OPERATOR;
    private String WBT_Status;
    private String WBT_direction;
    private String WBT_BU;
    private String WBT_Shift;

    private String Origin;
    private String Destination;
    private String Cargo;
    private String ActionTaken;
    private BigInteger permitNo;

    public String getTicketNo() {
        return ticketNo;
    }

    public WeighbridgeTransactionsRequest setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
        return this;
    }

    public String getStationCode() {
        return stationCode;
    }

    public WeighbridgeTransactionsRequest setStationCode(String stationCode) {
        this.stationCode = stationCode;
        return this;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public WeighbridgeTransactionsRequest setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public WeighbridgeTransactionsRequest setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
        return this;
    }

    public String getAxleConfiguration() {
        return axleConfiguration;
    }

    public WeighbridgeTransactionsRequest setAxleConfiguration(String axleConfiguration) {
        this.axleConfiguration = axleConfiguration;
        return this;
    }

    public Double getFirstAxleWeight() {
        return firstAxleWeight;
    }

    public WeighbridgeTransactionsRequest setFirstAxleWeight(Double firstAxleWeight) {
        this.firstAxleWeight = firstAxleWeight;
        return this;
    }

    public Double getFirstAxleLegalWeight() {
        return firstAxleLegalWeight;
    }

    public WeighbridgeTransactionsRequest setFirstAxleLegalWeight(Double firstAxleLegalWeight) {
        this.firstAxleLegalWeight = firstAxleLegalWeight;
        return this;
    }

    public String getFirstAxleType() {
        return firstAxleType;
    }

    public WeighbridgeTransactionsRequest setFirstAxleType(String firstAxleType) {
        this.firstAxleType = firstAxleType;
        return this;
    }

    public String getFirstAxleGrouping() {
        return firstAxleGrouping;
    }

    public WeighbridgeTransactionsRequest setFirstAxleGrouping(String firstAxleGrouping) {
        this.firstAxleGrouping = firstAxleGrouping;
        return this;
    }

    public Double getSecondAxleWeight() {
        return secondAxleWeight;
    }

    public WeighbridgeTransactionsRequest setSecondAxleWeight(Double secondAxleWeight) {
        this.secondAxleWeight = secondAxleWeight;
        return this;
    }

    public Double getSecondAxleLegalWeight() {
        return secondAxleLegalWeight;
    }

    public WeighbridgeTransactionsRequest setSecondAxleLegalWeight(Double secondAxleLegalWeight) {
        this.secondAxleLegalWeight = secondAxleLegalWeight;
        return this;
    }

    public String getSecondAxleType() {
        return secondAxleType;
    }

    public WeighbridgeTransactionsRequest setSecondAxleType(String secondAxleType) {
        this.secondAxleType = secondAxleType;
        return this;
    }

    public String getSecondAxleGrouping() {
        return secondAxleGrouping;
    }

    public WeighbridgeTransactionsRequest setSecondAxleGrouping(String secondAxleGrouping) {
        this.secondAxleGrouping = secondAxleGrouping;
        return this;
    }

    public Double getThirdAxleWeight() {
        return thirdAxleWeight;
    }

    public WeighbridgeTransactionsRequest setThirdAxleWeight(Double thirdAxleWeight) {
        this.thirdAxleWeight = thirdAxleWeight;
        return this;
    }

    public Double getThirdAxleLegalWeight() {
        return thirdAxleLegalWeight;
    }

    public WeighbridgeTransactionsRequest setThirdAxleLegalWeight(Double thirdAxleLegalWeight) {
        this.thirdAxleLegalWeight = thirdAxleLegalWeight;
        return this;
    }

    public String getThirdAxleType() {
        return thirdAxleType;
    }

    public WeighbridgeTransactionsRequest setThirdAxleType(String thirdAxleType) {
        this.thirdAxleType = thirdAxleType;
        return this;
    }

    public String getThirdAxleGrouping() {
        return thirdAxleGrouping;
    }

    public WeighbridgeTransactionsRequest setThirdAxleGrouping(String thirdAxleGrouping) {
        this.thirdAxleGrouping = thirdAxleGrouping;
        return this;
    }

    public Double getFourthAxleWeight() {
        return fourthAxleWeight;
    }

    public WeighbridgeTransactionsRequest setFourthAxleWeight(Double fourthAxleWeight) {
        this.fourthAxleWeight = fourthAxleWeight;
        return this;
    }

    public Double getFourthAxleLegalWeight() {
        return fourthAxleLegalWeight;
    }

    public WeighbridgeTransactionsRequest setFourthAxleLegalWeight(Double fourthAxleLegalWeight) {
        this.fourthAxleLegalWeight = fourthAxleLegalWeight;
        return this;
    }

    public String getFourthAxleType() {
        return fourthAxleType;
    }

    public WeighbridgeTransactionsRequest setFourthAxleType(String fourthAxleType) {
        this.fourthAxleType = fourthAxleType;
        return this;
    }

    public String getFourthAxleGrouping() {
        return fourthAxleGrouping;
    }

    public WeighbridgeTransactionsRequest setFourthAxleGrouping(String fourthAxleGrouping) {
        this.fourthAxleGrouping = fourthAxleGrouping;
        return this;
    }

    public Double getFifthAxleWeight() {
        return fifthAxleWeight;
    }

    public WeighbridgeTransactionsRequest setFifthAxleWeight(Double fifthAxleWeight) {
        this.fifthAxleWeight = fifthAxleWeight;
        return this;
    }

    public Double getFifthAxleLegalWeight() {
        return fifthAxleLegalWeight;
    }

    public WeighbridgeTransactionsRequest setFifthAxleLegalWeight(Double fifthAxleLegalWeight) {
        this.fifthAxleLegalWeight = fifthAxleLegalWeight;
        return this;
    }

    public String getFifthAxleType() {
        return fifthAxleType;
    }

    public WeighbridgeTransactionsRequest setFifthAxleType(String fifthAxleType) {
        this.fifthAxleType = fifthAxleType;
        return this;
    }

    public String getFifthAxleGrouping() {
        return fifthAxleGrouping;
    }

    public WeighbridgeTransactionsRequest setFifthAxleGrouping(String fifthAxleGrouping) {
        this.fifthAxleGrouping = fifthAxleGrouping;
        return this;
    }

    public Double getSixthAxleWeight() {
        return sixthAxleWeight;
    }

    public WeighbridgeTransactionsRequest setSixthAxleWeight(Double sixthAxleWeight) {
        this.sixthAxleWeight = sixthAxleWeight;
        return this;
    }

    public Double getSixthAxleLegalWeight() {
        return sixthAxleLegalWeight;
    }

    public WeighbridgeTransactionsRequest setSixthAxleLegalWeight(Double sixthAxleLegalWeight) {
        this.sixthAxleLegalWeight = sixthAxleLegalWeight;
        return this;
    }

    public String getSixthAxleType() {
        return sixthAxleType;
    }

    public WeighbridgeTransactionsRequest setSixthAxleType(String sixthAxleType) {
        this.sixthAxleType = sixthAxleType;
        return this;
    }

    public String getSixthAxleGrouping() {
        return sixthAxleGrouping;
    }

    public WeighbridgeTransactionsRequest setSixthAxleGrouping(String sixthAxleGrouping) {
        this.sixthAxleGrouping = sixthAxleGrouping;
        return this;
    }

    public Double getSeventhAxleWeight() {
        return seventhAxleWeight;
    }

    public WeighbridgeTransactionsRequest setSeventhAxleWeight(Double seventhAxleWeight) {
        this.seventhAxleWeight = seventhAxleWeight;
        return this;
    }

    public Double getSeventhAxleLegalWeight() {
        return seventhAxleLegalWeight;
    }

    public WeighbridgeTransactionsRequest setSeventhAxleLegalWeight(Double seventhAxleLegalWeight) {
        this.seventhAxleLegalWeight = seventhAxleLegalWeight;
        return this;
    }

    public String getSeventhAxleType() {
        return seventhAxleType;
    }

    public WeighbridgeTransactionsRequest setSeventhAxleType(String seventhAxleType) {
        this.seventhAxleType = seventhAxleType;
        return this;
    }

    public String getSeventhAxleGrouping() {
        return seventhAxleGrouping;
    }

    public WeighbridgeTransactionsRequest setSeventhAxleGrouping(String seventhAxleGrouping) {
        this.seventhAxleGrouping = seventhAxleGrouping;
        return this;
    }

    public Double getWBT_1_Gross() {
        return WBT_1_Gross;
    }

    public WeighbridgeTransactionsRequest setWBT_1_Gross(Double WBT_1_Gross) {
        this.WBT_1_Gross = WBT_1_Gross;
        return this;
    }

    public String getWBT_OPERATOR() {
        return WBT_OPERATOR;
    }

    public WeighbridgeTransactionsRequest setWBT_OPERATOR(String WBT_OPERATOR) {
        this.WBT_OPERATOR = WBT_OPERATOR;
        return this;
    }

    public String getWBT_Status() {
        return WBT_Status;
    }

    public WeighbridgeTransactionsRequest setWBT_Status(String WBT_Status) {
        this.WBT_Status = WBT_Status;
        return this;
    }

    public String getWBT_direction() {
        return WBT_direction;
    }

    public WeighbridgeTransactionsRequest setWBT_direction(String WBT_direction) {
        this.WBT_direction = WBT_direction;
        return this;
    }

    public String getWBT_BU() {
        return WBT_BU;
    }

    public WeighbridgeTransactionsRequest setWBT_BU(String WBT_BU) {
        this.WBT_BU = WBT_BU;
        return this;
    }

    public String getWBT_Shift() {
        return WBT_Shift;
    }

    public WeighbridgeTransactionsRequest setWBT_Shift(String WBT_Shift) {
        this.WBT_Shift = WBT_Shift;
        return this;
    }

    public String getOrigin() {
        return Origin;
    }

    public WeighbridgeTransactionsRequest setOrigin(String origin) {
        Origin = origin;
        return this;
    }

    public String getDestination() {
        return Destination;
    }

    public WeighbridgeTransactionsRequest setDestination(String destination) {
        Destination = destination;
        return this;
    }

    public String getCargo() {
        return Cargo;
    }

    public WeighbridgeTransactionsRequest setCargo(String cargo) {
        Cargo = cargo;
        return this;
    }

    public String getActionTaken() {
        return ActionTaken;
    }

    public WeighbridgeTransactionsRequest setActionTaken(String actionTaken) {
        ActionTaken = actionTaken;
        return this;
    }

    public BigInteger getPermitNo() {
        return permitNo;
    }

    public WeighbridgeTransactionsRequest setPermitNo(BigInteger permitNo) {
        this.permitNo = permitNo;
        return this;
    }

    @Override
    public String toString() {
        return String.format("WeighbridgeTransactionsRequest " +
                        "[ticketNo=%s, stationCode=%s, transactionDate=%s, vehicleNo=%s, axleConfiguration=%s, WBT_1_Gross=%s,  WBT_OPERATOR=%s, WBT_Status=%s, WBT_direction=%s, WBT_BU=%s, WBT_Shift=%s,  Origin=%s, Destination=%s, Cargo=%s, ActionTaken=%s, PermitNo=%s]",
                getTicketNo(), getStationCode(), getTransactionDate(), getVehicleNo(), getAxleConfiguration(), getWBT_1_Gross(), getWBT_OPERATOR(), getWBT_Status(), getWBT_direction(), getWBT_BU(), getWBT_Shift(), getOrigin(), getDestination(), getCargo(), getActionTaken(), getPermitNo());
    }
}
