package atlas.web.usermanager.services;

import atlas.core.template.AppConstants;
import atlas.core.template.forms.AuditData;
import atlas.web.configs.entities.AuditTrail;
import atlas.web.configs.entities.ReasonCodes;
import atlas.web.configs.repository.AuditTrailRepository;
import atlas.web.usermanager.UserGroupServiceInterface;
import atlas.web.usermanager.entities.*;
import atlas.web.usermanager.forms.UserGroupForm;
import atlas.web.usermanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Ignatius
 * @version 1.0.0
 * @category User Manager
 * @package Dev
 * @since Nov 05, 2018
 */
@Service
@Transactional
public class UserGroupService implements UserGroupServiceInterface {

    private UserGroupRepository entityRepository;
    private UserGroupForm entityForm;
    private SchoolGroupsRepository bankGroupsRepository;
    private PermissionsRepository permissionsRepository;
    private AppRolesRepository appRolesRepository;
    private AuditTrailRepository auditTrailRepository;


    @Autowired
    public UserGroupService(UserGroupRepository entityRepository,
                            UserGroupForm entityForm,
                            SchoolGroupsRepository bankGroupsRepository,
                            PermissionsRepository permissionsRepository,
                            AppRolesRepository appRolesRepository,
                            AuditTrailRepository auditTrailRepository) {
        this.entityRepository = entityRepository;
        this.entityForm = entityForm;
        this.bankGroupsRepository = bankGroupsRepository;
        this.permissionsRepository = permissionsRepository;
        this.appRolesRepository = appRolesRepository;
        this.auditTrailRepository = auditTrailRepository;

    }

    /**
     * Fetch a list of permissions
     *
     * @param request
     * @return List<Permissions>
     */
    @Override
    public List<Object> fetchPermissions(HttpServletRequest request) {
//        List<Permissions> permissions = new ArrayList<>();
//        String parentType = (String)request.getSession().getAttribute("_userParentType");
//
//        if( StringUtils.isEmpty( parentType ) ) {
//            Iterable<Permissions> permissionsSet = permissionsRepository.findAll();
//            permissionsSet.forEach(permissions::add);
//        }
//
//        //When serving merchants
//        else if ( UserTypes.MERCHANT_PARENT.equals( parentType ) ){
//            permissions.addAll( permissionsRepository.fetchByRole( parentType ));
//        }


        List<Object> permissionRolesList = new ArrayList<>();
        Iterator<AppRoles> rolesSet = appRolesRepository.findAll().iterator();

        while (rolesSet.hasNext()) {
            AppRoles role = rolesSet.next();

            List<Object> permissionsList = new ArrayList<>();
            Set<Permissions> permissionSet = role.getPermissions();

            for (Permissions permission : permissionSet) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", permission.getId());
                map.put("name", permission.getName());

                permissionsList.add(map);
            }

            Map<String, Object> map = new HashMap<>();
            map.put("role", role.getName());
            map.put("function", role.getAppFunction());
            map.put("permissions", permissionsList);
            permissionRolesList.add(map);
        }

