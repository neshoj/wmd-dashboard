package opdwms.web.weighingtransactions.entities;

import javax.persistence.*;
import javax.sql.rowset.serial.SerialBlob;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Date;

@Entity
@Table(name = "virtual_stations_transactions")
public class VirtualStationTransactions implements Serializable {

    public static final String UNKNOWN_STATE = "UNKNOWN STATE";
    public static final String WITHIN_ALLOWED_LIMIT = "WITHIN ALLOWED LIMIT";
    public static final String WITHIN_TOLERABLE_OVERLOAD = "WITHIN TOLERABLE OVERLOAD";
    public static final String ABOVE_TOLERABLE_OVERLOAD = "ABOVE TOLERABLE OVERLOAD";

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "virtual_station_transaction_id", nullable = false)
    private Long virtualStationTransactionId;

    @Column(name = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTime;

    @Column(name = "front_plate", length = 45)
    private String frontPlate;

    @Column(name = "back_plate", length = 45)
    private String backPlate;

    @Column(name = "virtual_station_id", length = 45)
    private String virtualStationId;

    @Column(name = "virtual_station", length = 150)
    private String virtualStation;

    @Column(name = "axles_count", length = 2)
    private int axleCount;

    @Column(name = "total_weight", precision = 11, scale = 2)
    private BigDecimal totalWeight;

    @Column(name = "weight_limit", precision = 11, scale = 2)
    private BigDecimal weightLimit;

    @Column(name = "percentage_overload", precision = 11, scale = 2)
    private BigDecimal percentageOverload;

    @Column(name = "axle_configuration", length = 35)
    private String axleConfiguration;

    @Column(name = "axle_description", length = 35)
    private String axleDescription;

    @Column(name = "velocity", length = 50)
    private String velocity;

    @Column(name = "length", length = 10)
    private String length;

    @Column(name = "wim_detector_id", length = 10)
    private String wimDetectorId;

    //    First Axle
    @Column(name = "first_axle_load", precision = 11, scale = 2)
    private BigDecimal firstAxleLoad;

    //    Second Axle
    @Column(name = "second_axle_load", precision = 11, scale = 2)
    private BigDecimal secondAxleLoad;

    //    Third Axle
    @Column(name = "third_axle_load", precision = 11, scale = 2)
    private BigDecimal thirdAxleLoad;

    //    Fourth Axle
    @Column(name = "fourth_axle_load", precision = 11, scale = 2)
    private BigDecimal fourthAxleLoad;

    //    Fifth Axle
    @Column(name = "fifth_axle_load", precision = 11, scale = 2)
    private BigDecimal fifthAxleLoad;

    //    Sixth Axle
    @Column(name = "sixth_axle_load", precision = 11, scale = 2)
    private BigDecimal sixthAxleLoad;

    //    Seventh axle
    @Column(name = "seventh_axle_load", precision = 11, scale = 2)
    private BigDecimal seventhAxleLoad;

    //    eight axle
    @Column(name = "eighth_axle_load", precision = 11, scale = 2)
    private BigDecimal eighthAxleLoad;

    //    ninth axle
    @Column(name = "ninth_axle_load", precision = 11, scale = 2)
    private BigDecimal ninthAxleLoad;

    @Column(name = "lp_image_front")
    @Lob
    private Blob frontPlateBinaryImage;

    @Column(name = "back_plate_binary_image")
    @Lob
    private Blob backPlateBinaryImage;

    @Column(name = "detail_image")
    @Lob
    private Blob detailImage;

    @Column(name = "detail_image_back")
    @Lob
    private Blob detailImageBack;

//    @Column(name = "overviewImage")
//    @Lob
//    private Blob overviewImage;

    @Column(name = "flag", length = 30)
    private String flag;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    public Long getId() {
        return id;
    }

    public VirtualStationTransactions setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getVirtualStationTransactionId() {
        return virtualStationTransactionId;
    }

    public VirtualStationTransactions setVirtualStationTransactionId(Long virtualStationTransactionId) {
        this.virtualStationTransactionId = virtualStationTransactionId;
        return this;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public VirtualStationTransactions setDateTime(Date dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public String getFrontPlate() {
        return frontPlate;
    }

    public VirtualStationTransactions setFrontPlate(String frontPlate) {
        this.frontPlate = frontPlate;
        return this;
    }

    public String getBackPlate() {
        return backPlate;
    }

    public VirtualStationTransactions setBackPlate(String backPlate) {
        this.backPlate = backPlate;
        return this;
    }

    public String getVirtualStationId() {
        return virtualStationId;
    }

    public VirtualStationTransactions setVirtualStationId(String virtualStationId) {
        this.virtualStationId = virtualStationId;
        return this;
    }

    public String getVirtualStation() {
        return virtualStation;
    }

    public VirtualStationTransactions setVirtualStation(String virtualStation) {
        this.virtualStation = virtualStation;
        return this;
    }

    public int getAxleCount() {
        return axleCount;
    }

    public VirtualStationTransactions setAxleCount(int axleCount) {
        this.axleCount = axleCount;
        return this;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public VirtualStationTransactions setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
        return this;
    }

    public BigDecimal getWeightLimit() {
        return weightLimit;
    }

    public VirtualStationTransactions setWeightLimit(BigDecimal weightLimit) {
        this.weightLimit = weightLimit;
        return this;
    }

    public BigDecimal getPercentageOverload() {
        return percentageOverload;
    }

    public VirtualStationTransactions setPercentageOverload(BigDecimal percentageOverload) {
        this.percentageOverload = percentageOverload;
        return this;
    }

    public String getAxleConfiguration() {
        return axleConfiguration;
    }

    public VirtualStationTransactions setAxleConfiguration(String axleConfiguration) {
        this.axleConfiguration = axleConfiguration;
        return this;
    }

    public String getAxleDescription() {
        return axleDescription;
    }

    public VirtualStationTransactions setAxleDescription(String axleDescription) {
        this.axleDescription = axleDescription;
        return this;
    }

    public String getVelocity() {
        return velocity;
    }

    public VirtualStationTransactions setVelocity(String velocity) {
        this.velocity = velocity;
        return this;
    }

    public String getLength() {
        return length;
    }

    public VirtualStationTransactions setLength(String length) {
        this.length = length;
        return this;
    }

    public String getWimDetectorId() {
        return wimDetectorId;
    }

    public VirtualStationTransactions setWimDetectorId(String wimDetectorId) {
        this.wimDetectorId = wimDetectorId;
        return this;
    }

    public BigDecimal getFirstAxleLoad() {
        return firstAxleLoad;
    }

    public VirtualStationTransactions setFirstAxleLoad(BigDecimal firstAxleLoad) {
        this.firstAxleLoad = firstAxleLoad;
        return this;
    }

    public BigDecimal getSecondAxleLoad() {
        return secondAxleLoad;
    }

    public VirtualStationTransactions setSecondAxleLoad(BigDecimal secondAxleLoad) {
        this.secondAxleLoad = secondAxleLoad;
        return this;
    }

    public BigDecimal getThirdAxleLoad() {
        return thirdAxleLoad;
    }

    public VirtualStationTransactions setThirdAxleLoad(BigDecimal thirdAxleLoad) {
        this.thirdAxleLoad = thirdAxleLoad;
        return this;
    }

    public BigDecimal getFourthAxleLoad() {
        return fourthAxleLoad;
    }

    public VirtualStationTransactions setFourthAxleLoad(BigDecimal fourthAxleLoad) {
        this.fourthAxleLoad = fourthAxleLoad;
        return this;
    }

    public BigDecimal getFifthAxleLoad() {
        return fifthAxleLoad;
    }

    public VirtualStationTransactions setFifthAxleLoad(BigDecimal fifthAxleLoad) {
        this.fifthAxleLoad = fifthAxleLoad;
        return this;
    }

    public BigDecimal getSixthAxleLoad() {
        return sixthAxleLoad;
    }

    public VirtualStationTransactions setSixthAxleLoad(BigDecimal sixthAxleLoad) {
        this.sixthAxleLoad = sixthAxleLoad;
        return this;
    }

    public BigDecimal getSeventhAxleLoad() {
        return seventhAxleLoad;
    }

    public VirtualStationTransactions setSeventhAxleLoad(BigDecimal seventhAxleLoad) {
        this.seventhAxleLoad = seventhAxleLoad;
        return this;
    }

    public BigDecimal getEighthAxleLoad() {
        return eighthAxleLoad;
    }

    public VirtualStationTransactions setEighthAxleLoad(BigDecimal eighthAxleLoad) {
        this.eighthAxleLoad = eighthAxleLoad;
        return this;
    }

    public BigDecimal getNinthAxleLoad() {
        return ninthAxleLoad;
    }

    public VirtualStationTransactions setNinthAxleLoad(BigDecimal ninthAxleLoad) {
        this.ninthAxleLoad = ninthAxleLoad;
        return this;
    }

    public Blob getFrontPlateBinaryImage() {
        return frontPlateBinaryImage;
    }

    public VirtualStationTransactions setFrontPlateBinaryImage(byte[] frontPlateBinaryImage) {
        try {
            this.frontPlateBinaryImage = new SerialBlob(frontPlateBinaryImage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Blob getBackplateBinaryImage() {
        return backPlateBinaryImage;
    }

    public VirtualStationTransactions setBackplateBinaryImage(byte[] backplateBinaryImage) {
        try {
            this.backPlateBinaryImage = new SerialBlob(backplateBinaryImage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }


    public Blob getDetailImage() {
        return detailImage;
    }

    public VirtualStationTransactions setDetailImage(byte[] detailImage) {
        try {
            this.detailImage = new SerialBlob(detailImage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public Blob getDetailImageBack() {
        return detailImageBack;
    }

    public VirtualStationTransactions setDetailImageBack(byte[] detailImageBack) {
        try {
            this.detailImageBack = new SerialBlob(detailImageBack);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

//    public Blob getOverviewImage() {
//        return overviewImage;
//    }
//
//    public VirtualStationTransactions setOverviewImage(byte[] overviewImage) {
//        try {
//            this.overviewImage = new SerialBlob(overviewImage);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return this;
//    }

    public String getFlag() {
        return flag;
    }

    public VirtualStationTransactions setFlag(String flag) {
        this.flag = flag;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public VirtualStationTransactions setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
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
        if (!(object instanceof VirtualStationTransactions)) {
            return false;
        }
        VirtualStationTransactions other = (VirtualStationTransactions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
