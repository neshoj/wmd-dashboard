package opdwms.web.weighbridgestations.services;

import opdwms.web.configs.repository.AuditTrailRepository;
import opdwms.web.weighbridgestations.WeighbridgeTypesServiceInterface;
import opdwms.web.weighbridgestations.entities.WeighbridgeTypes;
import opdwms.web.weighbridgestations.forms.WeighbridgeTypesForm;
import opdwms.web.weighbridgestations.repository.WeighbridgeTypesRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WeighBridgeTypesService implements WeighbridgeTypesServiceInterface {

    @Autowired
    private WeighbridgeTypesForm entityForm;
    @Autowired
    private WeighbridgeTypesRepository entityRepository;
    @Autowired
    private AuditTrailRepository auditTrailRepository;


    @Override
    public List<WeighbridgeTypes> fetchRecords(HttpServletRequest request) {
        List<WeighbridgeTypes> targetCollection = new ArrayList<>();
        CollectionUtils.addAll(targetCollection, entityRepository.findAll().iterator());
        return targetCollection;
    }


}
