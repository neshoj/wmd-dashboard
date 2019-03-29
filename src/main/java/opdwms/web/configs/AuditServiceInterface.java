package opdwms.web.configs;


import opdwms.web.configs.entities.AuditTrail;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public interface AuditServiceInterface {

    /**
     *  Save record given the current user's principal name
     *
     * @param trail
     * @param email
     */
    public void saveLog(AuditTrail trail, String email);

}
