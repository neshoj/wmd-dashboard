package opdwms.web.configs.entities;


import opdwms.web.usermanager.entities.Users;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Entity
@Table(name = "audit_trail")
public class AuditTrail implements Serializable {
    public static final String USER_GENERATED = "user_generated";
    public static final String API_ERROR = "api";
    public static final String SYSTEM_ERROR = "system";
    public static final String MAIL_ERROR = "mail";

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Lob
    @Column(name = "ACTIVITY")
    private String activity;

    @Column(name = "STATUS", length = 45)
    private String status;

    @Column(name = "CREATED_ON")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn = new Date( System.currentTimeMillis() );

    @Lob
    @Column(name = "OLD_VALUES")
    private String oldValues = "N/A";

    @Lob
    @Column(name = "NEW_VALUES")
    private String newValues = "N/A";

    @Column( name = "LOG_TYPE")
    private String logType = USER_GENERATED;

    @Column(name = "USER_NO")
    private Long userNo;

    @JoinColumn(name = "USER_NO", referencedColumnName = "ID", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Users userLink;

    public AuditTrail() {}
    public AuditTrail(String activity, String status, Long userNo){
        this.activity = activity;
        this.status = status;
        this.userNo = userNo;
    }

    public AuditTrail(String activity, String status, Long userNo, String oldValues, String newValues){
        this.activity = activity;
        this.status = status;
        this.userNo = userNo;
        this.oldValues = oldValues;
        this.newValues = newValues;
    }

    public Long getId() { return id; }
    public AuditTrail setId(Long id) {
        this.id = id;
        return this;
    }

    public String getActivity() { return activity; }
    public AuditTrail setActivity(String activity) {
        this.activity = activity;
        return this;
    }

    public String getStatus() { return status; }
    public AuditTrail setStatus(String status) {
        this.status = status;
        return this;
    }

    public Date getCreatedOn() { return createdOn; }
    public AuditTrail setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
        return this;
    }

    public String getOldValues() { return oldValues; }
    public AuditTrail setOldValues(String oldValues) {
        this.oldValues = oldValues;
        return this;
    }

    public String getNewValues() { return newValues; }
    public AuditTrail setNewValues(String newValues) {
        this.newValues = newValues;
        return this;
    }

    public String getLogType() { return logType; }
    public AuditTrail setLogType(String logType) {
        this.logType = logType;
        return this;
    }

    public Long getUserNo() {
        return userNo;
    }
    public AuditTrail setUserNo(Long userNo) {
        this.userNo = userNo;
        return this;
    }

    public Users getUserLink() { return userLink; }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuditTrail)) {
            return false;
        }
        AuditTrail other = (AuditTrail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AuditTrail[ id=" + id + " ]";
    }
    
}
