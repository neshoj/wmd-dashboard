package opdwms.web.usermanager.services;

import opdwms.core.mail.MailerService;
import opdwms.core.template.AppConstants;
import opdwms.core.template.forms.AuditData;
import opdwms.web.configs.entities.AuditTrail;
import opdwms.web.configs.entities.ReasonCodes;
import opdwms.web.configs.repository.AuditTrailRepository;
import opdwms.web.configs.services.AuditService;
import opdwms.web.usermanager.UserServiceInterface;
import opdwms.web.usermanager.auth.SecurityUtils;
import opdwms.web.usermanager.entities.*;
import opdwms.web.usermanager.forms.UsersForm;
import opdwms.web.usermanager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


/**
 * @author Ignatius
 * @version 1.0.0
 * @category User Manager
 * @package Dev
 * @since Nov 05, 2018
 */
@Transactional
@Service
public class UserService implements UserServiceInterface {

    private UserRepository userRepository;
    private AuditService auditLogService;
    private AuditTrailRepository auditTrailRepository;
    private UserTypesRepository userTypesRepository;
    private UserAttemptsRepository userAttemptsRepository;
    private SchoolsUsersRepository schoolsUsersRepository;
    private UsersForm usersForm;
    private MailerService mailService;

    @Autowired
    public UserService(
            UserRepository userRepository,
            AuditService auditLogService,
            AuditTrailRepository auditTrailRepository,
            UserTypesRepository userTypesRepository,
            UserAttemptsRepository userAttemptsRepository,
            SchoolsUsersRepository schoolsUsersRepository,
            UsersForm usersForm,
            MailerService mailService) {
        this.userRepository = userRepository;
        this.auditLogService = auditLogService;
        this.auditTrailRepository = auditTrailRepository;
        this.userTypesRepository = userTypesRepository;
        this.userAttemptsRepository = userAttemptsRepository;
        this.schoolsUsersRepository = schoolsUsersRepository;
        this.usersForm = usersForm;
        this.mailService = mailService;
    }

    @Value("${app.endpoint}")
    private String baseURL;

    /**
     * Validates email and sends a reset password link to the email in question
     *
     * @param email
     * @return
     */
    @Override
    public Map<String, Object> generateResetToken(String email) {
        Map<String, Object> map = new HashMap<>();
        Optional<Users> oUser = userRepository.findByEmail(email);
        if (oUser.isPresent()) {

            //Generate a random unique token
            String token = UUID.randomUUID().toString();

            //Update user record
            Users user = oUser.get();
            user
                    .setEmailToken(token).setResetReqDate(new Date(System.currentTimeMillis()));
            userRepository.save(user);
            auditLogService.logActivity("Password reset request processed successfully.", email, "Success");

            map.put("status", "ok");
            map.put("names", String.format("%s", user.getSurname()));
            map.put("token", token);
            map.put("email", email);

            AuditTrail log = new AuditTrail();
            log
                    .setLogType(AuditTrail.USER_GENERATED)
                    .setActivity("Generated password reset token")
                    .setStatus("Success").setUserNo(user.getId());
            auditTrailRepository.save(log);
        } else {
            map.put("status", "not-found");
        }

        return map;
    }

    /**
     * Allows a user to reset their password
     *
     * @param names Client full names
     * @param email Client email address
     * @param token Password reset token
     * @return Boolean Results of processing
     */
    @Override
    public boolean sendPasswordToken(String names, String email, String token) {
        try {
            StringBuilder emailLink = new StringBuilder();
            emailLink.append(baseURL).append("/password-reset/").append(token);

            mailService.sendMail(mailService.sendGridConfig()
                    .setTemplateId("d-7700d43e395e4f2f9e69d1997ad4d340")
                    .setTo(email, names)
                    .setSubject("Password Reset")
                    .addAttribute("_lastname", names)
                    .addAttribute("_baseUrl", emailLink.toString())
            );

        } catch (Exception e) {
            //logger.error("Error while sending mail for password reset :", e);
            return false;
        }
        return true;
    }

    /**
     * Validates the secure token send to the user
     *
     * @param token
     * @return
     */
    @Override
    public String validateResetToken(final String token) {
        String result = "";
        Optional<Users> oUser = userRepository.findByEmailToken(token);

        if (oUser.isPresent()) {
            Users user = oUser.get();
            result = user.getEmail();
        } else {
            result = "invalid";
        }
        return result;

    }

