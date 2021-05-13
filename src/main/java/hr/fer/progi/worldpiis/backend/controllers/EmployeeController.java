package hr.fer.progi.worldpiis.backend.controllers;

import hr.fer.progi.worldpiis.backend.model.Employee;
import hr.fer.progi.worldpiis.backend.model.Task;
import hr.fer.progi.worldpiis.backend.repository.EmployeeRepository;
import hr.fer.progi.worldpiis.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * This class represents a rest controller for Employee type of objects to connect with Database.
 * @author evelyn
 */
@RestController
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeRepository empRepo;

    @Autowired
    private TaskRepository taskRepo;

    /**
     * method which returns all employees in Database when sending a get request on localhost/employees route
     * @return response entity with http status ok and in response body
     * a List of all employees in Database
     */
    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees(){
        List<Employee> employees = new LinkedList<>();
        empRepo.findAll().forEach(employees::add);
        return ResponseEntity.accepted().body(employees);
    }

    /**
     * method which returns an employee from Database with given username
     * when sending a get request on localhost/employees/{username} route
     * @param username is given username
     * @return response entity with http status ok and in response body a json of
     * an employee whose username is equal to a given username if such exists in Database,
     * otherwise it returns response entity with http status not found and null in response body.
     */
    @GetMapping("/employees/{username}")
    public ResponseEntity<Employee> getEmployeeByUsername(@PathVariable(value = "username") String username) {
        Optional<Employee> employee = empRepo.findByUsername(username);
        return employee.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.accepted().body(employee.get());
    }



    /** method which updates an employee with given username
     * when sending a put request on localhost/employees/{username} route.
     * The concrete employee has to be update modeled on employee given in request body.
     * @param username is given username
     * @param emp is employee from request body with attributes on whose value we need to set an employee attributes with given username
     * @return response entity with http status ok and in response body a json of
     * updated employee if update was successfully,
     * otherwise it returns response entity with http status bad request and null in response body
     */
     @PutMapping("/employees/{username}")
     @Consumes("application/json")
     public ResponseEntity<Employee> updateEmployeeWithUsername(@PathVariable(value= "username") String username, @RequestBody Employee emp){
         if(username == null || emp == null)
             return ResponseEntity.badRequest().build();

         Optional<Employee> employee = empRepo.findByUsername(emp.getUsername());

         if(employee.isEmpty())
             return ResponseEntity.notFound().build();

         if(!Objects.equals(username, employee.get().username))
             return ResponseEntity.badRequest().build();

         Employee e = employee.get();       
         e.setEmail(emp.getEmail());
         e.setName(emp.getName());
         e.setSurname(emp.getSurname());
         e.setPassword(emp.getPassword());
         e.setPhoneNumber(emp.getPhoneNumber());

         try {
             empRepo.save(e);
             return ResponseEntity.accepted().body(e);
         } catch(Exception ex) {
             return ResponseEntity.badRequest().build();
         }
    }

    @PutMapping("/employees/{username}/tasks/{task_id}")
    @Consumes("application/json")
    public ResponseEntity<Employee> updateEmployeeTasks(
            @PathVariable(value= "username") String username,
            @PathVariable(value= "task_id") Long task_id
    ){
        Optional<Employee> employee = empRepo.findByUsername(username);

        if(employee.isEmpty())
            return ResponseEntity.notFound().build();

        Optional<Task> t = taskRepo.findById(task_id);

        if (t.isEmpty())
            return ResponseEntity.badRequest().build();

        Task task = t.get();
        Employee e = employee.get();
        task.setEmployee(e);
        task.setTeam(e.getTeam());
        e.getTasks().add(task);
        taskRepo.save(task);
        empRepo.save(e);
        return ResponseEntity.accepted().body(e);
    }
}