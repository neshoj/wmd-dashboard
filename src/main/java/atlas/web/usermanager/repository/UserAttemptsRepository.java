package atlas.web.usermanager.repository;

import atlas.web.usermanager.entities.UserAttempts;
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
public interface UserAttemptsRepository extends CrudRepository<UserAttempts, Long> {

    /**
     * Fetch a list of attempts of a specific user
     * @param email User principal object
     * @return List<UserAttempts>
     */
    public List<UserAttempts> findByEmail(String email);
}
