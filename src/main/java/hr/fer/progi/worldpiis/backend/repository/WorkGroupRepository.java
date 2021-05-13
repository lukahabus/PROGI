package hr.fer.progi.worldpiis.backend.repository;

import hr.fer.progi.worldpiis.backend.model.Employee;
import hr.fer.progi.worldpiis.backend.model.WorkGroup;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkGroupRepository extends CrudRepository<WorkGroup, Long> {

    @Query("SELECT wg FROM WorkGroup wg WHERE wg.coordinator = ?1")
    Optional<WorkGroup> getByCoordinator(Employee e);

    @Query("SELECT wg FROM WorkGroup wg WHERE wg.coordinator.id = ?1")
    Optional<WorkGroup> getByCoordinatorId(Long id);
}
