package opdwms.web.dataagentstatus.repository;

import opdwms.web.dataagentstatus.entities.DataAgentStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataAgentsStatusRepository extends CrudRepository<DataAgentStatus, Long> {
}
