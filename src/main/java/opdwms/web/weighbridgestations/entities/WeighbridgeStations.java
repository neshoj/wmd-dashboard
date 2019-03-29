package opdwms.web.weighbridgestations.entities;

import opdwms.core.template.forms.EditDataWrapper;
import opdwms.core.template.forms.MutableField;
import opdwms.web.configs.entities.ReasonCodes;
import opdwms.web.usermanager.entities.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table( name="weighbridge_stations")
public class WeighbridgeStations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 100)
    @MutableField( name = "Name ")
    @Column(name = "NAME", length = 100)
    private String name;

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 200)
    @MutableField( name = "Station Code ")
    @Column(name = "station_code", length = 200)
    private String stationCode;

    @Size(max = 200)
    @MutableField( name = "Location ")
    @Column(name = "LOCATION", length = 200)
    private String location;

    @Size(max = 20)
    @MutableField( name = "Mobile No ")
    @Column(name = "MOBILE_NO", length = 20)
    private String mobileNo;

    @Column(name = "CREATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "UPDATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;

    @Column(name = "DELETED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedOn;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "UPDATED_BY")
    private Long updatedBy;

    @Size(max = 20)
    @Column(name = "FLAG", length = 20)
    private String flag;

    @Size(max = 200)
    @Column(name = "REASON_DESCRIPTION", length = 200)
    private String reasonDescription;

    @Lob
    @EditDataWrapper
    @Size(max = 2147483647)
    @Column(name = "EDIT_DATA", length = 2147483647)
    private String editData;

    @Column(name = "REASON_CODE_NO")
    private Long reasonCodeNo;

    @JoinColumn(name = "REASON_CODE_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ReasonCodes reasonCodeLink;

    @MutableField( name = "Weighbridge Station Type", entity = "WeighbridgeTypes", reference = "name", optional = true)
    @Column(name = "weighbridge_types_no")
    private Long weighbridgeTypeNo;

    @JoinColumn(name = "weighbridge_types_no", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private WeighbridgeTypes weighbridgeTypesLink;

    //DISCLAIMER: RELATION NOT ENFORCED IN THE DB
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users createdByLink;

    //DISCLAIMER: RELATION NOT ENFORCED IN THE DB
    @JoinColumn(name = "UPDATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users updatedByLink;

    public WeighbridgeStations() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStationCode() {
        return stationCode;
    }

    public WeighbridgeStations setStationCode(String stationCode) {
        this.stationCode = stationCode;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Long getWeighbridgeTypeNo() {
        return weighbridgeTypeNo;
    }

    public WeighbridgeStations setWeighbridgeTypeNo(Long weighbridgeTypeNo) {
        this.weighbridgeTypeNo = weighbridgeTypeNo;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Date getDeletedOn() {
        return deletedOn;
    }

    public void setDeletedOn(Date deletedOn) {
        this.deletedOn = deletedOn;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getFlag() {   return flag;  }
    public WeighbridgeStations setFlag(String flag) {
        this.flag = flag;
        return this;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
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

//    public String getCountry() {
//        return country;
//    }
//
//    public WeighbridgeStations setCountry(String country) {
//        this.country = country;
//        return this;
//    }

    @JsonIgnore
    public WeighbridgeTypes getWeighbridgeTypesLink() {
        return weighbridgeTypesLink;
    }

    @JsonIgnore public ReasonCodes getReasonCodeLink() { return reasonCodeLink; }

    @JsonIgnore public Users getCreatedByLink() {  return createdByLink; }
    @JsonIgnore public Users getUpdatedByLink() {  return updatedByLink; }

    public WeighbridgeStations createdOn(Long userId){
        this.updatedOn = new Date( System.currentTimeMillis() );
        this.createdOn = new Date( System.currentTimeMillis() );
        this.updatedBy = userId;
        this.createdBy = userId;
        return this;
    }

    public WeighbridgeStations updatedOn(Long userId){
        this.updatedOn = new Date( System.currentTimeMillis() );
        this.updatedBy = userId;
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
        if (!(object instanceof WeighbridgeStations)) {
            return false;
        }
        WeighbridgeStations other = (WeighbridgeStations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AxleConfiguration[ id=" + id + " ]";
    }
}
