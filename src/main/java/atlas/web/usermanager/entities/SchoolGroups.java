package atlas.web.usermanager.entities;

import atlas.web.schools.entities.Schools;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "schools_groups")
public class SchoolGroups implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "schools_no")
    private Long schoolNo;

    @JoinColumn(name = "id", referencedColumnName = "ID", nullable = false, insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private UserGroups userGroup;

    @JoinColumn(name = "schools_no", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Schools schoolsLink;

    @JsonIgnore
    public UserGroups getUserGroup() {
        return userGroup;
    }

    @JsonIgnore
    public Schools getSchoolsLink() {
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
        if (!(object instanceof SchoolGroups)) {
            return false;
        }
        SchoolGroups other = (SchoolGroups) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SchoolGroups[ " +
                "id=" + id +
                "schoolNo=" + schoolNo + " ]";
    }

    public Long getId() {
        return id;
    }

    public SchoolGroups setId(Long id) {
        this.id = id;
        return this;
    }

    public Long getSchoolNo() {
        return schoolNo;
    }

    public SchoolGroups setSchoolNo(Long schoolNo) {
        this.schoolNo = schoolNo;
        return this;
    }

    public SchoolGroups setUserGroup(UserGroups userGroup) {
        this.userGroup = userGroup;
        return this;
    }

    public SchoolGroups setSchoolsLink(Schools schoolsLink) {
        this.schoolsLink = schoolsLink;
        return this;
    }
}
