package opdwms.web.policeofficers.repository;

import opdwms.web.policeofficers.entities.PoliceOfficers;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PoliceOfficerRepository extends CrudRepository<PoliceOfficers, Long> {

    public Optional<PoliceOfficers> findByPoliceNo(String code);

    public List<PoliceOfficers> findAllByFlag(String flag);

}