    /**
     * Set up new account password | handle password change request
     *
     * @param token    Random secure code send to the user email
     * @param password New password for this account
     * @return String
     */
    @Override
    public Map<String, Object> setupNewPassword(String token, String password) {
        Map<String, Object> map = new HashMap<>();
        String status = "error";
        Optional<Users> oUser = userRepository.findByEmailToken(token);
        if (oUser.isPresent()) {
            Users user = oUser.get();
            user
                    .setEnabled(true)
                    .setEmailVerified(true)
                    .setPassword(SecurityUtils.hashPassword(password))
                    .setResetReqDate(null)
                    .setEmailToken(null);
            userRepository.save(user);

            //Audit this action
            AuditTrail trail = new AuditTrail()
                    .setActivity("Password reset processed successfully.")
                    .setUserNo(user.getId())
                    .setStatus("Success");
            auditTrailRepository.save(trail);


            //Package email properties
            map.put("sendMail", true);
            map.put("email", user.getEmail());
            map.put("lastName", user.getSurname());
            map.put("names", user.getFullNames());

            status = "ok";
        } else status = "invalid";

        map.put("status", status);
        return map;
    }


    /**
     * Persist a new record
     *
     * @param request
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>
     */
    @Override
    public Map<String, Object> saveRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Long userId = (Long) request.getSession().getAttribute("_userNo");
        Users entity = this.usersForm.handleRequests(request);

        String parentType = (String) request.getSession().getAttribute("_userParentType");
        Long parentNo = (Long) request.getSession().getAttribute("_userParentNo");
        List<Users> existingUsers = userRepository.findAllByEmail(entity.getEmail());
        if (existingUsers.size() > 0) {
            map.put("status", "01");
            map.put("message", "Email is already being used by another user");
            return map;
        }

        entity
                .createdOn(userId)
                .setFlag(AppConstants.STATUS_NEWRECORD);
        /*Save record*/
        userRepository.save(entity);

        //When a super-admin is creating this record
        if (null == parentType) {
            UserTypes userTypeNode = userTypesRepository.findById(entity.getUserTypeNo()).get();
            String code = userTypeNode.getCode();

            if (code.equals(UserTypes.AEA_ADMIN) ||
                    code.equals(UserTypes.AEA_OPERATIONS_MANAGER) ||
                    code.equals(UserTypes.AEA_WEIGHBRIDGE_MANAGER)
            ) {
                ClientsUsers child = new ClientsUsers();
                String merchantNo = request.getParameter("clientNo");
                child.setId(entity.getId()).setClientNo(Long.valueOf(merchantNo));
                schoolsUsersRepository.save(child);
            }
        } else {
            //When a AEA or KENHA is creating this record
            if (UserTypes.AEA_ADMIN.equals(parentType) || UserTypes.KENHA_ADMIN.equals(parentType)) {
                ClientsUsers child = new ClientsUsers();
                child.setId(entity.getId()).setClientNo(Long.valueOf(parentNo));
                schoolsUsersRepository.save(child);
            }
        }

        AuditTrail log = new AuditTrail();
        log
                .setLogType(AuditTrail.USER_GENERATED)
                .setActivity("Created a new user : " + entity.getFullNames())
                .setNewValues(entity.getFullNames())
                .setOldValues("N/A")
                .setStatus("Success")
                .setUserNo(userId);

        auditTrailRepository.save(log);

