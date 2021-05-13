package hr.fer.progi.worldpiis.backend.controllers;

import hr.fer.progi.worldpiis.backend.model.Project;
import hr.fer.progi.worldpiis.backend.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectRepository repo;

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = new LinkedList<>();
        repo.findAll().forEach(projects::add);
        return ResponseEntity.accepted().body(projects);
    }
}
