package atlas.web.schools.entities;

import atlas.core.template.forms.EditDataWrapper;
import atlas.core.template.forms.MutableField;
import atlas.web.usermanager.entities.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import atlas.web.configs.entities.ReasonCodes;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table( name = "schools")
public class Schools implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 100)
    @MutableField( name = "Name")
    @Column(name = "name", length = 100)
    private String name;

    @Size(max = 100)
    @MutableField( name = "Location")
    @Column(name = "location", length = 100)
    private String location;

    @Size(max = 20 )
    @MutableField( name = "Contact Phone")
    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Size(max = 100)
    @MutableField( name = "Contact Email")
    @Column(name = "contact_email", length = 100)
    private String contactEmail;

    @Column(name = "balance", precision = 10, scale = 4)
    private BigDecimal balance;

    @MutableField( name = "Cost Per SMS")
    @Column(name = "cost_per_sms", precision = 10, scale = 4)
    private BigDecimal costPerSMS;

    @Size(max = 20)
    @Column(name = "FLAG", length = 20)
    private String flag;

    @Lob
    @EditDataWrapper
    @Size(max = 2147483647)
    @Column(name = "EDIT_DATA", length = 2147483647)
    private String editData;

    @Column( name = "REASON_CODE_NO")
    private Long reasonCodeNo;

    @Size(max = 200)
    @Column(name = "REASON_DESCRIPTION", length = 200)
    private String reasonDescription;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "UPDATED_BY")
    private Long updatedBy;

    @Column(name = "CREATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "UPDATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;

    @Column(name = "DELETED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedOn;

    @JoinColumn(name = "REASON_CODE_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ReasonCodes reasonCodeLink;

    //DISCLAIMER: RELATION NOT ENFORCED IN THE DB
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users createdByLink;

    //DISCLAIMER: RELATION NOT ENFORCED IN THE DB
    @JoinColumn(name = "UPDATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users updatedByLink;

    @JsonIgnore
    public ReasonCodes getReasonCodeLink() {  return reasonCodeLink; }
    @JsonIgnore public Users getCreatedByLink() {  return createdByLink; }
    @JsonIgnore public Users getUpdatedByLink() {  return updatedByLink; }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Schools)) {
            return false;
        }
        Schools other = (Schools) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Schools[ " +
                "   id=" + id +
                "   name=" + name +
                "   location=" + location +
                "   contactPhone=" + contactPhone +
                "   contactEmail=" + contactEmail
                + " ]";
    }

    public Long getId() {
        return id;
    }

    public Schools setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Schools setName(String name) {
        this.name = name;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Schools setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public Schools setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
        return this;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public Schools setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
        return this;
    }

    public String getFlag() {
        return flag;
    }

    public Schools setFlag(String flag) {
        this.flag = flag;
        return this;
    }

    public String getEditData() {
        return editData;
    }

    public Schools setEditData(String editData) {
        this.editData = editData;
        return this;
    }

    public Long getReasonCodeNo() {
        return reasonCodeNo;
    }

    public Schools setReasonCodeNo(Long reasonCodeNo) {
        this.reasonCodeNo = reasonCodeNo;
        return this;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }

    public Schools setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
        return this;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public Schools setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public Schools setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public Schools createdOn(Long userId){
        this.updatedOn = new Date( System.currentTimeMillis() );
        this.createdOn = new Date( System.currentTimeMillis() );
        this.updatedBy = userId;
        this.createdBy = userId;
        return this;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public Schools updatedOn(Long userId){
        this.updatedOn = new Date( System.currentTimeMillis() );
        this.updatedBy = userId;
        return this;
    }

    public Schools setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public Schools setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public Date getDeletedOn() {
        return deletedOn;
    }

    public Schools setDeletedOn(Date deletedOn) {
        this.deletedOn = deletedOn;
        return this;
    }

    public Schools setReasonCodeLink(ReasonCodes reasonCodeLink) {
        this.reasonCodeLink = reasonCodeLink;
        return this;
    }

    public Schools setCreatedByLink(Users createdByLink) {
        this.createdByLink = createdByLink;
        return this;
    }

    public Schools setUpdatedByLink(Users updatedByLink) {
        this.updatedByLink = updatedByLink;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Schools setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public BigDecimal getCostPerSMS() {
        return costPerSMS;
    }

    public Schools setCostPerSMS(BigDecimal costPerSMS) {
        this.costPerSMS = costPerSMS;
        return this;
    }
}
