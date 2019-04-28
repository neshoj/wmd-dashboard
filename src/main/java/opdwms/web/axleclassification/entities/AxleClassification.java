package opdwms.web.axleclassification.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import opdwms.core.template.forms.EditDataWrapper;
import opdwms.core.template.forms.MutableField;
import opdwms.web.configs.entities.ReasonCodes;
import opdwms.web.usermanager.entities.Users;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "axle_classifications")
public class AxleClassification  implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 10)
    @MutableField( name = "Axle Code")
    @Column(name = "axle_code", length = 10)
    private String axleCode;

    @MutableField( name = "Axle One")
    @Column(name = "axle_one", precision = 11, scale = 2)
    private BigDecimal axleOne;

    @MutableField( name = "Axle Two")
    @Column(name = "axle_two", precision = 11, scale = 2)
    private BigDecimal axleTwo;

    @MutableField( name = "Axle Three")
    @Column(name = "axle_three", precision = 11, scale = 2)
    private BigDecimal axleThree;

    @MutableField( name = "Axle Four")
    @Column(name = "axle_four", precision = 11, scale = 2)
    private BigDecimal axleFour;

    @MutableField( name = "Axle Five")
    @Column(name = "axle_five", precision = 11, scale = 2)
    private BigDecimal axleFive;

    @MutableField( name = "Axle Six")
    @Column(name = "axle_six", precision = 11, scale = 2)
    private BigDecimal axleSix;

    @MutableField( name = "Axle Seven")
    @Column(name = "axle_seven", precision = 11, scale = 2)
    private BigDecimal axleSeven;

    @MutableField( name = "Axle Count")
    @Column(name = "axle_count")
    private int axleCount;

    @Column(name = "CREATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "updated_by")
    private Long updatedBy;

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

    @JoinColumn(name = "created_by", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users createdByLink;

    @JoinColumn(name = "updated_by", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users updatedByLink;

    @JoinColumn(name = "REASON_CODE_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ReasonCodes reasonCodeLink;

    @JsonIgnore
    public Users getUpdatedByLink() {  return updatedByLink; }

    @JsonIgnore
    public ReasonCodes getReasonCodeLink() {  return reasonCodeLink; }

    @JsonIgnore
    public Users getCreatedByLink() {
        return createdByLink;
    }

    public AxleClassification createdOn(Long userId){
        this.updatedOn = new Date( System.currentTimeMillis() );
        this.createdOn = new Date( System.currentTimeMillis() );
        this.updatedBy = userId;
        this.createdBy = userId;
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AxleClassification updatedOn(Long userId){
        this.updatedOn = new Date( System.currentTimeMillis() );
        this.updatedBy = userId;
        return this;
    }

    public String getAxleCode() {
        return axleCode;
    }

    public AxleClassification setAxleCode(String axleCode) {
        this.axleCode = axleCode;
        return this;
    }

    public BigDecimal getAxleOne() {
        return axleOne;
    }

    public AxleClassification setAxleOne(BigDecimal axleOne) {
        this.axleOne = axleOne;
        return this;
    }

    public BigDecimal getAxleTwo() {
        return axleTwo;
    }

    public AxleClassification setAxleTwo(BigDecimal axleTwo) {
        this.axleTwo = axleTwo;
        return this;
    }

    public BigDecimal getAxleThree() {
        return axleThree;
    }

    public AxleClassification setAxleThree(BigDecimal axleThree) {
        this.axleThree = axleThree;
        return this;
    }

    public BigDecimal getAxleFour() {
        return axleFour;
    }

    public AxleClassification setAxleFour(BigDecimal axleFour) {
        this.axleFour = axleFour;
        return this;
    }

    public BigDecimal getAxleFive() {
        return axleFive;
    }

    public AxleClassification setAxleFive(BigDecimal axleFive) {
        this.axleFive = axleFive;
        return this;
    }

    public BigDecimal getAxleSix() {
        return axleSix;
    }

    public AxleClassification setAxleSix(BigDecimal axleSix) {
        this.axleSix = axleSix;
        return this;
    }

    public BigDecimal getAxleSeven() {
        return axleSeven;
    }

    public AxleClassification setAxleSeven(BigDecimal axleSeven) {
        this.axleSeven = axleSeven;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public AxleClassification setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public AxleClassification setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public AxleClassification setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public AxleClassification setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public String getFlag() {
        return flag;
    }

    public AxleClassification setFlag(String flag) {
        this.flag = flag;
        return this;
    }

    public int getAxleCount() {
        return axleCount;
    }

    public void setAxleCount(int axleCount) {
        this.axleCount = axleCount;
    }

    public String getEditData() {
        return editData;
    }

    public void setEditData(String editData) {
        this.editData = editData;
    }

    public Long getReasonCodeNo() {
        return reasonCodeNo;
    }

    public void setReasonCodeNo(Long reasonCodeNo) {
        this.reasonCodeNo = reasonCodeNo;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
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
        if (!(object instanceof Users)) {
            return false;
        }
        AxleClassification other = (AxleClassification) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Axle Classification[ " +
                "id=" + id +
                "axleCode=" + axleCode +
                "axleOne=" + axleOne +
                "axleTwo=" + axleTwo +
                "axleThree=" + axleThree +
                "axleFour=" + axleFour +
                "axleFive=" + axleFive +
                "axleSix=" + axleSix +
                "axleSeven=" + axleSeven +
                " ]";
    }

}
