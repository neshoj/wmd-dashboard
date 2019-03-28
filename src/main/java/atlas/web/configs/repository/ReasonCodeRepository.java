package atlas.web.configs.repository;

import atlas.web.configs.entities.ReasonCodes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Ignatius
 * @version 1.0.0
 * @category Audit Logs
 * @package Dev
 * @since Nov 05, 2018
 */
@Repository
public interface ReasonCodeRepository extends CrudRepository<ReasonCodes, Long> {
    Iterable<ReasonCodes> findAllByFlag(String flag);

}
