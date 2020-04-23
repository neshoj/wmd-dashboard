package opdwms.web.dataagentstatus.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import opdwms.core.template.forms.MutableField;
import opdwms.web.weighbridgestations.entities.WeighbridgeStations;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "data_agent_status")
public class DataAgentStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "last_updated_on")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdatedOn;

    @MutableField(name = "Weighbridge Station Type", entity = "WeighbridgeTypes", reference = "name", optional = true)
    @Column(name = "weighbridge_no")
    private Long weighbridgeTypeNo;

    @JoinColumn(name = "weighbridge_no", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private WeighbridgeStations weighbridgeStationsLink;

    public Long getId() {
        return id;
    }

    public DataAgentStatus setId(Long id) {
        this.id = id;
        return this;
    }

    public Date getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public DataAgentStatus setLastUpdatedOn(Date lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
        return this;
    }

    public Long getWeighbridgeTypeNo() {
        return weighbridgeTypeNo;
    }

    public DataAgentStatus setWeighbridgeTypeNo(Long weighbridgeTypeNo) {
        this.weighbridgeTypeNo = weighbridgeTypeNo;
        return this;
    }

    @JsonIgnore
    public WeighbridgeStations getWeighbridgeStationsLink() {
        return weighbridgeStationsLink;
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
        if (!(object instanceof DataAgentStatus)) {
            return false;
        }
        DataAgentStatus other = (DataAgentStatus) object;
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
