package opdwms.web.weighingtransactions.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import opdwms.web.policeofficers.entities.PoliceOfficers;
import opdwms.web.usermanager.entities.Users;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transactions_mobile_weigh_prosecution")
public class TransactionMobileWeighProsecution {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "transporter", length = 150)
    private String transporter;

    @Column(name = "axle_class", length = 40)
    private String axleClass;

    @Column(name = "excess_gvw", precision = 11, scale = 2)
    private BigDecimal excessGVW;

    @Column(name = "excess_axle", precision = 11, scale = 2)
    private BigDecimal excessAxleWeight;

    @Column(name = "vehicle_no", length = 40)
    private String vehicleNo;

    @Column(name = "prohibition_no", length = 100)
    private String prohibitionNo;

    @Column(name = "expected_amount", precision = 10, scale = 2)
    private BigDecimal expectedAmount;

    @Column(name = "actual_amount", precision = 10, scale = 2)
    private BigDecimal actualAmount;

    @Column(name = "receipt_no", length = 100)
    private String receiptNo;

    @Column(name = "payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paymentDate;

    @Column(name = "cargo", length = 100)
    private String cargo;

    @Column(name = "origin", length = 100)
    private String origin;

    @Column(name = "destination", length = 100)
    private String destination;

    @Column(name = "officer")
    private Long officer;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @JoinColumn(name = "CREATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users createdByLink;

    @JoinColumn(name = "officer", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private PoliceOfficers policeOfficerLink;

    public Long getId() {
        return id;
    }

    public TransactionMobileWeighProsecution setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTransporter() {
        return transporter;
    }

    public TransactionMobileWeighProsecution setTransporter(String transporter) {
        this.transporter = transporter;
        return this;
    }

    public String getAxleClass() {
        return axleClass;
    }

    public TransactionMobileWeighProsecution setAxleClass(String axleClass) {
        this.axleClass = axleClass;
        return this;
    }

    public BigDecimal getExcessGVW() {
        return excessGVW;
    }

    public TransactionMobileWeighProsecution setExcessGVW(BigDecimal excessGVW) {
        this.excessGVW = excessGVW;
        return this;
    }

    public BigDecimal getExcessAxleWeight() {
        return excessAxleWeight;
    }

    public TransactionMobileWeighProsecution setExcessAxleWeight(BigDecimal excessAxleWeight) {
        this.excessAxleWeight = excessAxleWeight;
        return this;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public TransactionMobileWeighProsecution setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
        return this;
    }

    public String getProhibitionNo() {
        return prohibitionNo;
    }

    public TransactionMobileWeighProsecution setProhibitionNo(String prohibitionNo) {
        this.prohibitionNo = prohibitionNo;
        return this;
    }

    public BigDecimal getExpectedAmount() {
        return expectedAmount;
    }

    public TransactionMobileWeighProsecution setExpectedAmount(BigDecimal expectedAmount) {
        this.expectedAmount = expectedAmount;
        return this;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public TransactionMobileWeighProsecution setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
        return this;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public TransactionMobileWeighProsecution setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
        return this;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public TransactionMobileWeighProsecution setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public String getCargo() {
        return cargo;
    }

    public TransactionMobileWeighProsecution setCargo(String cargo) {
        this.cargo = cargo;
        return this;
    }

    public String getOrigin() {
        return origin;
    }

    public TransactionMobileWeighProsecution setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public TransactionMobileWeighProsecution setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public Long getOfficer() {
        return officer;
    }

    public TransactionMobileWeighProsecution setOfficer(Long officer) {
        this.officer = officer;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public TransactionMobileWeighProsecution setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public TransactionMobileWeighProsecution setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    @JsonIgnore
    public Users getCreatedByLink() {
        return createdByLink;
    }

    @JsonIgnore
    public PoliceOfficers getPoliceOfficerLink() {
        return policeOfficerLink;
    }

    public TransactionMobileWeighProsecution createdOn(Long userId){
        this.createdOn = new Date( System.currentTimeMillis() );
        this.createdBy = userId;
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
        if (!(object instanceof PoliceOfficers)) {
            return false;
        }
        TransactionMobileWeighProsecution other = (TransactionMobileWeighProsecution) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Transaction Mobile Weigh Arrest" +
                "[ id=" + id +
                " paymentDate=" + paymentDate +
                " vehicleNo=" + vehicleNo +
                " policeNo=" + transporter +
                " transporter=" + excessGVW +
                " excessAxleWeight=" + excessAxleWeight +
                " cargo=" + cargo +
                " origin=" + origin +
                " destination=" + destination +
                " prohibitionNo=" + prohibitionNo +
                " receiptNo=" + receiptNo +
                " officer=" + officer +
                " expectedAmount=" + expectedAmount +
                " actualAmount=" + actualAmount +
                " ]";
    }
}
