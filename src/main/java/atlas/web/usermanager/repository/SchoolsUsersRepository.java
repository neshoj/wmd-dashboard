package atlas.web.usermanager.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import atlas.web.usermanager.entities.SchoolUsers;


/**
 * @author Ignatius
 * @version 1.0.0
 * @category User Manager
 * @package Dev
 * @since Nov 05, 2018
 */
@Repository
public interface SchoolsUsersRepository extends CrudRepository<SchoolUsers, Long> {
    public long countBankUsersBySchool(Long parentNo);
}
