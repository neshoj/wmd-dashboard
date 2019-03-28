package atlas.web.schools.services;

import atlas.core.template.AppConstants;
import atlas.core.template.forms.AuditData;
import atlas.web.schools.SchoolServiceInterface;
import atlas.web.schools.entities.Schools;
import atlas.web.schools.forms.SchoolsForm;
import atlas.web.schools.repository.SchoolsRepository;
import atlas.web.configs.entities.AuditTrail;
import atlas.web.configs.entities.ReasonCodes;
import atlas.web.configs.repository.AuditTrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class SchoolsService implements SchoolServiceInterface {

    private SchoolsForm entityForm;
    private SchoolsRepository entityRepository;
    private AuditTrailRepository auditTrailRepository;

    @Autowired
    public SchoolsService(SchoolsRepository entityRepository,
                          SchoolsForm entityForm,
                          AuditTrailRepository auditTrailRepository) {
        this.entityRepository = entityRepository;
        this.entityForm = entityForm;
        this.auditTrailRepository = auditTrailRepository;
    }

    @Override
    public Map<String, Object> saveRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Long userId = (Long) request.getSession().getAttribute("_userNo");
        Schools entity = entityForm.handleRequests(request);

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
                .setActivity("Created a new bank institution : " + entity.getName())
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
        Schools record = entityForm.getEntity();

        if (entityRepository.findByNameAndIdNot(record.getName(), record.getId()).isPresent()) {
            //Generate log
            log
                    .setLogType(AuditTrail.USER_GENERATED)
                    .setActivity("Attempt to update bank details failed: new name already exists")
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
                        .setActivity(String.format("Edited bank - %s : %s", record.getName(), auditData.getDescription()))
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
                        .setActivity(String.format("Attempt to update bank - %s : no changes made", record.getName()))
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

        Schools record = entityRepository.findById(index).get();
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
        Schools record = entityRepository.findById( index ).get();


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
        Schools record = entityForm.deactivateRequest( request );
        record
                .setFlag( AppConstants.STATUS_DEACTIVATED )
                .updatedOn( userNo );
        entityRepository.save( record );

        AuditTrail log = new AuditTrail()
                .setActivity(String.format("Deactivated a bank successfully. Reference %s", record.getName() ) )
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

        Schools record = entityRepository.findById( index ).get();
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
    public List<Schools> fetchRecords(HttpServletRequest request) {
        return entityRepository.findAllByFlag( AppConstants.STATUS_ACTIVERECORD );
    }
}
