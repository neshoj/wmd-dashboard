package opdwms.web.weighingtransactions.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "hswim_transactions")
public class HSWIMTransaction {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "ticket_no", length = 100)
    private String ticketNo;

    @Column(name = "station_name", length = 150)
    private String stationName;

    @Column(name = "vehicle_speed", length = 50)
    private String vehicleSpeed;

    @Column(name = "transaction_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Column(name = "vehicle_length", length = 100)
    private String vehicleLength;

    @Column(name = "axle_count", length = 100)
    private int axleCount;

    @Column(name = "axle_one", precision = 11, scale = 2)
    private BigDecimal axleOne;

    @Column(name = "axle_two", precision = 11, scale = 2)
    private BigDecimal axleTwo;

    @Column(name = "axle_three", precision = 11, scale = 2)
    private BigDecimal axleThree;

    @Column(name = "axle_four", precision = 11, scale = 2)
    private BigDecimal axleFour;

    @Column(name = "axle_five", precision = 11, scale = 2)
    private BigDecimal axleFive;

    @Column(name = "axle_six", precision = 11, scale = 2)
    private BigDecimal axleSix;

    @Column(name = "axle_seven", precision = 11, scale = 2)
    private BigDecimal axleSeven;


    public Long getId() {
        return id;
    }

    public HSWIMTransaction setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public HSWIMTransaction setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
        return this;
    }

    public String getStationName() {
        return stationName;
    }

    public HSWIMTransaction setStationName(String stationName) {
        this.stationName = stationName;
        return this;
    }

    public String getVehicleSpeed() {
        return vehicleSpeed;
    }

    public HSWIMTransaction setVehicleSpeed(String vehicleSpeed) {
        this.vehicleSpeed = vehicleSpeed;
        return this;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public HSWIMTransaction setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public String getVehicleLength() {
        return vehicleLength;
    }

    public HSWIMTransaction setVehicleLength(String vehicleLength) {
        this.vehicleLength = vehicleLength;
        return this;
    }

    public int getAxleCount() {
        return axleCount;
    }

    public HSWIMTransaction setAxleCount(int axleCount) {
        this.axleCount = axleCount;
        return this;
    }

    public BigDecimal getAxleOne() {
        return axleOne;
    }

    public HSWIMTransaction setAxleOne(BigDecimal axleOne) {
        this.axleOne = axleOne;
        return this;
    }

    public BigDecimal getAxleTwo() {
        return axleTwo;
    }

    public HSWIMTransaction setAxleTwo(BigDecimal axleTwo) {
        this.axleTwo = axleTwo;
        return this;
    }

    public BigDecimal getAxleThree() {
        return axleThree;
    }

    public HSWIMTransaction setAxleThree(BigDecimal axleThree) {
        this.axleThree = axleThree;
        return this;
    }

    public BigDecimal getAxleFour() {
        return axleFour;
    }

    public HSWIMTransaction setAxleFour(BigDecimal axleFour) {
        this.axleFour = axleFour;
        return this;
    }

    public BigDecimal getAxleFive() {
        return axleFive;
    }

    public HSWIMTransaction setAxleFive(BigDecimal axleFive) {
        this.axleFive = axleFive;
        return this;
    }

    public BigDecimal getAxleSix() {
        return axleSix;
    }

    public HSWIMTransaction setAxleSix(BigDecimal axleSix) {
        this.axleSix = axleSix;
        return this;
    }

    public BigDecimal getAxleSeven() {
        return axleSeven;
    }

    public HSWIMTransaction setAxleSeven(BigDecimal axleSeven) {
        this.axleSeven = axleSeven;
        return this;
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
        if (!(object instanceof HSWIMTransaction)) {
            return false;
        }
        HSWIMTransaction other = (HSWIMTransaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
