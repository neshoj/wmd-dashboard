package opdwms.web.weighingtransactions.repositories;

import opdwms.web.weighingtransactions.entities.HSWIMTransaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HSWIMTransactionsRepository extends CrudRepository<HSWIMTransaction, Long> {
}
