package hr.fer.progi.worldpiis.backend.repository;

import hr.fer.progi.worldpiis.backend.model.Project;
import hr.fer.progi.worldpiis.backend.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    @Query("SELECT p FROM Project p WHERE p.name = ?1")
    Optional<Project> getByName(String name);

    @Query("SELECT p FROM Project p WHERE p.team = ?1")
    Optional<Project> getByTeam(Team t);
}
