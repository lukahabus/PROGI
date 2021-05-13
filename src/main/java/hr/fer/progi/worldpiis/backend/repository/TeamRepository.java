package hr.fer.progi.worldpiis.backend.repository;

import hr.fer.progi.worldpiis.backend.model.Employee;
import hr.fer.progi.worldpiis.backend.model.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends CrudRepository<Team, Long> {

    @Query("SELECT t FROM Team t JOIN Employee e ON t.teamId = e.team.teamId WHERE e = ?1 AND e.clearanceLevel = 1")
    Optional<Team> getByTeamLead(Employee lead);
}
