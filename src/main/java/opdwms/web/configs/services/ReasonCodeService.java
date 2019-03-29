package opdwms.web.configs.services;

import opdwms.core.template.AppConstants;
import opdwms.web.configs.ReasonCodeServiceInterface;
import opdwms.web.configs.entities.AuditTrail;
import opdwms.web.configs.entities.ReasonCodes;
import opdwms.web.configs.forms.ReasonCodeForm;
import opdwms.web.configs.repository.AuditTrailRepository;
import opdwms.web.configs.repository.ReasonCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Service
@Transactional
public class ReasonCodeService implements ReasonCodeServiceInterface {

    @Autowired
    private ReasonCodeRepository entityRepository;
    @Autowired
    private ReasonCodeForm reasonCodeForm;
    @Autowired
    private AuditTrailRepository auditTrailRepository;

    /**
     * Fetch all persisted records
     *
     * @return List<ReasonCodes>
     */
    @Override
    public List<ReasonCodes> fetchRecords(){
        return (List)entityRepository.findAllByFlag(AppConstants.STATUS_ACTIVERECORD);
    }

    /**
     * Fetch a record information
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> fetchRecord(HttpServletRequest request){
        String index = request.getParameter("index");
        return this.reasonCodeForm.transformEntity( index );
    }

    /**
     * Persist a record
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> saveRecord(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        Long userId = (Long)request.getSession().getAttribute("_userNo");

        ReasonCodes entity = this.reasonCodeForm.handleRequests( request );
        entity.setFlag( AppConstants.STATUS_ACTIVERECORD ).createdOn( userId );
        entityRepository.save( entity );

        map.put("status", "00");
        map.put("message", "Request processed successfully");


        AuditTrail log = new AuditTrail();
        log
                .setLogType( AuditTrail.USER_GENERATED )
                .setActivity("Created a new reason code : " + entity.getName() )
                .setNewValues( entity.getName() )
                .setOldValues("N/A")
                .setStatus("Success")
                .setUserNo( userId );
        auditTrailRepository.save( log );

        return map;
    }

    /**
     * Updates a record with given changes
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> updateRecord(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        Long userId = (Long)request.getSession().getAttribute("_userNo");

        ReasonCodes changes = this.reasonCodeForm.handleRequests( request );

        ReasonCodes entity = this.entityRepository.findById( changes.getId() ).get();
        entity.setName( changes.getName() ).setDescription( changes.getDescription() ).updatedOn( userId );
        entityRepository.save( entity );

        map.put("status", "00");
        map.put("message", "Request processed successfully");

        return map;
    }

    /**
     *  Soft deletes a record in storage
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> deleteRecord(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        Long userId = (Long)request.getSession().getAttribute("_userNo");

        String index = request.getParameter("index");
        ReasonCodes entity = this.entityRepository.findById( Long.valueOf( index )).get();
        entity.updatedOn( userId ).setFlag( AppConstants.STATUS_SOFTDELETED );
        this.entityRepository.save( entity );

        map.put("status", "00");
        map.put("message", "Request processed successfully");
        return map;
    }
}
