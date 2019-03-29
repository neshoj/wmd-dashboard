package opdwms.web.usermanager.repository;

import opdwms.web.usermanager.entities.Permissions;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;


/**
 *
 * @category    User Manager
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Repository
public interface PermissionsRepository extends CrudRepository<Permissions, Long> {

    /**
     * Fetch a set of permissions given their PKS
     *
     * @param pks
     * @return Set<Permissions>
     */
    @Query("SELECT a FROM Permissions a WHERE a.id IN( :pks ) ")
    public Set<Permissions> fetchByIds(@Param("pks") List<Long> pks);

    /**
     * Fetch records given an application role
     *
     * @param role
     * @return List<Permissions>
     */
    @Query("SELECT a FROM Permissions a LEFT JOIN a.roleLink b WHERE b.appFunction LIKE %?1% ")
    public List<Permissions> fetchByRole(String role);
}
