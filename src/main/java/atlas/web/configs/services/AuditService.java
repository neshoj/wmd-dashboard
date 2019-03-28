package atlas.web.configs.services;

import atlas.web.configs.AuditServiceInterface;
import atlas.web.configs.entities.AuditTrail;
import atlas.web.usermanager.entities.Users;
import atlas.web.usermanager.repository.UserRepository;
import atlas.web.configs.repository.AuditTrailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class AuditService implements AuditServiceInterface {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuditTrailRepository auditTrailRepository;

    /**
     *  Save record given the current user's principal name
     *
     * @param trail
     * @param email
     */
    @Override
    public void saveLog(AuditTrail trail, String email){
        Optional<Users> oUser = userRepository.findByEmail( email );
        if( oUser.isPresent() ){
            Users currentUser = oUser.get();
            trail.setUserNo( currentUser.getId() );
        }
        auditTrailRepository.save( trail );
    }

    public void logActivity(String activity, String email, String status) {
        Optional<Users> oUser = userRepository.findByEmail( email );
        AuditTrail trail = new AuditTrail();
        if( oUser.isPresent() ){
            Users currentUser = oUser.get();
            trail.setUserNo( currentUser.getId() );
        }

        trail.setActivity( activity ).setStatus( status );

        auditTrailRepository.save( trail );
    }

}
