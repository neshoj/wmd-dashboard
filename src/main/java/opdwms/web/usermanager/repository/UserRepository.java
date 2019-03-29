package opdwms.web.usermanager.repository;

import opdwms.web.usermanager.entities.Users;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Repository
public interface UserRepository extends CrudRepository<Users, Long> {

    /**
     * Find user by PK
     *
     * @param id
     * @return Optional<Users>
     */
    public Optional<Users> findById(Long id);

    /**
     * Fetch user record given their email address
     *
     * @param email
     * @return Optional<Users>
     */
    public Optional<Users> findByEmail(String email);

    /**
     * Fetch user record given their email address
     *
     * @param email
     * @return List<Users>
     */
    public List<Users> findAllByEmail(String email);
    
    /**
     * Fetch user record given their email token
     * 
     * @param token
     * @return  Optional<Users>
     */
    public Optional<Users> findByEmailToken(String token);


}
