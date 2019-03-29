package opdwms.web.weighbridgestations.repository;

import opdwms.web.weighbridgestations.entities.WeighbridgeTypes;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WeighbridgeTypesRepository extends CrudRepository<WeighbridgeTypes, Long> {

    /**
     * Fetch a record given their name
     *
     * @param name
     * @return Optional<WeighbridgeTypes>
     */
    public Optional<WeighbridgeTypes> findByName(String name);

}
