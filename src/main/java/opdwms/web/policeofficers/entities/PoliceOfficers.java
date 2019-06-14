package opdwms.web.policeofficers.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import opdwms.core.template.forms.EditDataWrapper;
import opdwms.core.template.forms.MutableField;
import opdwms.web.configs.entities.ReasonCodes;
import opdwms.web.usermanager.entities.Users;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "police_officer_details")
public class PoliceOfficers {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 100)
    @MutableField( name = "First Name")
    @Column(name = "FIRST_NAME", length = 100)
    private String firstName;

    @Size(max = 100)
    @MutableField( name = "Last Name")
    @Column(name = "LAST_NAME", length = 100)
    private String surname;

    @MutableField( name = "Gender")
    @Column(name = "GENDER", length = 1)
    private String gender;

    @Size(max = 100)
    @MutableField( name = "Police No. ")
    @Column(name = "POLICE_NO", length = 100)
    private String policeNo;

    @Column(name = "CREATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "UPDATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;

    @Column(name = "CREATED_BY")
    private Long createdBy;

    @Column(name = "UPDATED_BY")
    private Long updatedBy;

    @Size(max = 20)
    @Column(name = "FLAG", length = 20)
    private String flag;

    @Lob
    @EditDataWrapper
    @Size(max = 2147483647)
    @Column(name = "EDIT_DATA", length = 2147483647)
    private String editData;

    @Size(max = 200)
    @Column(name = "REASON_DESCRIPTION", length = 200)
    private String reasonDescription;

    @Column( name = "REASON_CODE_NO")
    private Long reasonCodeNo;

    @JoinColumn(name = "REASON_CODE_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ReasonCodes reasonCodeLink;

    @JoinColumn(name = "CREATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users createdByLink;

    @JoinColumn(name = "UPDATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users updatedByLink;

    public Long getId() {
        return id;
    }

    public PoliceOfficers setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public PoliceOfficers setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public PoliceOfficers setSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public PoliceOfficers setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getPoliceNo() {
        return policeNo;
    }

    public PoliceOfficers setPoliceNo(String policeNo) {
        this.policeNo = policeNo;
        return this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public PoliceOfficers setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public PoliceOfficers setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
        return this;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public PoliceOfficers setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
        return this;
    }

    public Long getUpdatedBy() {
        return updatedBy;
    }

    public PoliceOfficers setUpdatedBy(Long updatedBy) {
        this.updatedBy = updatedBy;
        return this;
    }

    public String getFlag() {
        return flag;
    }

    public PoliceOfficers setFlag(String flag) {
        this.flag = flag;
        return this;
    }

    public String getEditData() {
        return editData;
    }

    public PoliceOfficers setEditData(String editData) {
        this.editData = editData;
        return this;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }

    public PoliceOfficers setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
        return this;
    }

    public Long getReasonCodeNo() {
        return reasonCodeNo;
    }

    public PoliceOfficers setReasonCodeNo(Long reasonCodeNo) {
        this.reasonCodeNo = reasonCodeNo;
        return this;
    }

    @JsonIgnore
    public ReasonCodes getReasonCodeLink() {
        return reasonCodeLink;
    }

    @JsonIgnore
    public Users getCreatedByLink() {
        return createdByLink;
    }

    @JsonIgnore
    public Users getUpdatedByLink() {
        return updatedByLink;
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
        PoliceOfficers other = (PoliceOfficers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public PoliceOfficers createdOn(Long userId){
        this.updatedOn = new Date( System.currentTimeMillis() );
        this.createdOn = new Date( System.currentTimeMillis() );
        this.updatedBy = userId;
        this.createdBy = userId;
        return this;
    }

    public PoliceOfficers updatedOn(Long userId){
        this.updatedOn = new Date( System.currentTimeMillis() );
        this.updatedBy = userId;
        return this;
    }

    @Override
    public String toString() {
        return "Police Officers" +
                "[ id=" + id +
                " firstName=" + firstName +
                " surname=" + surname +
                " policeNo=" + policeNo +
                " gender=" + gender +
                " ]";
    }

}
