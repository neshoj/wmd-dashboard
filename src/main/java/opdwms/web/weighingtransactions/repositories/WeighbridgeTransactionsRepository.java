package opdwms.web.weighingtransactions.repositories;

import opdwms.web.weighingtransactions.entities.WeighingTransactions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeighbridgeTransactionsRepository extends CrudRepository<WeighingTransactions, Long> {
}
