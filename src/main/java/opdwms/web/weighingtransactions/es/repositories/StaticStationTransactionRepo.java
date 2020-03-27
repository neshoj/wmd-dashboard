package opdwms.web.weighingtransactions.es.repositories;

import opdwms.web.weighingtransactions.es.documents.StaticStationTransaction;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaticStationTransactionRepo extends ElasticsearchRepository<StaticStationTransaction, String> {

}
