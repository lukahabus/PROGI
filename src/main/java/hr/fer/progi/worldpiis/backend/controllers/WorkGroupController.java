package hr.fer.progi.worldpiis.backend.controllers;

import hr.fer.progi.worldpiis.backend.model.Team;
import hr.fer.progi.worldpiis.backend.model.WorkGroup;
import hr.fer.progi.worldpiis.backend.repository.WorkGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class WorkGroupController {

    @Autowired
    private WorkGroupRepository wgRepo;

    @GetMapping("/workgroups/{coordinator_id}")
    public ResponseEntity<WorkGroup> getWorkGroupByCoordinatorId(@PathVariable(value = "coordinator_id") Long coordinatorId) {
        Optional<WorkGroup> workGroup = wgRepo.getByCoordinatorId(coordinatorId);

        return workGroup.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.accepted().body(workGroup.get());
    }

    @GetMapping("/workgroups/{coordinator_id}/teams")
    public ResponseEntity<List<Team>> getWorkGroupTeamsByCoordinatorID(@PathVariable(value = "coordinator_id") Long coordinatorId) {
        Optional<WorkGroup> workGroup = wgRepo.getByCoordinatorId(coordinatorId);

        return workGroup.isEmpty() ?
               ResponseEntity.notFound().build() :
               ResponseEntity.accepted().body(workGroup.get().getTeams());
    }
}
