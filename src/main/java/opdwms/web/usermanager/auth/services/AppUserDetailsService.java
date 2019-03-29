package opdwms.web.usermanager.auth.services;

import opdwms.web.usermanager.auth.exceptions.InactiveAccountException;
import opdwms.web.usermanager.auth.exceptions.UserAccountStatusException;
import opdwms.web.usermanager.entities.Users;
import opdwms.web.usermanager.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * This class creates an instance of a custom spring security UserDetails
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Service("appUserDetailsService")
@Transactional
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserAttemptsService userAttemptsService;
    
    private static final Logger logger = LoggerFactory.getLogger(AppUserDetailsService.class);

    /**
     * Custom UserDetails
     *
     * @param email
     * @return
     * @throws UsernameNotFoundException
     * @throws UserAccountStatusException
     */
    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException, UserAccountStatusException {
        Optional<Users> oUser = userRepository.findByEmail( email );

        System.err.println(" oUser ->" +oUser.isPresent() );

        // If the user was not found, throw an exception
        if( !oUser.isPresent() ) throw new UsernameNotFoundException("The email '" + email + "' was not found");
        
        return buildUserForAuthentication( oUser.get() );
    }

    private User buildUserForAuthentication(Users user) {
        List<GrantedAuthority> roles = new ArrayList<>();

        /*Check if email has been verified: by default, user account is inactive
         */
        if (!user.isEmailVerified() || StringUtils.isEmpty( user.getPassword() )) {
            throw new InactiveAccountException("Sorry! Your account is inactive. Go to your email to activate your account");
        }

         /*Check if user account is locked; if yes, check if elapsed time has been exhausted*/
        boolean isAccountNonLocked = userAttemptsService.isAccountNonLocked( user.getEmail() );

        System.err.println(" isAccountNonLocked ->" + isAccountNonLocked);

        /*Return Spring Security User object*/
        return new User(user.getEmail(), user.getPassword(), user.getEnabled(), true, true, isAccountNonLocked, roles);
    }
    
    

}
