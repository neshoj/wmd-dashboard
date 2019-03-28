package atlas.web.usermanager.services;

import atlas.web.usermanager.UserTypeServiceInterface;
import atlas.web.usermanager.entities.UserTypes;
import atlas.web.usermanager.repository.UserTypesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ignatius
 * @version 1.0.0
 * @category User Manager
 * @package Dev
 * @since Nov 05, 2018
 */
@Service
@Transactional
public class UserTypeService implements UserTypeServiceInterface {

    @Autowired
    private UserTypesRepository userTypesRepository;

    /**
     * Fetch a list of user types
     *
     * @return List<UserType>
     */
    @Override
    public List<UserTypes> fetchRecords(HttpServletRequest request) {
        List<UserTypes> userTypes = new ArrayList<>();
        String parentType = (String) request.getSession().getAttribute("_userParentType");

        //When serving superadmin
        if (ObjectUtils.isEmpty(parentType)) {
            userTypes = (List) userTypesRepository.findAll();
        }
        //When server non-super-admins
        else {
            List<String> codes = new ArrayList<>();
            //When serving bank admin
            if (parentType.equals(UserTypes.SCHOOL_ADMIN)) {
                codes.add(UserTypes.SCHOOL_ADMIN);
                codes.add(UserTypes.SCHOOL_OFFICER);
            }

            userTypes = userTypesRepository.findByCodeIn(codes);
        }

        //Return result
        return userTypes;
    }
}
