package hr.fer.progi.worldpiis.backend.repository;

import hr.fer.progi.worldpiis.backend.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.team.teamId = ?1")
    List<Task> getByTeam(Long t);

    @Query("SELECT t FROM Task t WHERE t.name = ?1")
    Optional<Task> getByName(String name);
}
