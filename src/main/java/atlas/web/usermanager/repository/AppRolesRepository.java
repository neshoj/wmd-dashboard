package atlas.web.usermanager.repository;

import atlas.web.usermanager.entities.AppRoles;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

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
public interface AppRolesRepository extends CrudRepository<AppRoles, Long> {

    /**
     * Fetch record by name
     *
     * @param name
     * @return Optional<AppRoles>
     */
    public Optional<AppRoles> findByName(String name);
}
