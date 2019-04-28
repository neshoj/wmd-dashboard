package opdwms.web.axleclassification.repository;

import opdwms.web.axleclassification.entities.AxleClassification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AxleClassificationRepository extends CrudRepository<AxleClassification, Long> {

    Optional<AxleClassification> findByAxleCode(String axleClass);
    Optional<AxleClassification> findByAxleCodeAndIdNot(String name, Long Id);
    List<AxleClassification> findAllByFlag(String flag);
}
