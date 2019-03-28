package atlas.web.usermanager.entities;

import atlas.web.schools.entities.Schools;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "schools_users")
public class SchoolUsers {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "schools_no")
    private Long school;

    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Users usersLink;

    @JoinColumn(name = "schools_no", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Schools schoolsLink;

    @JsonIgnore public Users getUsersLink() {  return usersLink; }
    @JsonIgnore public Schools getSchoolsLink() {
        return schoolsLink;
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
        if (!(object instanceof SchoolUsers)) {
            return false;
        }
        SchoolUsers other = (SchoolUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SchoolUsers[ " +
                "   id=" + id +
                "   schoolNo=" + school +
                " ]";
    }

    public Long getId() {
        return id;
    }

    public SchoolUsers setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getSchool() {
        return school;
    }

    public SchoolUsers setSchool(Long school) {
        this.school = school;
        return this;
    }
}
