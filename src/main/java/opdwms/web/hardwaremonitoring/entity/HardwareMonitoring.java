package opdwms.web.hardwaremonitoring.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "hardware_monitoring")
public class HardwareMonitoring implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Basic(optional = false)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "site_name")
  private String siteName;

  @Column(name = "site_ip_address")
  private String siteIPAddress;

  @Column(name = "device_name")
  private String deviceName;

  @Column(name = "last_updated_on")
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastUpdatedOn;

  public Long getId() {
    return id;
  }

  public HardwareMonitoring setId(Long id) {
    this.id = id;
    return this;
  }

    public String getSiteName() {
        return siteName;
    }

    public HardwareMonitoring setSiteName(String siteName) {
        this.siteName = siteName;
        return this;
    }

    public String getSiteIPAddress() {
        return siteIPAddress;
    }

    public HardwareMonitoring setSiteIPAddress(String siteIPAddress) {
        this.siteIPAddress = siteIPAddress;
        return this;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public HardwareMonitoring setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public Date getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  public HardwareMonitoring setLastUpdatedOn(Date lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
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
    if (!(object instanceof HardwareMonitoring)) {
      return false;
    }
    HardwareMonitoring other = (HardwareMonitoring) object;
    if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Data Agent Status [ id=" + id + " ]";
  }
}
