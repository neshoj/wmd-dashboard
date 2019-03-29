package opdwms.web.usermanager.repository;

import opdwms.web.usermanager.entities.UserTypes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Repository
public interface UserTypesRepository extends CrudRepository<UserTypes, Long> {

    /**
     * Fetch records given  a list of codes
     *
     * @param codes
     * @return  List<UserTypes>
     */

    public List<UserTypes> findByCodeIn(List codes);

}