        return permissionRolesList;
    }

    /**
     * Fetch  a list of records given their status
     *
     * @return List<UserGroups>
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<UserGroups> fetchAllRecords(HttpServletRequest request) {
        List<UserGroups> groups = new ArrayList<>();
        String parentType = (String) request.getSession().getAttribute("_userParentType");
        Long parentNo = (Long) request.getSession().getAttribute("_userParentNo");

        //When serving super-admin
        if (ObjectUtils.isEmpty(parentType)) {
            groups = entityRepository.findAllByFlag(AppConstants.STATUS_ACTIVERECORD);
        }
        //When server non-super-admins
        else {
            List<String> codes = new ArrayList<>();
            //When serving aea admin
            if (parentType.equals(UserTypes.AEA_ADMIN) || parentType.equals(UserTypes.KENHA_ADMIN)) {
                groups = entityRepository.fetchClientGroupRecords(parentNo);
            }
        }

        return groups;
    }

    /**
     * Persist a new record
     *
     * @param request
     * @return Map<String               ,                               Object>
     */
    @Override
    public Map<String, Object> saveRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        String parentType = (String) request.getSession().getAttribute("_userParentType");
        Long parentNo = (Long) request.getSession().getAttribute("_userParentNo");
        Long userNo = (Long) request.getSession().getAttribute("_userNo");

        AuditTrail log = new AuditTrail();
        UserGroups entity = this.entityForm.handleRequests(request);

        List<UserGroups> foundRecords = entityRepository.findByName(entity.getName().trim());
        boolean isUnique = foundRecords.size() < 1;
        if (!isUnique) {
            log
                    .setLogType(AuditTrail.USER_GENERATED)
                    .setActivity(String.format("Attempt to create a new User Group failed : %s. Record with similar details exists.", entity.getName()))
                    .setNewValues("N/A")
                    .setOldValues("N/A")
                    .setStatus("Success")
                    .setUserNo(userNo);

            map.put("status", "01");
            map.put("message", "A record with similar details exists");
        } else {

            String[] indices = request.getParameterValues("roles");
            Set<Permissions> permissions = new HashSet<>();
            for (String index : indices) {
                Optional<Permissions> permissionById = permissionsRepository.findById(Long.valueOf(index));
                if (permissionById.isPresent()) {
                    Permissions permission = permissionById.get();
                    permissions.add(permission);
                }
            }

            entity
                    .setFlag(AppConstants.STATUS_NEWRECORD)
                    .setPermissions(permissions)
                    .createdOn(userNo);

            /*Save record*/
            entityRepository.save(entity);

            // Set the owner of the group
            if (!StringUtils.isEmpty(parentType) && null != parentNo) {

                if (parentType.equals(UserTypes.AEA_ADMIN) || parentType.equals(UserTypes.KENHA_ADMIN)) {
                    ClientsGroups child = new ClientsGroups()
                            .setClientNo(parentNo)
                            .setId(entity.getId());
                    bankGroupsRepository.save(child);
                }
            }

            //Populate log
            log
                    .setLogType(AuditTrail.USER_GENERATED)
                    .setActivity("Created a new Usergroup : " + entity.getName())
                    .setNewValues(entity.getName())
                    .setOldValues("N/A")
                    .setStatus("Success")
                    .setUserNo(userNo);


            map.put("status", "00");
            map.put("message", "Request processed successfully");
        }

        auditTrailRepository.save(log);
        return map;
    }

    /**
     * Edit a record
     *
     * @param request
     * @return Map<String               ,                               Object>
     */
    @Override
    public Map<String, Object> editRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        AuditTrail log = new AuditTrail().setLogType(AuditTrail.USER_GENERATED).setUserNo(userNo);

        //Always update permissions
        boolean entityIsModified = this.entityForm.handleEditRequest(request);
        UserGroups record = this.entityForm.getEntity();
        boolean isModified = false;

        String[] indices = request.getParameterValues("roles");

        List<Long> pks = new ArrayList<>();
        for (String index : indices) {
            pks.add(Long.valueOf(index));
        }

        Set<Permissions> newPermissions = permissionsRepository.fetchByIds(pks);
        Set<Permissions> oldPermissions = record.getPermissions();

        //Check if permissions were updated
        String changeActivity = PermSetIsModified(oldPermissions, newPermissions);
        if (!changeActivity.isEmpty()) {
            isModified = true;
            record.setPermissions(newPermissions);
        }

        //Check if the record was updated
        if (entityIsModified) {
            isModified = true;
            record.setFlag(AppConstants.STATUS_EDITEDRECORD).updatedOn(userNo);
        }

        //When there were changes in the record
        if (isModified) {

            if (entityIsModified) {
                map.put("status", "00");
                map.put("message", "Request processed successfully");
            } else if (!changeActivity.isEmpty()) {
                map.put("status", "00");
                map.put("message", "Permissions updated successfully");
            }

            /*Persist record*/
            entityRepository.save(record);
            AuditData auditData = entityForm.auditData();
            log
                    .setActivity("Edited usergroup :" + record.getName() + " record - " + auditData.getDescription() + " " + changeActivity)
                    .setNewValues(auditData.getNewValue())
                    .setOldValues(auditData.getOldValue())
                    .setStatus("Success")
            ;
        }

        //When there are no changes
        else {
            log
                    .setActivity("Attempt to update usergroup :" + record.getName() + " record: no changes made")
                    .setNewValues("N/A")
                    .setOldValues("N/A")
                    .setStatus("Failed");

            map.put("status", "01");
            map.put("message", "No changes were made to this record");
        }

        auditTrailRepository.save(log);
        return map;
    }

    /**
     * Approve edit changes
     *
     * @param request
     * @return Map<String               ,                               Object>
     */
    @Override
    public Map<String, Object> approveEditChanges(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Long index = Long.valueOf(request.getParameter("index"));
        String action = request.getParameter("action");
        Long userNo = (Long) request.getSession().getAttribute("_userNo");

        UserGroups record = this.entityRepository.findById(index).get();
        boolean proceed = entityForm.applyMakerChecker(record, action);

        map.put("message", entityForm.getResponse());
        if (proceed) {
            record = entityForm.getEntity();
            entityRepository.save(record);
            map.put("status", "00");
        } else {
            map.put("status", "01");
        }

        //Insert logs
        AuditTrail log = entityForm.getLog().setUserNo(userNo);
        String activity = String.format("%s Reference: %s", log.getActivity(), record.getName());
        log.setActivity(activity);
        auditTrailRepository.save(log);
        return map;
    }

    /**
     * Fetch a record information
     *
     * @param request
     * @return Map<String               ,                               Object>
     */
    @Override
    public Map<String, Object> fetchRecord(HttpServletRequest request) {
        String index = request.getParameter("index");
        Map<String, Object> map = this.entityForm.transformEntity(index);
        List<Long> permissions = new ArrayList<>();
        for (Permissions row : (this.entityForm.getEntity()).getPermissions()) {
            permissions.add(row.getId());
        }
        map.put("permissions", permissions);
        return map;
    }

    /**
     * Fetch edit changes
     *
     * @param request
     * @return Map<String               ,                               Object>
     */
    @Override
    public Map<String, Object> fetchRecordChanges(HttpServletRequest request) {
        String index = request.getParameter("index");
        return this.entityForm.fetchChanges(index);
    }

    /**
     * Update record status
     *
     * @param request
     * @return Map<String       ,               Object>
     */
    @Override
    public Map<String, Object> flagRecords(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();

        String action = request.getParameter("action");
        Long index = Long.valueOf(request.getParameter("index"));
        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        UserGroups record = this.entityRepository.findById(index).get();

        boolean success = entityForm.applyMakerChecker(record, action);
        map.put("message", entityForm.getResponse());

        if (success) {
            record = entityForm.getEntity();
            entityRepository.save(record);

            map.put("status", "00");
        } else {
            map.put("status", "01");
        }

        //Insert logs
        AuditTrail log = entityForm.getLog().setUserNo(userNo);
        String logActivity = String.format("%s Reference: %s", log.getActivity(), record.getName());
        log.setActivity(logActivity);
        auditTrailRepository.save(log);
        return map;
    }

    /**
     * Deactivate a record
     *
     * @param request
     * @return Map<String       ,               Object>
     */
    @Override
    public Map<String, Object> deactivateRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        UserGroups record = this.entityForm.deactivateRequest(request);
        record
                .setFlag(AppConstants.STATUS_DEACTIVATED)
                .updatedOn(userNo);
        entityRepository.save(record);

        AuditTrail log = new AuditTrail()
                .setActivity(String.format("Deactivated a user group successfully. Reference %s", record.getName()))
                .setStatus("Success")
                .setOldValues("Active").setNewValues("Deactivated")
                .setUserNo(userNo);
        auditTrailRepository.save(log);

        map.put("status", "00");
        map.put("message", "Request processed successfully");

        return map;
    }

    /**
     * Fetch deactivation details
     *
     * @param request
     * @return Map<String       ,               Object>
     */
    @Override
    public Map<String, Object> fetchDeactivationInfo(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Long index = Long.valueOf(request.getParameter("index"));

        UserGroups record = this.entityRepository.findById(index).get();
        map.put("index", record.getId());
        map.put("editor", record.getUpdatedByLink().getFullNames());

        if (null != record.getReasonCodeLink()) {
            ReasonCodes reasonCode = record.getReasonCodeLink();
            map.put("reason", reasonCode.getName());
            map.put("description", record.getReasonDescription());
        } else {
            map.put("reason", "");
            map.put("description", "");
        }
        map.put("status", "00");
        return map;
    }


    /**
     * Checks if permissions of the record in request have been modified
     *
     * @param oldPermissions Permissions in permanent storage when this request was made
     * @param newPermissions Permissions submitted in the user request
     * @return <code> Empty String: When no changes were made; else generated log message </code>
     */
    private static String PermSetIsModified(Set<Permissions> oldPermissions, Set<Permissions> newPermissions) {
        Set<Permissions> oldSet = new HashSet<>();
        Set<Permissions> newSet = new HashSet<>();

        oldSet.addAll(oldPermissions);
        newSet.addAll(newPermissions);

        String changeActivity = "";
        if ((oldSet.isEmpty() && newSet.isEmpty()) && (oldSet.containsAll(newSet))) {
            changeActivity = "";
        }

        //Save a temporary copy of the new set to use it later
        final Set<Permissions> newSetCopy = new HashSet<>(newSet);

        //Retrieve added permissions
        newSet.removeAll(oldSet);
        Set<Permissions> addedSet = newSet;

        //Retrieve 'deleted' permissions
        oldSet.removeAll(newSetCopy);
        Set<Permissions> deletedSet = oldSet;

        //Generate log message for added permissions
        if (!addedSet.isEmpty()) {
            changeActivity = "Permissions added: ";

            for (Permissions node : addedSet) {
                changeActivity += node.getName() + ", ";
            }
        }

        //Generate log message for 'deleted' permissions
        if (!deletedSet.isEmpty()) {
            if (!changeActivity.isEmpty()) changeActivity += "; Permissions removed: ";
            else changeActivity = "Permissions removed: ";

            for (Permissions node : deletedSet) {
                changeActivity += node.getName() + ", ";
            }
        }

        //Return consumable log response
        return changeActivity;
    }
}
