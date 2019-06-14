package opdwms.web.weighingtransactions.repositories;

import opdwms.web.weighingtransactions.entities.TransactionMobileWeighProsecution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionMobileWeighProsecutionRepository extends CrudRepository<TransactionMobileWeighProsecution, Long> {

}
