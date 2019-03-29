package opdwms.web.usermanager.entities;

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
@Table(name = "permissions")
public class Permissions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 100)
    @Column(name = "NAME", length = 100)
    private String name;

    @Size(max = 100)
    @Column(name = "APP_CODE", length = 100)
    private String appCode;

    @Column(name = "CREATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(name = "UPDATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn;

    @Column(name = "ROLE_NO")
    private Long roleNo;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<UserGroups> userGroups;

    @JoinColumn(name = "ROLE_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private AppRoles roleLink;

    public Permissions() {  }

    public Long getId() {  return id;  }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {    return name;  }
    public Permissions setName(String name) {
        this.name = name;
        return this;
    }

    public String getAppCode() {  return appCode; }
    public Permissions setAppCode(String appCode) {
        this.appCode = appCode;
        return this;
    }

    public Date getCreatedOn() {   return createdOn;  }
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {  return updatedOn;  }
    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Long getRoleNo() { return roleNo;  }
    public Permissions setRoleNo(Long roleNo) {
        this.roleNo = roleNo;
        return this;
    }

    @JsonIgnore public Set<UserGroups> getUserGroups() { return userGroups; }
    @JsonIgnore public AppRoles getRoleLink() {   return roleLink; }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Permissions)) {
            return false;
        }
        Permissions other = (Permissions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Permissions[ id=" + id + " ]";
    }
    
}
