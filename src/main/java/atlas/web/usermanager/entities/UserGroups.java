/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atlas.web.usermanager.entities;

import atlas.core.template.forms.EditDataWrapper;
import atlas.core.template.forms.MutableField;
import atlas.web.configs.entities.ReasonCodes;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Entity
@Table(name = "user_groups")
public class UserGroups implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 100)
    @MutableField( name = "Name")
    @Column(name = "NAME", length = 100)
    private String name;

    @Size(max = 250)
    @MutableField( name = "Description")
    @Column(name = "DESCRIPTION", length = 250)
    private String description;

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

    @Size(max = 100)
    @MutableField( name = "System Role")
    @Column(name = "BASE_TYPE", length = 100)
    private String baseType;

    @Size(max = 200)
    @Column(name = "REASON_DESCRIPTION", length = 200)
    private String reasonDescription;

    @Lob
    @EditDataWrapper
    @Size(max = 2147483647)
    @Column(name = "EDIT_DATA", length = 2147483647)
    private String editData;

    @Column(name = "SYSTEM_DEFINED")
    private Boolean systemDefined;

    @Column( name = "REASON_CODE_NO")
    private Long reasonCodeNo;

    @JoinTable(name = "group_permissions", joinColumns = {
            @JoinColumn(name = "USER_GROUP_ID", referencedColumnName = "ID", nullable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "ID", nullable = false)})
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Permissions> permissions;

    @JoinColumn(name = "REASON_CODE_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private ReasonCodes reasonCodeLink;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "userGroup", fetch = FetchType.LAZY)
    private ClientsGroups clientsGroups;

    //DISCLAIMER: RELATION NOT ENFORCED IN THE DB
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users createdByLink;

    //DISCLAIMER: RELATION NOT ENFORCED IN THE DB
    @JoinColumn(name = "UPDATED_BY", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Users updatedByLink;

    public UserGroups() { }

    public Long getId() { return id;}
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name.trim();
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description.trim();
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

    public String getFlag() { return flag;  }
    public UserGroups setFlag(String flag) {
        this.flag = flag;
        return this;
    }

    public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
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

    public Boolean getSystemDefined() {
        return systemDefined;
    }

    public void setSystemDefined(Boolean systemDefined) {
        this.systemDefined = systemDefined;
    }

    public Set<Permissions> getPermissions() {return permissions; }
    public UserGroups setPermissions(Set<Permissions> permissions) {
        this.permissions = permissions;
        return this;
    }

    public Long getReasonCodeNo() {
        return reasonCodeNo;
    }

    public void setReasonCodeNo(Long reasonCodeNo) {
        this.reasonCodeNo = reasonCodeNo;
    }


    @JsonIgnore public ClientsGroups getClientsGroups() { return clientsGroups;    }
    @JsonIgnore public ReasonCodes getReasonCodeLink() {
        return reasonCodeLink;
    }
    @JsonIgnore public Users getCreatedByLink() {  return createdByLink; }
    @JsonIgnore public Users getUpdatedByLink() {  return updatedByLink; }

    public UserGroups createdOn(Long userId){
        this.updatedOn = new Date( System.currentTimeMillis() );
        this.createdOn = new Date( System.currentTimeMillis() );
        this.updatedBy = userId;
        this.createdBy = userId;
        return this;
    }

    public UserGroups updatedOn(Long userId){
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
        if (!(object instanceof UserGroups)) {
            return false;
        }
        UserGroups other = (UserGroups) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "UserGroups[ id=" + id + " ]";
    }
    
}
