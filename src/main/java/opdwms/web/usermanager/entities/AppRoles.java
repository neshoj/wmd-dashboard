/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opdwms.web.usermanager.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Entity
@Table(name = "app_roles")
public class AppRoles implements Serializable {

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

    @Column(name = "CREATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date( System.currentTimeMillis() );

    @Column(name = "UPDATED_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date( System.currentTimeMillis() );

    @Size(max = 200)
    @Column(name = "APP_FUNCTION", length = 200)
    private String appFunction;

//    @OneToMany(mappedBy = "roleNo", fetch = FetchType.LAZY)
//    private Set<Permissions> permissionsSet;

    //Permissions linked to this record
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleNo", fetch = FetchType.LAZY)
    private Set<Permissions> permissions;

    public AppRoles() { }

    public Long getId() { return id; }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {  return name;   }
    public AppRoles setName(String name) {
        this.name = name;
        return this;
    }

    public String getAppCode() {    return appCode;  }
    public AppRoles setAppCode(String appCode) {
        this.appCode = appCode;
        return this;
    }

    public Date getCreatedAt() {  return createdAt; }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {   return updatedAt;  }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAppFunction() {   return appFunction;  }
    public AppRoles setAppFunction(String appFunction) {
        this.appFunction = appFunction;
        return this;
    }

    @JsonIgnore public Set<Permissions> getPermissions() {  return permissions;}
    public void setPermissions(Set<Permissions> permissions) {
        this.permissions = permissions;
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
        if (!(object instanceof AppRoles)) {
            return false;
        }
        AppRoles other = (AppRoles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AppRoles[ id=" + id + " ]";
    }
    
}
