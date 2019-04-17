package opdwms.api.models;

import java.math.BigDecimal;
import java.util.Date;

public class HSWIMTransactionRequest {

    private String ticketNo;
    private String stationName;
    private String vehicleSpeed;
    private Date transactionDate;
    private String vehicleLength;
    private int axleCount;
    private BigDecimal axleOne;
    private BigDecimal axleTwo;
    private BigDecimal axleThree;
    private BigDecimal axleFour;
    private BigDecimal axleFive;
    private BigDecimal axleSix;
    private BigDecimal axleSeven;

    public String getTicketNo() {
        return ticketNo;
    }

    public HSWIMTransactionRequest setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
        return this;
    }

    public String getStationName() {
        return stationName;
    }

    public HSWIMTransactionRequest setStationName(String stationName) {
        this.stationName = stationName;
        return this;
    }

    public String getVehicleSpeed() {
        return vehicleSpeed;
    }

    public HSWIMTransactionRequest setVehicleSpeed(String vehicleSpeed) {
        this.vehicleSpeed = vehicleSpeed;
        return this;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public HSWIMTransactionRequest setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public String getVehicleLength() {
        return vehicleLength;
    }

    public HSWIMTransactionRequest setVehicleLength(String vehicleLength) {
        this.vehicleLength = vehicleLength;
        return this;
    }

    public int getAxleCount() {
        return axleCount;
    }

    public HSWIMTransactionRequest setAxleCount(int axleCount) {
        this.axleCount = axleCount;
        return this;
    }

    public BigDecimal getAxleOne() {
        return axleOne;
    }

    public HSWIMTransactionRequest setAxleOne(BigDecimal axleOne) {
        this.axleOne = axleOne;
        return this;
    }

    public BigDecimal getAxleTwo() {
        return axleTwo;
    }

    public HSWIMTransactionRequest setAxleTwo(BigDecimal axleTwo) {
        this.axleTwo = axleTwo;
        return this;
    }

    public BigDecimal getAxleThree() {
        return axleThree;
    }

    public HSWIMTransactionRequest setAxleThree(BigDecimal axleThree) {
        this.axleThree = axleThree;
        return this;
    }

    public BigDecimal getAxleFour() {
        return axleFour;
    }

    public HSWIMTransactionRequest setAxleFour(BigDecimal axleFour) {
        this.axleFour = axleFour;
        return this;
    }

    public BigDecimal getAxleFive() {
        return axleFive;
    }

    public HSWIMTransactionRequest setAxleFive(BigDecimal axleFive) {
        this.axleFive = axleFive;
        return this;
    }

    public BigDecimal getAxleSix() {
        return axleSix;
    }

    public HSWIMTransactionRequest setAxleSix(BigDecimal axleSix) {
        this.axleSix = axleSix;
        return this;
    }

    public BigDecimal getAxleSeven() {
        return axleSeven;
    }

    public HSWIMTransactionRequest setAxleSeven(BigDecimal axleSeven) {
        this.axleSeven = axleSeven;
        return this;
    }
}
