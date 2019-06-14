package opdwms.web.weighingtransactions.services;

import opdwms.web.configs.entities.AuditTrail;
import opdwms.web.configs.repository.AuditTrailRepository;
import opdwms.web.weighingtransactions.TransactionMobileWeighArrestServiceInterface;
import opdwms.web.weighingtransactions.entities.TransactionMobileWeighArrest;
import opdwms.web.weighingtransactions.forms.TransactionMobileWeighArrestForm;
import opdwms.web.weighingtransactions.repositories.TransactionMobileWeighArrestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ignatius
 * @version 1.0.0
 * @category Weighing Transactions
 * @package Dev
 * @since Nov 05, 2018
 */
@Service
@Transactional
public class TransactionMobileWeighArrestService implements TransactionMobileWeighArrestServiceInterface {

    private TransactionMobileWeighArrestRepository entityRepository;
    private TransactionMobileWeighArrestForm transactionMobileWeighArrestForm;
    private AuditTrailRepository auditTrailRepository;

    @Autowired
    public TransactionMobileWeighArrestService(
            TransactionMobileWeighArrestRepository entityRepository,
            TransactionMobileWeighArrestForm transactionMobileWeighArrestForm,
            AuditTrailRepository auditTrailRepository
    ) {
        this.entityRepository = entityRepository;
        this.transactionMobileWeighArrestForm = transactionMobileWeighArrestForm;
        this.auditTrailRepository = auditTrailRepository;
    }


    /**
     * Fetch a record information
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> fetchRecord(HttpServletRequest request) {
        String index = request.getParameter("index");
        return this.transactionMobileWeighArrestForm.transformEntity(index);
    }

    /**
     * Persist a record
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> saveRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Long userId = (Long) request.getSession().getAttribute("_userNo");

        TransactionMobileWeighArrest entity = this.transactionMobileWeighArrestForm.handleRequests(request);
        entity.createdOn(userId);
        entityRepository.save(entity);

        map.put("status", "00");
        map.put("message", "Request processed successfully");

        AuditTrail log = new AuditTrail();
        log
                .setLogType(AuditTrail.USER_GENERATED)
                .setActivity("Created a new mobile weigh arrest transaction : " + entity.getVehicleNo())
                .setNewValues(entity.getVehicleNo())
                .setOldValues("N/A")
                .setStatus("Success")
                .setUserNo(userId);
        auditTrailRepository.save(log);

        return map;
    }
}
