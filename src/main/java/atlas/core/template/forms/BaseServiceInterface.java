package atlas.core.template.forms;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *
 * @category    Forms
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 *
 */
public interface BaseServiceInterface {

    /**
     * Persist a new record 
     * 
     * @param request
     * @return  Map<String, Object>
     */
    public Map<String, Object> saveRecord(HttpServletRequest request);


    /**
     * Edit a record 
     * 
     * @param request
     * @return  Map<String, Object>
     */
    public Map<String, Object> editRecord(HttpServletRequest request);
    
    /**
     * Approve edit changes
     * 
     * @param request
     * @return  Map<String, Object>
     */
    public Map<String, Object> approveEditChanges(HttpServletRequest request);

    /**
     * Fetch a record information
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> fetchRecord(HttpServletRequest request);

    /**
     * Fetch edit changes
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> fetchRecordChanges(HttpServletRequest request);

    /**
     * Update record status
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> flagRecords(HttpServletRequest request);

    /**
     * Deactivate a record
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> deactivateRecord(HttpServletRequest request);

    /**
     * Fetch deactivation details
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> fetchDeactivationInfo(HttpServletRequest request);
}
