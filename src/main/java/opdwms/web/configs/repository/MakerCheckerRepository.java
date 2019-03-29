package opdwms.web.configs.repository;

import opdwms.web.configs.entities.MakerChecker;
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
public interface MakerCheckerRepository extends CrudRepository<MakerChecker, Long> {

    public Optional<MakerChecker> findByModule(String module);
}