        map.put("status", "00");
        map.put("message", "Request processed successfully");
        return map;
    }


    /**
     * Edit a record
     *
     * @param request
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>
     */
    @Override
    public Map<String, Object> editRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        AuditTrail log = new AuditTrail();
        Long userNo = (Long) request.getSession().getAttribute("_userNo");

        boolean isModified = this.usersForm.handleEditRequest(request);
        Users record = this.usersForm.getEntity();

        //If record has changes
        if (isModified) {

            record
                    .setFlag(AppConstants.STATUS_EDITEDRECORD)
                    .updatedOn(userNo);
            //Persist record
            userRepository.save(record);

            //Generate log
            AuditData auditData = usersForm.auditData();
            log
                    .setLogType(AuditTrail.USER_GENERATED)
                    .setActivity(String.format("Edited user - %s : %s", record.getFullNames(), auditData.getDescription()))
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
                    .setActivity(String.format("Attempt to update user - %s : no changes made", record.getFullNames()))
                    .setNewValues("N/A")
                    .setOldValues("N/A")
                    .setStatus("Failed")
                    .setUserNo(userNo);

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
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>
     */
    @Override
    public Map<String, Object> approveEditChanges(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        Long index = Long.valueOf(request.getParameter("index"));
        String action = request.getParameter("action");

        Users record = this.userRepository.findById(index).get();
        boolean proceed = usersForm.applyMakerChecker(record, action);

        map.put("message", usersForm.getResponse());
        if (proceed) {
            record = usersForm.getEntity();
            this.userRepository.save(record);
            map.put("status", "00");
        } else {
            map.put("status", "01");
        }

        //Insert logs
        AuditTrail log = usersForm.getLog().setUserNo(userNo);
        String activity = String.format("%s Reference: %s", log.getActivity(), record.getFullNames());
        log.setActivity(activity);
        auditTrailRepository.save(log);
        return map;
    }

    /**
     * Fetch a record information
     *
     * @param request
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>
     */
    @Override
    public Map<String, Object> fetchRecord(HttpServletRequest request) {
        String index = request.getParameter("index");
        return this.usersForm.transformEntity(index);
    }

    /**
     * Fetch edit changes
     *
     * @param request
     * @return Map<String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>
     */
    @Override
    public Map<String, Object> fetchRecordChanges(HttpServletRequest request) {
        String index = request.getParameter("index");
        return this.usersForm.fetchChanges(index);
    }

    /**
     * Update record status
     *
     * @param request
     * @return Map<String                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>
     */
    @Override
    public Map<String, Object> flagRecords(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        StringBuilder activity = new StringBuilder();

        String action = request.getParameter("action");
        Long index = Long.valueOf(request.getParameter("index"));
        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        Users record = this.userRepository.findById(index).get();

        boolean success = usersForm.applyMakerChecker(record, action);
        map.put("message", usersForm.getResponse());

        if (success) {
            record = usersForm.getEntity();

            if (action.equals(AppConstants.ACTION_APRROVE_NEW)) {
                activity.append("Record approved successfully");
                /*Set up the secure token*/
                String token = UUID.randomUUID().toString();
                record.setEmailToken(token);

                //Package email properties
                map.put("sendMail", true);
                map.put("token", token);
                map.put("email", record.getEmail());
                map.put("lastName", record.getSurname());
                map.put("names", record.getFullNames());
            }

            userRepository.save(record);
            map.put("status", "00");
        } else {
            map.put("status", "01");
        }

        //Insert logs
        AuditTrail log = usersForm.getLog().setUserNo(userNo);
        String logActivity = String.format("%s Reference: %s", log.getActivity(), record.getFullNames());
        log.setActivity(logActivity);
        auditTrailRepository.save(log);
        return map;
    }

    /**
     * Deactivate a record
     *
     * @param request
     * @return Map<String                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>
     */
    @Override
    public Map<String, Object> deactivateRecord(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        Users record = this.usersForm.deactivateRequest(request);
        record
                .setFlag(AppConstants.STATUS_DEACTIVATED)
                .updatedOn(userNo);
        userRepository.save(record);

        AuditTrail log = new AuditTrail()
                .setActivity(String.format("Deactivated a user successfully. Reference %s", record.getFullNames()))
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
     * @return Map<String                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>
     */
    @Override
    public Map<String, Object> fetchDeactivationInfo(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Long index = Long.valueOf(request.getParameter("index"));

        Users record = this.userRepository.findById(index).get();
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
     * Reset user attempts
     *
     * @param request
     * @return Map<String                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>
     */
    @Override
    public Map<String, Object> unlockUser(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<>();
        Long userNo = (Long) request.getSession().getAttribute("_userNo");

        Long index = Long.valueOf(request.getParameter("index"));
        Users user = this.userRepository.findById(index).get();

        AuditTrail log = new AuditTrail().setUserNo(userNo);
        List<UserAttempts> attempts = userAttemptsRepository.findByEmail(user.getEmail());
        if (attempts.size() > 0) {
            /*Update number of retries*/
            UserAttempts userAttempt = attempts.get(0);
            userAttempt.setAttempts(0L);
            userAttempt.setLastmodified(null);
            userAttemptsRepository.save(userAttempt);

            user.setNonlocked(true);
            userRepository.save(user);

            log
                    .setActivity(String.format("Unlocked user account successfully. Reference %s", user.getFullNames()))
                    .setStatus("Success")
                    .setOldValues("Locked").setNewValues("Active");
        } else {
            log
                    .setActivity(String.format("Attempt to unlocked user account failed. Reference %s", user.getFullNames()))
                    .setStatus("Failed")
                    .setOldValues("N/A").setNewValues("N/A");
        }

        auditTrailRepository.save(log);

        map.put("status", "00");
        map.put("message", "Request processed successfully");
        return map;
    }


}
