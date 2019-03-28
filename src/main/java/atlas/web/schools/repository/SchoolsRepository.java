package atlas.web.schools.repository;

import atlas.web.schools.entities.Schools;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolsRepository extends CrudRepository<Schools, Long> {
    /**
     * Find a Schools by its name
     *
     * @param SchoolsName the provided search name
     * @return
     */
    Optional<Schools> findByName(String SchoolsName);

    /**
     * Find a name and Id not marching the provided.
     * Used when doing edits, ensure a change in name does not created two records with duplicated names
     *
     * @param name
     * @param Id
     * @return
     */
    Optional<Schools> findByNameAndIdNot(String name, Long Id);

    /**
     * Fetch all records with the flag
     *
     * @param flag
     * @return
     */
    List<Schools> findAllByFlag(String flag);
}
