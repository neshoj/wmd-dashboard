package opdwms.core.template.forms;

/**
 * @category    Forms
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public class AuditData {

    private String oldValue = "";
    private String newValue = "";
    private Long userNo;
    private String email;
    private String logType;
    private String description = "";
    private String status;

    public String getOldValue() {
        return oldValue;
    }

    public AuditData setOldValue(String oldValue) {
        this.oldValue = oldValue;
        return this;
    }

    public String getNewValue() {
        return newValue;
    }

    public AuditData setNewValue(String newValue) {
        this.newValue = newValue;
        return this;
    }

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getDescription() {
        return description;
    }

    public AuditData setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
