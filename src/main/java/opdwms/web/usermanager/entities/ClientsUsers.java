package opdwms.web.usermanager.entities;

import opdwms.web.clients.entities.Clients;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "client_users")
public class ClientsUsers {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "client_no")
    private Long clientNo;

    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Users usersLink;

    @JoinColumn(name = "client_no", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Clients clientLink;

    @JsonIgnore public Users getUsersLink() {  return usersLink; }
    @JsonIgnore    public Clients getClientLink() {        return clientLink;    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientsUsers)) {
            return false;
        }
        ClientsUsers other = (ClientsUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ClientsUsers[ " +
                "   id=" + id +
                "   Client No=" + clientNo +
                " ]";
    }

    public Long getId() {
        return id;
    }

    public ClientsUsers setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getClientNo() {
        return clientNo;
    }

    public ClientsUsers setClientNo(Long clientNo) {
        this.clientNo = clientNo;
        return this;
    }
}
