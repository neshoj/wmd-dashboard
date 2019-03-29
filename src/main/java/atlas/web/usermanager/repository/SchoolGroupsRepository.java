package atlas.web.usermanager.repository;

import atlas.web.usermanager.entities.ClientsGroups;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Repository
public interface SchoolGroupsRepository extends CrudRepository<ClientsGroups, Long> {
}
