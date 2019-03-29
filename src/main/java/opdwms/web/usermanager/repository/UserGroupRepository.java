package opdwms.web.usermanager.repository;

import opdwms.web.usermanager.entities.UserGroups;
import org.springframework.data.jpa.repository.Query;
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
public interface UserGroupRepository extends CrudRepository<UserGroups, Long> {

    /**
     * Fetch a collection of records with the supplied name
     *
     * @param name
     * @return List<UserGroups>
     */
    public List<UserGroups> findByName(String name);

    /**
     * Fetch merchant records
     *
     * @param parentNo
     * @return List<UserGroups>
     */
    @Query("SELECT a FROM UserGroups a LEFT JOIN a.clientsGroups b WHERE b.clientNo = ?1 AND a.flag = '1' ")
    public List<UserGroups> fetchClientGroupRecords(Long parentNo);


    /**
     * Fetch all records by flag
     *
     * @param flag
     * @return List<UserGroups>
     */
    List<UserGroups> findAllByFlag(String flag);
}
