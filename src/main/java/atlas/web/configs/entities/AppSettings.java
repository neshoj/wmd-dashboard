package atlas.web.configs.entities;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Entity
@Table(name = "app_settings")
public class AppSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Size(max = 150)
    @Column(name = "NAME", length = 150)
    private String name;

    @Size(max = 150)
    @Column(name = "CODE", length = 150)
    private String code;

    @Size(max = 100)
    @Column(name = "FLAG", length = 100)
    private String flag;

    @Size(max = 100)
    @Column(name = "VALUE", length = 100)
    private String value;

    @Size(max = 100)
    @Column(name = "DESCRIPTION", length = 100)
    private String description;

    public AppSettings(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {   return name;  }
    public AppSettings setName(String name) {
        this.name = name;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getValue() {  return value;}
    public AppSettings setValue(String value) {
        this.value = value;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof AppSettings)) {
            return false;
        }
        AppSettings other = (AppSettings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AppSettings[ id=" + id + " ]";
    }
    
}
