package opdwms.web.weighingtransactions.repositories;

import opdwms.web.weighingtransactions.entities.TaggingTransactions;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaggingTransactionsRepository extends CrudRepository<TaggingTransactions, Long> {
}
