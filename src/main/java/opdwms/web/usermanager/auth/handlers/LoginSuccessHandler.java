package opdwms.web.usermanager.auth.handlers;

import opdwms.web.usermanager.entities.ClientsUsers;
import opdwms.web.usermanager.entities.UserTypes;
import opdwms.web.usermanager.entities.Users;
import opdwms.web.usermanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

/**
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Component
@Transactional
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException, ServletException {

        /*Set various session parameters*/
        HttpSession session = request.getSession();
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Users> oUser = userRepository.findByEmail( authUser.getUsername() );


        if( oUser.isPresent() ){
            Users user = oUser.get();

            switch( user.getUserTypeLink().getCode() )
            {
                case UserTypes.AEA_ADMIN:
                    session = setClientUsersParentNoAndParentType(session, user, UserTypes.AEA_ADMIN);
                    break;
                case UserTypes.AEA_OPERATIONS_MANAGER:
                    session = setClientUsersParentNoAndParentType(session, user, UserTypes.AEA_OPERATIONS_MANAGER);
                    break;
                case UserTypes.AEA_WEIGHBRIDGE_MANAGER:
                    session = setClientUsersParentNoAndParentType(session, user, UserTypes.AEA_WEIGHBRIDGE_MANAGER);
                    break;

                case UserTypes.KENHA_ADMIN:
                    session = setClientUsersParentNoAndParentType(session, user, UserTypes.KENHA_ADMIN);
                    break;
                case UserTypes.KENHA_AXLE_LOAD_CONTROL_OFFICER:
                    session = setClientUsersParentNoAndParentType(session, user, UserTypes.KENHA_AXLE_LOAD_CONTROL_OFFICER);
                    break;
            }

            String userType = user.getUserTypeLink().getCode();
            session.setAttribute("_userNo", user.getId());
            session.setAttribute("_userType", userType);
            session.setAttribute("_userEmail", user.getEmail());
            session.setAttribute("_username", user.getSurname());

            //User photo url
            String photoUrl = user.getPhotoUrl();
            if( StringUtils.isEmpty( photoUrl ) ){
                photoUrl = "/images/avatar00.png";
            }
            session.setAttribute("_photoUrl", photoUrl);
        }

        // Call the superclass method
        super.onAuthenticationSuccess(request, response, auth);

    }


    private HttpSession setClientUsersParentNoAndParentType(HttpSession session, Users user, String parent) {

        if( null != user.getClientUsersLink() ) {
            ClientsUsers clientUsersLink = user.getClientUsersLink();
            session.setAttribute("_userParentNo", clientUsersLink.getClientNo() );
        }

        session.setAttribute("_userParentType", parent);
        return  session;
    }

//    /**
//     * Override this method to ensure that when one logs into the app the login
//     * redirects to the page that one had timed out from.
//     *
//     * @param request
//     * @param response
//     * @return String
//     */
//    @Override
//    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
//        // If the default target url has been defined as always
//        if (isAlwaysUseDefaultTargetUrl()) {
//            return getDefaultTargetUrl();
//        }
//
//        // Set the redirect url if the saved request has been set
//        HttpSession session = request.getSession();
//        if (null != session.getAttribute("SPRING_SECURITY_SAVED_REQUEST")) {
//            // Set the default redirect url
//            String targetURL = ((DefaultSavedRequest) session.getAttribute("SPRING_SECURITY_SAVED_REQUEST")).getRedirectUrl();
//
//            // Unset this param
//            session.removeAttribute("SPRING_SECURITY_SAVED_REQUEST");
//
//            // Return what has been found
//            return targetURL;
//        }
//
//        // Let the app behave as normal
//        return super.determineTargetUrl(request, response);
//    }

}
