package opdwms.web.weighingtransactions.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import opdwms.web.weighbridgestations.entities.WeighbridgeStations;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "tagging_transactions")
public class TaggingTransactions {

    public static String CLEARED_TAGS = "1";
    public static String PENDING_TAGS = "0";

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "tag_reference", length = 150)
    private String tagReference;

    @Column(name = "vehicle_no", length = 50)
    private String vehicleNo;

    @Column(name = "transgression", length = 250)
    private String transgression;

    @Column(name = "transaction_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Column(name = "weighing_reference", length = 100)
    private String weighingReference;

    @Column(name = "tagging_system", length = 100)
    private String taggingSystem;

    @Column(name = "tagging_scene", length = 100)
    private String taggingScene;

    @Column(name = "tag_status", length = 10)
    private String tagStatus;

    @Column(name = "confirmed_vehicle_no", length = 50)
    private String confirmedVehicle_no;

    @Column(name = "tag_on_charge_amount", precision = 11, scale = 2)
    private BigDecimal tagOnChargeAmount;

    @Column(name = "tag_type", length = 20)
    private String tagType;

    @Column(name = "weighbridge", length = 150)
    private String weighbridge;

    @Column(name = "charged_reason", length = 255)
    private String chargedReason;

    @Column(name = "evidence_reference", length = 20)
    private String evidenceReference;

    @Column(name = "evidence_id", length = 20)
    private String evidenceId;

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

    public TaggingTransactions setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTagReference() {
        return tagReference;
    }

    public TaggingTransactions setTagReference(String tagReference) {
        this.tagReference = tagReference;
        return this;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public TaggingTransactions setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
        return this;
    }

    public String getTransgression() {
        return transgression;
    }

    public TaggingTransactions setTransgression(String transgression) {
        this.transgression = transgression;
        return this;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public TaggingTransactions setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public String getWeighingReference() {
        return weighingReference;
    }

    public TaggingTransactions setWeighingReference(String weighingReference) {
        this.weighingReference = weighingReference;
        return this;
    }

    public String getTaggingSystem() {
        return taggingSystem;
    }

    public TaggingTransactions setTaggingSystem(String taggingSystem) {
        this.taggingSystem = taggingSystem;
        return this;
    }

    public String getTaggingScene() {
        return taggingScene;
    }

    public TaggingTransactions setTaggingScene(String taggingScene) {
        this.taggingScene = taggingScene;
        return this;
    }

    public String getTagStatus() {
        return tagStatus;
    }

    public TaggingTransactions setTagStatus(String tagStatus) {
        this.tagStatus = tagStatus;
        return this;
    }

    public String getConfirmedVehicle_no() {
        return confirmedVehicle_no;
    }

    public TaggingTransactions setConfirmedVehicle_no(String confirmedVehicle_no) {
        this.confirmedVehicle_no = confirmedVehicle_no;
        return this;
    }

    public BigDecimal getTagOnChargeAmount() {
        return tagOnChargeAmount;
    }

    public TaggingTransactions setTagOnChargeAmount(BigDecimal tagOnChargeAmount) {
        this.tagOnChargeAmount = tagOnChargeAmount;
        return this;
    }

    public String getTagType() {
        return tagType;
    }

    public TaggingTransactions setTagType(String tagType) {
        this.tagType = tagType;
        return this;
    }

    public String getWeighbridge() {
        return weighbridge;
    }

    public TaggingTransactions setWeighbridge(String weighbridge) {
        this.weighbridge = weighbridge;
        return this;
    }

    public String getChargedReason() {
        return chargedReason;
    }

    public TaggingTransactions setChargedReason(String chargedReason) {
        this.chargedReason = chargedReason;
        return this;
    }

    public String getEvidenceReference() {
        return evidenceReference;
    }

    public TaggingTransactions setEvidenceReference(String evidenceReference) {
        this.evidenceReference = evidenceReference;
        return this;
    }

    public String getEvidenceId() {
        return evidenceId;
    }

    public TaggingTransactions setEvidenceId(String evidenceId) {
        this.evidenceId = evidenceId;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public TaggingTransactions setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public Long getWeighbridgeNo() {
        return weighbridgeNo;
    }

    public TaggingTransactions setWeighbridgeNo(Long weighbridgeNo) {
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
        if (!(object instanceof TaggingTransactions)) {
            return false;
        }
        TaggingTransactions other = (TaggingTransactions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
