package opdwms.web.weighingtransactions.services;

import opdwms.web.configs.entities.AuditTrail;
import opdwms.web.configs.repository.AuditTrailRepository;
import opdwms.web.weighingtransactions.TransactionMobileWeighProsecutionServiceInterface;
import opdwms.web.weighingtransactions.entities.TransactionMobileWeighProsecution;
import opdwms.web.weighingtransactions.forms.TransactionMobileWeighProsecutionForm;
import opdwms.web.weighingtransactions.repositories.TransactionMobileWeighProsecutionRepository;
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
public class TransactionMobileWeighProsecutionService implements TransactionMobileWeighProsecutionServiceInterface {

    private TransactionMobileWeighProsecutionRepository entityRepository;
    private TransactionMobileWeighProsecutionForm transactionMobileWeighProsecutionForm;
    private AuditTrailRepository auditTrailRepository;

    @Autowired
    public TransactionMobileWeighProsecutionService(
            TransactionMobileWeighProsecutionRepository entityRepository,
            TransactionMobileWeighProsecutionForm transactionMobileWeighProsecutionForm,
            AuditTrailRepository auditTrailRepository
    ) {
        this.entityRepository = entityRepository;
        this.transactionMobileWeighProsecutionForm = transactionMobileWeighProsecutionForm;
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
        return this.transactionMobileWeighProsecutionForm.transformEntity(index);
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

        TransactionMobileWeighProsecution entity = this.transactionMobileWeighProsecutionForm.handleRequests(request);
        entity.createdOn(userId);
        entityRepository.save(entity);

        map.put("status", "00");
        map.put("message", "Request processed successfully");

        AuditTrail log = new AuditTrail();
        log
                .setLogType(AuditTrail.USER_GENERATED)
                .setActivity("Created a new mobile weigh prosecution transaction : " + entity.getVehicleNo())
                .setNewValues(entity.getVehicleNo())
                .setOldValues("N/A")
                .setStatus("Success")
                .setUserNo(userId);
        auditTrailRepository.save(log);

        return map;
    }
}
