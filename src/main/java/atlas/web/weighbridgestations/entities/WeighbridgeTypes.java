package atlas.web.weighbridgestations.entities;

import atlas.core.template.forms.MutableField;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table( name="weighbridge_types")
public class WeighbridgeTypes implements Serializable {

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

    @Size(max = 100)
    @MutableField( name = "Description ")
    @Column(name = "description", length = 100)
    private String description;

    public Long getId() {
        return id;
    }

    public WeighbridgeTypes setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public WeighbridgeTypes setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public WeighbridgeTypes setDescription(String description) {
        this.description = description;
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
        if (!(object instanceof WeighbridgeTypes)) {
            return false;
        }
        WeighbridgeTypes other = (WeighbridgeTypes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "weighbridge Types[ " +
                "id=" + id +
                "Name=" + name +
                "Description=" + description +
                " ]";
    }
}
