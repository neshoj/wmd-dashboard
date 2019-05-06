package opdwms.web.weighingtransactions.repositories;

import opdwms.web.weighingtransactions.entities.TaggingClearanceReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaggingClearanceReportRepository extends CrudRepository<TaggingClearanceReport, Long> {
}
