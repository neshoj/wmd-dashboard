package opdwms.web.configs;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public interface MakerCheckerServiceInterface {


    /**
     * Edit a record
     *
     * @param request
     * @return  Map<String, Object>
     */
    public Map<String, Object> editRecord(HttpServletRequest request);

    /**
     * Fetch a record information
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> fetchRecord(HttpServletRequest request);
}
