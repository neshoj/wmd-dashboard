package opdwms.web.policeofficers.services;

import opdwms.core.template.AppConstants;
import opdwms.core.template.forms.AuditData;
import opdwms.web.configs.entities.AuditTrail;
import opdwms.web.configs.entities.ReasonCodes;
import opdwms.web.configs.repository.AuditTrailRepository;
import opdwms.web.policeofficers.PoliceOfficerServiceInterface;
import opdwms.web.policeofficers.entities.PoliceOfficers;
import opdwms.web.policeofficers.forms.PoliceOfficersForm;
import opdwms.web.policeofficers.repository.PoliceOfficerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PoliceOfficerService implements PoliceOfficerServiceInterface {


    private PoliceOfficersForm entityForm;
    private PoliceOfficerRepository entityRepository;
    private AuditTrailRepository auditTrailRepository;

    @Autowired
    public PoliceOfficerService(PoliceOfficersForm entityForm,
                                PoliceOfficerRepository entityRepository,
                                AuditTrailRepository auditTrailRepository){
        this.entityForm = entityForm;
        this.entityRepository = entityRepository;
        this.auditTrailRepository = auditTrailRepository;
    }

    @Override
    public List<PoliceOfficers> fetchRecords(HttpServletRequest request) {
        return entityRepository.findAllByFlag(AppConstants.STATUS_ACTIVERECORD );
    }

    /**
     * Persist a new record
     *
     * @param request
     * @return  Map<String, Object>
     */
    @Override
    public Map<String, Object> saveRecord(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        Long userId = (Long)request.getSession().getAttribute("_userNo");
        Long parentNo = (Long)request.getSession().getAttribute("_userParentNo");
        String parentType = (String)request.getSession().getAttribute("_userParentType");
        PoliceOfficers entity = entityForm.handleRequests( request );
        AuditTrail log = new AuditTrail();

        //Merchant access
        boolean isDuplicate = false;

            if (entityRepository.findByPoliceNo(entity.getPoliceNo().trim()).isPresent()) {
                isDuplicate = true;
            }

        //When we have duplicates
        if( isDuplicate ){
            log
                    .setLogType(AuditTrail.USER_GENERATED)
                    .setActivity("Attempt to create a new police officer account failed: record already exists. Reference: " + entity.getPoliceNo())
                    .setNewValues(entity.getPoliceNo())
                    .setOldValues("N/A")
                    .setStatus("N/A")
                    .setUserNo(userId);

            map.put("status", "01");
            map.put("message", "Record already exists.");
        }

        //Process record
        else {

            entity
                    .createdOn(userId)
                    .setFlag(AppConstants.STATUS_NEWRECORD);

            /*Save record*/
            entityRepository.save(entity);

            log
                    .setLogType(AuditTrail.USER_GENERATED)
                    .setActivity("Created a new police officer account : " + entity.getPoliceNo())
                    .setNewValues(entity.getPoliceNo())
                    .setOldValues("N/A")
                    .setStatus("Success")
                    .setUserNo(userId);

            map.put("status", "00");
            map.put("message", "Request processed successfully");
        }

        //Save audit log
        auditTrailRepository.save( log );
        return map;
    }


    /**
     * Edit a record
     *
     * @param request
     * @return  Map<String, Object>
     */
    @Override
    public Map<String, Object> editRecord(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        AuditTrail log = new AuditTrail();
        Long userNo = (Long) request.getSession().getAttribute("1");

        boolean isModified = entityForm.handleEditRequest(request);
        PoliceOfficers record = entityForm.getEntity();

        //If record has changes
        if( isModified ){
            record
                    .setFlag( AppConstants.STATUS_EDITEDRECORD )
                    .updatedOn( userNo );
            //Persist record
            entityRepository.save( record );

            //Generate log
            AuditData auditData = entityForm.auditData();
            log
                    .setLogType( AuditTrail.USER_GENERATED )
                    .setActivity(String.format("Edited police officers  - %s : %s", record.getPoliceNo(), auditData.getDescription() ) )
                    .setNewValues( auditData.getNewValue() )
                    .setOldValues( auditData.getOldValue() )
                    .setStatus("Success")
                    .setUserNo( userNo );

            map.put("status", "00");
            map.put("message", "Request processed successfully");
        }
        //No changes were made
        else{
            //Generate log
            log
                    .setLogType( AuditTrail.USER_GENERATED )
                    .setActivity(String.format("Attempt to update police officer account - %s : no changes made", record.getPoliceNo())  )
                    .setNewValues( "N/A" )
                    .setOldValues("N/A" )
                    .setStatus("Failed")
                    .setUserNo( userNo );

            map.put("status", "01");
            map.put("message", "No changes were made to this record");
        }

        auditTrailRepository.save( log );
        return map;
    }

    /**
     * Approve edit changes
     *
     * @param request
     * @return  Map<String, Object>
     */
    @Override
    public Map<String, Object> approveEditChanges(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        Long index = Long.valueOf( request.getParameter("index") );
        String action = request.getParameter("action");

        PoliceOfficers record = entityRepository.findById( index ).get();
        boolean proceed = entityForm.applyMakerChecker(record, action);

        map.put("message", entityForm.getResponse());
        if ( proceed ) {
            record = entityForm.getEntity();
            entityRepository.save( record );
            map.put("status", "00");
        }
        else  map.put("status", "01");

        //Insert logs
        AuditTrail log = entityForm.getLog().setUserNo(userNo);
        String activity = String.format( "%s Reference: %s", log.getActivity(), record.getPoliceNo() );
        log.setActivity( activity );
        auditTrailRepository.save(log);
        return map;
    }

    /**
     * Fetch a record information
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> fetchRecord(HttpServletRequest request){
        String index = request.getParameter("index");
        return entityForm.transformEntity(index);
    }

    /**
     * Fetch edit changes
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> fetchRecordChanges(HttpServletRequest request){
        String index = request.getParameter("index");
        return entityForm.fetchChanges( index );
    }

    /**
     * Update record status
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> flagRecords( HttpServletRequest request ){
        Map<String, Object> map = new HashMap<>();

        String action = request.getParameter("action");
        Long index = Long.valueOf( request.getParameter("index") );
        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        PoliceOfficers record = entityRepository.findById( index ).get();

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
        String logActivity = String.format( "%s Reference: %s", log.getActivity(), record.getPoliceNo());
        log.setActivity( logActivity );
        auditTrailRepository.save(log);
        return map;
    }

    /**
     * Deactivate a record
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> deactivateRecord(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        PoliceOfficers record = entityForm.deactivateRequest( request );
        record
                .setFlag( AppConstants.STATUS_DEACTIVATED )
                .updatedOn( userNo );
        entityRepository.save( record );

        AuditTrail log = new AuditTrail()
                .setActivity(String.format("Deactivated a police officer account successfully. Reference %s", record.getPoliceNo() ) )
                .setStatus("Success")
                .setOldValues("Active").setNewValues("Deactivated")
                .setUserNo( userNo );
        auditTrailRepository.save( log );

        map.put("status", "00");
        map.put("message", "Request processed successfully");

        return map;
    }

    /**
     * Fetch deactivation details
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> fetchDeactivationInfo(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        Long index = Long.valueOf( request.getParameter("index") );

        PoliceOfficers record = entityRepository.findById( index ).get();
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
}
