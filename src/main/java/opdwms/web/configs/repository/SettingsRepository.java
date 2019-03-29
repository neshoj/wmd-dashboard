package opdwms.web.configs.repository;

import opdwms.web.configs.entities.AppSettings;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @category    Audit Logs
 * @package     Dev
 * @since       Nov 05, 2018
 * @author      Ignatius
 * @version     1.0.0
 */
@Repository
public interface SettingsRepository extends CrudRepository<AppSettings, Long> {

    /**
     *
     * @param code
     * @return Optional<AppSettings>
     */
    public Optional<AppSettings> findByCode(String code);
}
