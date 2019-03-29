package atlas.web.usermanager.entities;

import atlas.web.clients.entities.Clients;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "clients_groups")
public class ClientsGroups implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "client_no")
    private Long clientNo;

    @JoinColumn(name = "id", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private UserGroups userGroup;

    @JoinColumn(name = "client_no", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Clients clientsLink;

    @JsonIgnore
    public UserGroups getUserGroup() {
        return userGroup;
    }


    @JsonIgnore
    public Clients getClientsLink() {
        return clientsLink;
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
        if (!(object instanceof ClientsGroups)) {
            return false;
        }
        ClientsGroups other = (ClientsGroups) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Clients Groups[ " +
                "id=" + id +
                "clientNo=" + clientNo + " ]";
    }

    public Long getId() {
        return id;
    }

    public ClientsGroups setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getClientNo() {
        return clientNo;
    }

    public ClientsGroups setClientNo(Long clientNo) {
        this.clientNo = clientNo;
        return this;
    }

    public ClientsGroups setUserGroup(UserGroups userGroup) {
        this.userGroup = userGroup;
        return this;
    }
}
