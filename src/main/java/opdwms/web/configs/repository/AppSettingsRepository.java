package opdwms.web.configs.repository;

import opdwms.web.configs.entities.AppSettings;
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
public interface AppSettingsRepository extends CrudRepository<AppSettings, Long> {
}
