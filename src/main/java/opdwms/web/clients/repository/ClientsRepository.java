package opdwms.web.clients.repository;

import opdwms.web.clients.entities.Clients;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientsRepository extends CrudRepository<Clients, Long> {
    /**
     * Find a Clients by its name
     *
     * @param Clients the provided search name
     * @return
     */
    Optional<Clients> findByName(String Clients);

    /**
     * Find a name and Id not marching the provided.
     * Used when doing edits, ensure a change in name does not created two records with duplicated names
     *
     * @param name
     * @param Id
     * @return
     */
    Optional<Clients> findByNameAndIdNot(String name, Long Id);

    /**
     * Fetch all records with the flag
     *
     * @param flag
     * @return
     */
    List<Clients> findAllByFlag(String flag);
}
