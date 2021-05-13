package hr.fer.progi.worldpiis.backend.controllers;

import hr.fer.progi.worldpiis.backend.model.Employee;
import hr.fer.progi.worldpiis.backend.model.Project;
import hr.fer.progi.worldpiis.backend.model.Team;
import hr.fer.progi.worldpiis.backend.repository.EmployeeRepository;
import hr.fer.progi.worldpiis.backend.repository.ProjectRepository;
import hr.fer.progi.worldpiis.backend.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class TeamController {

    @Autowired
    private TeamRepository teamRepo;

    @Autowired
    private ProjectRepository projectRepo;

    @Autowired
    private EmployeeRepository empRepo;

    @GetMapping("/teams")
    public ResponseEntity<List<Team>> getAllTeams(){
        List<Team> teams = new LinkedList<>();
        teamRepo.findAll().forEach(teams::add);
        return teams.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.accepted().body(teams);
    }

    @GetMapping("/teams/{lead_uname}")
    public ResponseEntity<Team> getTeamByLead(@PathVariable String lead_uname) {
        Optional<Employee> lead = empRepo.findByUsername(lead_uname);

        if(lead.isEmpty())
            return ResponseEntity.notFound().build();

        Optional<Team> team = teamRepo.getByTeamLead(lead.get());
        return team.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.accepted().body(team.get());
    }

    @GetMapping("/teams/id/{team_id}")
    public ResponseEntity<Team> getById(@PathVariable Long team_id) {
        Optional<Team> team = teamRepo.findById(team_id);
        return team.isEmpty() ?
                ResponseEntity.badRequest().build():
                ResponseEntity.accepted().body(team.get());
    }

    @GetMapping("/teams/proj/{project_id}")
    public ResponseEntity<Team> getTeamByProjectId(@PathVariable Long project_id) {
        Optional<Project> p = projectRepo.findById(project_id);
        return p.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.accepted().body(p.get().getTeam());
    }
}
