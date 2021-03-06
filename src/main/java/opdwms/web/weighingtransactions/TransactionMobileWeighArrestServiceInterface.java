package opdwms.web.weighingtransactions;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @category    Weighing Transactions
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
public interface TransactionMobileWeighArrestServiceInterface {

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



}
