package hr.fer.progi.worldpiis.backend.controllers;

import hr.fer.progi.worldpiis.backend.model.Task;
import hr.fer.progi.worldpiis.backend.model.Team;
import hr.fer.progi.worldpiis.backend.model.util.TaskStatus;
import hr.fer.progi.worldpiis.backend.repository.TaskRepository;
import hr.fer.progi.worldpiis.backend.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
public class TasksController {

    @Autowired
    private TaskRepository taskRepo;

    @Autowired
    private TeamRepository teamRepo;

    @GetMapping("/tasks/{team_id}")
    public ResponseEntity<List<Task>> getAllTasksByTeam(@PathVariable(value = "team_id") Long team_id){
        List<Task> tasks = taskRepo.getByTeam(team_id);

        return tasks.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.accepted().body(tasks);
    }

    @PutMapping("/tasks/{task_id}")
    @Consumes("application/json")
    public ResponseEntity<Task> updateTask(@PathVariable(value = "task_id") Long task_id, @RequestBody Task task){
        Optional<Task> requestedTask = taskRepo.findById(task_id);
        if(requestedTask.isEmpty())
            return ResponseEntity.badRequest().build();

        Task requested = requestedTask.get();
        requested.setName(task.getName());
        requested.setDescription(task.getDescription());
        requested.setPriority(task.getPriority());
        requested.setDeadline(task.getDeadline());
        requested.setStatus(task.getStatus());

        try {
            taskRepo.save(requested);
            return ResponseEntity.accepted().body(requested);
        } catch(Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/tasks/{task_id}")
    public ResponseEntity<Void> deleteTask(@PathVariable(value = "task_id") Long task_id){
        Optional<Task> t = taskRepo.findById(task_id);
        if (t.isEmpty())
            return ResponseEntity.badRequest().build();

        taskRepo.delete(t.get());
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/tasks/{team_id}")
    public ResponseEntity<Task> createTask(@PathVariable(value = "team_id") Long team_id, @RequestBody Task task){
        Task created = new Task(
                task.getName(),
                task.getDescription(),
                task.getPriority(),
                task.getDeadline(),
                TaskStatus.BACKLOG,
                null
        );

        Optional<Team> t = teamRepo.findById(team_id);
        if(t.isEmpty())
            return ResponseEntity.badRequest().build();

        created.setTeam(t.get());

        return ResponseEntity.accepted().body(taskRepo.save(created));
    }
}
