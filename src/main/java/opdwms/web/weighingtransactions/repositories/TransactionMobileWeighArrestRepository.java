package opdwms.web.weighingtransactions.repositories;

import opdwms.web.weighingtransactions.entities.TransactionMobileWeighArrest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMobileWeighArrestRepository extends CrudRepository<TransactionMobileWeighArrest, Long> {

}
