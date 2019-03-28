package atlas.web.configs.services;

import atlas.web.configs.MakerCheckerServiceInterface;
import atlas.web.configs.entities.MakerChecker;
import atlas.web.configs.forms.MakerCheckerForm;
import atlas.web.configs.repository.AuditTrailRepository;
import atlas.web.configs.repository.MakerCheckerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Service
@Transactional
public class MakerCheckerService implements MakerCheckerServiceInterface {

    @Autowired
    private MakerCheckerRepository entityRepo;
    @Autowired
    private AuditTrailRepository auditRepository;
    @Autowired
    private MakerCheckerForm entityForm;

    /**
     * Edit a record
     *
     * @param request
     * @return  Map<String, Object>
     */
    @Override
    public Map<String, Object> editRecord(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        String index = request.getParameter("id");
        String enabled = request.getParameter("enabled");

        Optional<MakerChecker> oEntity = this.entityRepo.findById( Long.valueOf( index ) );
        if( oEntity.isPresent() ) {
            MakerChecker entity = oEntity.get();
            entity.setEnabled(enabled.equalsIgnoreCase("1"));
            this.entityRepo.save(entity);
        }

        map.put("status", "00");
        map.put("message", "Request processed successfully");
        return map;
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
        return this.entityForm.transformEntity(index);
    }
}
