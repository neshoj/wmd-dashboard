package opdwms.web.trafficcensus.repository;

import opdwms.web.trafficcensus.entity.TrafficCensus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrafficCensusRepository extends CrudRepository<TrafficCensus, Long> {
}
