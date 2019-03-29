package opdwms.web.usermanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import opdwms.web.usermanager.entities.ClientsUsers;


/**
 * @author Ignatius
 * @version 1.0.0
 * @category User Manager
 * @package Dev
 * @since Nov 05, 2018
 */
@Repository
public interface SchoolsUsersRepository extends CrudRepository<ClientsUsers, Long> {
}
