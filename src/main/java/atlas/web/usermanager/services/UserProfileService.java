package atlas.web.usermanager.services;

import atlas.web.configs.entities.AuditTrail;
import atlas.web.configs.repository.AuditTrailRepository;
import atlas.web.usermanager.UserProfileServiceInterface;
import atlas.web.usermanager.auth.SecurityUtils;
import atlas.web.usermanager.entities.UserTypes;
import atlas.web.usermanager.entities.Users;
import atlas.web.usermanager.forms.UsersForm;
import atlas.web.usermanager.repository.UserRepository;
import atlas.core.aws.AmazonS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


/**
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Service
@Transactional
public class UserProfileService implements UserProfileServiceInterface {

    @Autowired
    private UsersForm usersForm;
    @Autowired
    private AuditTrailRepository trailRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AmazonS3Service amazonS3Service;


    /**
     * Fetch the user details associated with the user in question
     *
     * @param   userId
     * @return  Map<String, Object>
     */
    @Override
    public Map<String, Object> fetchUserDetails(Long userId) {
        Optional<Users> Ouser = userRepository.findById( userId );
        if ( !Ouser.isPresent() ) return null;

        Users user = Ouser.get();
        Map<String, Object> map = usersForm.transformEntity( user );

        //Fetch user type
        if( null != user.getUserTypeLink() ){
            UserTypes userType = user.getUserTypeLink();
            String parent = "" ;

            switch ( userType.getCode() ){
                default:
                    parent = "Binary Limited";
                    break;
            }

            map.put("parent", parent);
            map.put("userType", userType.getName() );
        }

        // Return the result
        return map;
    }

    /**
     * Update profile details
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> updateProfile(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        Long userNo = (Long)request.getSession().getAttribute("_userNo");
        Users user = userRepository.findById( userNo ).get();

        //Retrieve changes
        AuditTrail log = fetchProfileChanges( new AuditTrail(), request, user)
                .setLogType( AuditTrail.USER_GENERATED).setUserNo(user.getId());

        if( ObjectUtils.isEmpty( log.getActivity()) ){
            log.setActivity("Attempt to updated profile details failed: no changes were made.");
            map.put("status", "error");
            map.put("message", "No changes were made.");
        }
        else{
            map.put("status", "msg");
            map.put("message", "Profile updated successfully.");
        }

        //Save log
        trailRepository.save( log );

        //Update user record
        userRepository.save( user );

        return map;
    }

    /**
     * Update profile image
     *
     * @param request
     * @return Map<String, Object>
     */
    public Map<String, Object> updatePhoto(HttpServletRequest request){

        String message = "";
        String status = "";
        String url = "";
        Map<String, Object> map = new HashMap<>();
        Long userNo = (Long) request.getSession().getAttribute("_userNo");

        Users user = userRepository.findById( userNo ).get();
        AuditTrail log = new AuditTrail().setUserNo(user.getId());

        try {

            boolean isPhotoNew = false;

            //Upload profile image
            if (!StringUtils.isEmpty(request.getParameter("avatar"))) {
                MultipartRequest fileRequest = (MultipartRequest) request;
                MultipartFile multipartFile = fileRequest.getFile("file");

                String fileKeyName = String.format("profile-%s", UUID.randomUUID().toString());

                if (null != user.getPhotoKey()) amazonS3Service.deleteFile(user.getPhotoKey());
                url = amazonS3Service.uploadMultipart(multipartFile, fileKeyName);

                user.setPhotoKey(fileKeyName).setPhotoUrl(url);
                isPhotoNew = true;

                //Update record
                userRepository.save(user);
            } else url = user.getPhotoUrl();

            if ( isPhotoNew ) {
                status = "msg";
                message = "Profile picture uploaded successfully.";
                log.setStatus( "Success" );
            } else {
                status = "error";
                message = "Attempt to edit profile details: no changes made";
                log.setStatus( "Failed" );
            }

        }
        catch ( Exception e){
            e.printStackTrace();
            status = "error";
            message = "Internal server error: try again later";

            log.setStatus( "Failed" );
            log.setLogType( AuditTrail.SYSTEM_ERROR );
        }

        log.setActivity(message);
        //Persist record
        trailRepository.save(log);

        map.put("status", status);
        map.put("message", message);
        map.put("url", url);

        return map;
    }

    /**
     * Allow a user to change their password
     *
     * @param request
     * @return Map<String, Object>
     */
    @Override
    public Map<String, Object> changePassword(HttpServletRequest request){
        String message = "";
        String status = "";
        Map<String, Object> map = new HashMap<>();
        Long userNo = (Long) request.getSession().getAttribute("_userNo");
        Users user = userRepository.findById( userNo ).get();
        AuditTrail log = new AuditTrail().setUserNo(user.getId());

        String currentPassword = request.getParameter("currentPassword");
        String password = request.getParameter("password");

        if( !SecurityUtils.checkPasswords( currentPassword, user.getPassword())){
            status = "error";
            message = "Your current password doesn't match the one supplied.";
        }
        else {

            //Update record
            user.setPassword(SecurityUtils.hashPassword(password));
            userRepository.save(user);

            status = "msg";
            message = "Your password has been changed successfully.";
        }

        log.setActivity( message ).setStatus( "msg".equals( status) ? "Success": "Failed" );
        trailRepository.save( log );

        map.put("status", status);
        map.put("message", message);
        return map;
    }

    /**
     * Retrieve changes made to one's profile
     * @param log
     * @param request
     * @param entity
     * @return AppAuditLog
     */
    private AuditTrail fetchProfileChanges(AuditTrail log, HttpServletRequest request, Users entity){
        StringBuilder keys = new StringBuilder();
        StringBuilder oldValues = new StringBuilder();
        StringBuilder newValues = new StringBuilder();

        String firstName = request.getParameter("firstName");
        String middleName = request.getParameter("middleName");
        String surname = request.getParameter("surname");

        if( !ObjectUtils.nullSafeEquals( firstName, entity.getFirstName() )){
            oldValues.append( entity.getFirstName() );
            newValues.append( firstName );
            keys.append( "First Name");
            entity.setFirstName( firstName );
        }

        if( !ObjectUtils.nullSafeEquals( surname, entity.getSurname() )){
            oldValues.append( entity.getSurname() );
            newValues.append( surname );
            keys.append( "Surname");
            entity.setSurname( surname );
        }

        if( keys.length() > 0 ) {
            log
                    .setActivity("Edited user profile details: " + keys.toString())
                    .setNewValues(newValues.toString())
                    .setOldValues(oldValues.toString());
        }

        return log;
    }
}
