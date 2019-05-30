package opdwms.web.weighingtransactions.repositories;

import opdwms.web.weighingtransactions.entities.VirtualStationTransactions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualStationTransactionRepository  extends CrudRepository<VirtualStationTransactions, Long> {
}
