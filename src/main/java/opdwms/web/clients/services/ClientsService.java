package opdwms.web.clients.services;

import opdwms.core.template.AppConstants;
import opdwms.core.template.forms.AuditData;
import opdwms.web.clients.ClientsServiceInterface;
import opdwms.web.clients.entities.Clients;
import opdwms.web.clients.forms.ClientsForm;
import opdwms.web.clients.repository.ClientsRepository;
import opdwms.web.configs.entities.AuditTrail;
import opdwms.web.configs.entities.ReasonCodes;
import opdwms.web.configs.repository.AuditTrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ClientsService implements ClientsServiceInterface {

    private ClientsForm entityForm;
    private ClientsRepository entityRepository;
    private AuditTrailRepository auditTrailRepository;

    @Autowired
    public ClientsService(ClientsRepository entityRepository,
                          ClientsForm entityForm,
                          AuditTrailRepository auditTrailRepository) {
        this.entityRepository = entityRepository;
        this.entityForm = entityForm;
        this.auditTrailRepository = auditTrailRepository;
    }

    @Override
    public Map<String, Object> saveRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Long userId = (Long) request.getSession().getAttribute("_userNo");
        Clients entity = entityForm.handleRequests(request);

        if (entityRepository.findByName(entity.getName().trim()).isPresent()) {
            map.put("status", "01");
            map.put("message", "Name already exists.");
            return map;
        }

        entity
                .createdOn(userId)
                .setFlag(AppConstants.STATUS_NEWRECORD);

        /*Save record*/
        entityRepository.save(entity);

        AuditTrail log = new AuditTrail();
        log
                .setLogType(AuditTrail.USER_GENERATED)
                .setActivity("Created a new client : " + entity.getName())
                .setNewValues(entity.getName())
                .setOldValues("N/A")
                .setStatus("Success")
                .setUserNo(userId);

        auditTrailRepository.save(log);

        map.put("status", "00");
        map.put("message", "Request processed successfully");
        return map;
    }

    @Override
    public Map<String, Object> editRecord(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        AuditTrail log = new AuditTrail();
        Long userNo = (Long) request.getSession().getAttribute("_userNo");

        boolean isModified = entityForm.handleEditRequest(request);
        Clients record = entityForm.getEntity();

        if (entityRepository.findByNameAndIdNot(record.getName(), record.getId()).isPresent()) {
            //Generate log
            log
                    .setLogType(AuditTrail.USER_GENERATED)
                    .setActivity("Attempt to update client details failed: new name already exists")
                    .setStatus("Success")
                    .setUserNo(userNo);

            map.put("status", "01");
            map.put("message", "Name already exists.");
        } else {

            //If record has changes
            if (isModified) {
                record
                        .setFlag(AppConstants.STATUS_EDITEDRECORD)
                        .updatedOn(userNo);
                //Persist record
                entityRepository.save(record);
                //Generate log
                AuditData auditData = entityForm.auditData();
                log
                        .setLogType(AuditTrail.USER_GENERATED)
                        .setActivity(String.format("Edited client - %s : %s", record.getName(), auditData.getDescription()))
                        .setNewValues(auditData.getNewValue())
                        .setOldValues(auditData.getOldValue())
                        .setStatus("Success")
                        .setUserNo(userNo);

                map.put("status", "00");
                map.put("message", "Request processed successfully");
            }

            //No changes were made
            else {
                //Generate log
                log
                        .setLogType(AuditTrail.USER_GENERATED)
                        .setActivity(String.format("Attempt to update client - %s : no changes made", record.getName()))
                        .setNewValues("N/A")
                        .setOldValues("N/A")
                        .setStatus("Failed")
                        .setUserNo(userNo);

                map.put("status", "01");
                map.put("message", "No changes were made to this record");
            }
        }

        auditTrailRepository.save(log);
        return map;
    }

    @Override
    public Map<String, Object> approveEditChanges(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        Long index = Long.valueOf(request.getParameter("index"));
        String action = request.getParameter("action");

        Clients record = entityRepository.findById(index).get();
        boolean proceed = entityForm.applyMakerChecker(record, action);

        map.put("message", entityForm.getResponse());
        if (proceed) {
            record = entityForm.getEntity();
            entityRepository.save(record);
            map.put("status", "00");
        } else map.put("status", "01");

        //Insert logs
        AuditTrail log = entityForm.getLog().setUserNo(userNo);
        String activity = String.format("%s Reference: %s", log.getActivity(), record.getName());
        log.setActivity(activity);
        auditTrailRepository.save(log);
        return map;
    }

    @Override
    public Map<String, Object> fetchRecord(HttpServletRequest request) {
        String index = request.getParameter("index");
        return entityForm.transformEntity(index);
    }

    @Override
    public Map<String, Object> fetchRecordChanges(HttpServletRequest request) {
        String index = request.getParameter("index");
        return entityForm.fetchChanges( index );
    }

    @Override
    public Map<String, Object> flagRecords(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        String action = request.getParameter("action");
        Long index = Long.valueOf( request.getParameter("index") );
        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        Clients record = entityRepository.findById( index ).get();


        boolean success = entityForm.applyMakerChecker(record, action);
        map.put("message", entityForm.getResponse());

        if ( success ) {
            record = entityForm.getEntity();
            entityRepository.save( record );
            map.put("status", "00");
        }
        else map.put("status", "01");

        //Insert logs
        AuditTrail log = entityForm.getLog().setUserNo( userNo );
        String logActivity = String.format( "%s Reference: %s", log.getActivity(), record.getName());
        log.setActivity( logActivity );
        auditTrailRepository.save(log);
        return map;
    }

    @Override
    public Map<String, Object> deactivateRecord(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        Clients record = entityForm.deactivateRequest( request );
        record
                .setFlag( AppConstants.STATUS_DEACTIVATED )
                .updatedOn( userNo );
        entityRepository.save( record );

        AuditTrail log = new AuditTrail()
                .setActivity(String.format("Deactivated a client successfully. Reference %s", record.getName() ) )
                .setStatus("Success")
                .setOldValues("Active").setNewValues("Deactivated")
                .setUserNo( userNo );
        auditTrailRepository.save( log );

        map.put("status", "00");
        map.put("message", "Request processed successfully");

        return map;
    }

    @Override
    public Map<String, Object> fetchDeactivationInfo(HttpServletRequest request) {

        Map<String, Object> map = new HashMap<>();
        Long index = Long.valueOf( request.getParameter("index") );

        Clients record = entityRepository.findById( index ).get();
        map.put("index", record.getId() );
        map.put("editor", record.getUpdatedByLink().getFullNames() );

        if( null != record.getReasonCodeLink() ){
            ReasonCodes reasonCode = record.getReasonCodeLink();
            map.put("reason", reasonCode.getName() );
            map.put("description", record.getReasonDescription() );
        }
        else{
            map.put("reason", "" );
            map.put("description", "");
        }
        map.put("status", "00");
        return map;
    }


    @Override
    public List<Clients> fetchRecords(HttpServletRequest request) {
        return entityRepository.findAllByFlag( AppConstants.STATUS_ACTIVERECORD );
    }
}
