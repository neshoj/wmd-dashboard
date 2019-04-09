package opdwms.web.weighbridgestations.repository;

import opdwms.web.weighbridgestations.entities.WeighbridgeStations;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeighbridgeStationsRepository extends CrudRepository<WeighbridgeStations, Long> {

    /**
     * Fetch a record given their name
     *
     * @param name
     * @return Optional<WeighbridgeStations>
     */
    public Optional<WeighbridgeStations> findByName(String name);

    public Optional<WeighbridgeStations> findByStationCode(String code);

    public List<WeighbridgeStations> findAllByFlag(String flag);

}
