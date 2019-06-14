package opdwms.web.weighingtransactions.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import opdwms.web.policeofficers.entities.PoliceOfficers;
import opdwms.web.usermanager.entities.Users;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "transactions_mobile_weigh_arrest")
public class TransactionMobileWeighArrest {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "transaction_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date transactionDate;

    @Column(name = "vehicle_no", length = 40)
    private String vehicleNo;

    @Column(name = "transporter", length = 150)
    private String transporter;

    @Column(name = "excess_gvw", precision = 11, scale = 2)
    private BigDecimal excessGVW;

    @Column(name = "excess_axle", precision = 11, scale = 2)
    private BigDecimal excessAxleWeight;

    @Column(name = "cargo", length = 100)
    private String cargo;

    @Column(name = "origin", length = 100)
    private String origin;

    @Column(name = "destination", length = 100)
    private String destination;

    @Column(name = "action", length = 250)
    private String description;

    @Column(name = "created_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @JoinColumn(name = "CREATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users createdByLink;

    public Long getId() {
        return id;
    }

    public TransactionMobileWeighArrest setId(Long id) {
        this.id = id;
        return this;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public TransactionMobileWeighArrest setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public TransactionMobileWeighArrest setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
        return this;
    }

    public String getTransporter() {
        return transporter;
    }

    public TransactionMobileWeighArrest setTransporter(String transporter) {
        this.transporter = transporter;
        return this;
    }

    public BigDecimal getExcessGVW() {
        return excessGVW;
    }

    public TransactionMobileWeighArrest setExcessGVW(BigDecimal excessGVW) {
        this.excessGVW = excessGVW;
        return this;
    }

    public BigDecimal getExcessAxleWeight() {
        return excessAxleWeight;
    }

    public TransactionMobileWeighArrest setExcessAxleWeight(BigDecimal excessAxleWeight) {
        this.excessAxleWeight = excessAxleWeight;
        return this;
    }

    public String getCargo() {
        return cargo;
    }

    public TransactionMobileWeighArrest setCargo(String cargo) {
        this.cargo = cargo;
        return this;
    }

    public String getOrigin() {
        return origin;
    }

    public TransactionMobileWeighArrest setOrigin(String origin) {
        this.origin = origin;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public TransactionMobileWeighArrest setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TransactionMobileWeighArrest setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public TransactionMobileWeighArrest setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public TransactionMobileWeighArrest setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    @JsonIgnore
    public Users getCreatedByLink() {
        return createdByLink;
    }

    public TransactionMobileWeighArrest createdOn(Long userId){
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
        TransactionMobileWeighArrest other = (TransactionMobileWeighArrest) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Transaction Mobile Weigh Arrest" +
                "[ id=" + id +
                " transactionDate=" + transactionDate +
                " vehicleNo=" + vehicleNo +
                " policeNo=" + transporter +
                " transporter=" + excessGVW +
                " excessAxleWeight=" + excessAxleWeight +
                " cargo=" + cargo +
                " origin=" + origin +
                " destination=" + destination +
                " action=" + description +
                " ]";
    }
}
