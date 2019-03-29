package opdwms.web.configs.repository;

import opdwms.web.configs.entities.AuditTrail;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Repository
public interface AuditTrailRepository extends CrudRepository<AuditTrail, Long> {
}
