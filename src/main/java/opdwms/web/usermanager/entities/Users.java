package opdwms.web.usermanager.entities;

import opdwms.core.template.forms.EditDataWrapper;
import opdwms.core.template.forms.MutableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import opdwms.web.configs.entities.ReasonCodes;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Entity
@Table(name = "users")
public class Users implements Serializable {

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
    @Column(name = "SURNAME", length = 100)
    private String surname;

    @Size(max = 250)
    @MutableField( name = "Email")
    @Column(name = "EMAIL", length = 250)
    private String email;

    @Size(max = 20)
    @MutableField( name = "Mobile Phone No.")
    @Column(name = "PHONE", length = 20)
    private String phone;

    @Size(max = 100)
    @Column(name = "PASSWORD", length = 100)
    private String password;

    @Lob
    @Size(max = 2147483647)
    @Column(name = "PHOTO_URL", length = 2147483647)
    private String photoUrl;

    @Size(max = 200)
    @Column(name = "PHOTO_KEY", length = 200)
    private String photoKey;

    @Column(name = "EXPIRY")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiry;

    @Column(name = "ENABLED")
    private Boolean enabled = false;

    @Column(name = "NON_LOCKED")
    private Boolean nonlocked = true ;

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

    @Size(max = 200)
    @Column(name = "MOBILE_TOKEN", length = 200)
    private String mobileToken;

    @Size(max = 200)
    @Column(name = "EMAIL_TOKEN", length = 200)
    private String emailToken;

    @Size(max = 600)
    @Column(name = "FCM_TOKEN", length = 600)
    private String fcmToken;

    @Size(max = 200)
    @Column(name = "RESET_KEY", length = 200)
    private String resetKey;

    @Column(name = "MOBILE_VERIFIED")
    private boolean mobileVerified = false;

    @Column(name = "EMAIL_VERIFIED")
    private boolean emailVerified = false;

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

    @Column(name = "LAST_TIME_PASSWORD_UPDATED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTimePasswordUpdated;

    @Column(name = "PASSWORD_NEVER_EXPIRES")
    private Boolean passwordNeverExpires;

    @Column(name = "RESET_REQ_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resetReqDate;

    @Size(max = 200)
    @Column(name = "ACTIVATION_KEY", length = 200)
    private String activationKey;

    @MutableField( name = "Usergroup", entity = "UserGroups", reference = "name")
    @Column(name = "USER_GROUP_NO")
    private Long userGroupNo;

    //@MutableField( name = "User Type", entity = "UserTypes", reference = "name")
    @Column(name = "USER_TYPE_NO")
    private Long userTypeNo;

    @Column( name = "REASON_CODE_NO")
    private Long reasonCodeNo;

    @Transient
    private String fullNames;

    @JoinColumn(name = "REASON_CODE_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ReasonCodes reasonCodeLink;

    @JoinColumn(name = "USER_GROUP_NO", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserGroups userGroupLink;

    @JoinColumn(name = "USER_TYPE_NO", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private UserTypes userTypeLink;

    @JoinColumn(name = "CREATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users createdByLink;

    @JoinColumn(name = "UPDATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users updatedByLink;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "usersLink", fetch = FetchType.LAZY)
    private ClientsUsers clientUsersLink;

    public Users() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {   return firstName; }
    public Users setFirstName(String firstName) {
        this.firstName = firstName.trim();
        return this;
    }

    public String getSurname() {   return surname;  }
    public Users setSurname(String surname) {
        this.surname = surname.trim();
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {  return password;}
    public Users setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoKey() {   return photoKey; }
    public Users setPhotoKey(String photoKey) {
        this.photoKey = photoKey;
        return this;
    }

    public Date getExpiry() {
        return expiry;
    }

    public void setExpiry(Date expiry) {
        this.expiry = expiry;
    }

    public Boolean getEnabled() {  return enabled; }
    public Users setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public Boolean getNonlocked() {
        return nonlocked;
    }

    public void setNonlocked(Boolean nonlocked) {
        this.nonlocked = nonlocked;
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

    public String getMobileToken() {
        return mobileToken;
    }

    public void setMobileToken(String mobileToken) {
        this.mobileToken = mobileToken;
    }

    public String getEmailToken() {  return emailToken; }
    public Users setEmailToken(String emailToken) {
        this.emailToken = emailToken;
        return this;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public boolean isMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(boolean mobileVerified) {
        this.mobileVerified = mobileVerified;
    }

    public boolean isEmailVerified() {    return emailVerified; }
    public Users setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    public String getFlag() {
        return flag;
    }

    public Users setFlag(String flag) {
        this.flag = flag;
        return this;
    }

    public String getEditData() {
        return editData;
    }

    public void setEditData(String editData) {
        this.editData = editData;
    }

    public String getReasonDescription() {
        return reasonDescription;
    }

    public void setReasonDescription(String reasonDescription) {
        this.reasonDescription = reasonDescription;
    }

    public Date getLastTimePasswordUpdated() {
        return lastTimePasswordUpdated;
    }

    public void setLastTimePasswordUpdated(Date lastTimePasswordUpdated) {
        this.lastTimePasswordUpdated = lastTimePasswordUpdated;
    }

    public Boolean getPasswordNeverExpires() {
        return passwordNeverExpires;
    }

    public void setPasswordNeverExpires(Boolean passwordNeverExpires) {
        this.passwordNeverExpires = passwordNeverExpires;
    }

    public Date getResetReqDate() {    return resetReqDate;  }
    public Users setResetReqDate(Date resetReqDate) {
        this.resetReqDate = resetReqDate;
        return this;
    }

    public String getActivationKey() {
        return activationKey;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public Long getUserGroupNo() {
        return userGroupNo;
    }

    public void setUserGroupNo(Long userGroupNo) {
        this.userGroupNo = userGroupNo;
    }

    public Long getUserTypeNo() {
        return userTypeNo;
    }

    public void setUserTypeNo(Long userTypeNo) {
        this.userTypeNo = userTypeNo;
    }

    public Long getReasonCodeNo() {
        return reasonCodeNo;
    }

    public void setReasonCodeNo(Long reasonCodeNo) {
        this.reasonCodeNo = reasonCodeNo;
    }

    public String getFullNames() {
        this.fullNames = String.format("%s %s", this.firstName, this.surname );
        return fullNames;
    }


    @JsonIgnore public ClientsUsers getClientUsersLink() {
        return clientUsersLink;
    }
    @JsonIgnore public ReasonCodes getReasonCodeLink() { return reasonCodeLink;  }
    @JsonIgnore public UserGroups getUserGroupLink() { return userGroupLink; }
    @JsonIgnore public UserTypes getUserTypeLink() { return userTypeLink; }

    @JsonIgnore public Users getCreatedByLink() {  return createdByLink; }
    @JsonIgnore public Users getUpdatedByLink() {  return updatedByLink; }

    public Users createdOn(Long userId){
        this.updatedOn = new Date( System.currentTimeMillis() );
        this.createdOn = new Date( System.currentTimeMillis() );
        this.updatedBy = userId;
        this.createdBy = userId;
        return this;
    }

    public Users updatedOn(Long userId){
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Users[ id=" + id + " ]";
    }
    
}
