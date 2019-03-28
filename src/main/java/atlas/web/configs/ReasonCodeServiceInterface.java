package atlas.web.configs;


import atlas.web.configs.entities.ReasonCodes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public interface ReasonCodeServiceInterface {

    /**
     * Fetch all persisted records
     *
     * @return List<ReasonCodes>
     */
    public List<ReasonCodes> fetchRecords();

    /**
     * Fetch a record information
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> fetchRecord(HttpServletRequest request);

    /**
     * Persist a record
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> saveRecord(HttpServletRequest request);

    /**
     * Updates a record with given changes
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> updateRecord(HttpServletRequest request);

    /**
     *  Soft deletes a record in storage
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> deleteRecord(HttpServletRequest request);

}
