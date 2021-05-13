package hr.fer.progi.worldpiis.backend;

import hr.fer.progi.worldpiis.backend.controllers.EmployeeController;
import hr.fer.progi.worldpiis.backend.controllers.TasksController;
import hr.fer.progi.worldpiis.backend.model.Employee;
import hr.fer.progi.worldpiis.backend.model.Task;
import hr.fer.progi.worldpiis.backend.model.Team;
import hr.fer.progi.worldpiis.backend.model.WorkGroup;
import hr.fer.progi.worldpiis.backend.model.util.ClearanceLevel;
import hr.fer.progi.worldpiis.backend.model.util.TaskPriority;
import hr.fer.progi.worldpiis.backend.model.util.TaskStatus;
import hr.fer.progi.worldpiis.backend.repository.EmployeeRepository;
import hr.fer.progi.worldpiis.backend.repository.TaskRepository;
import hr.fer.progi.worldpiis.backend.repository.TeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class UnitTests {

    @Mock
    EmployeeRepository employeeRepository;
    @InjectMocks
    EmployeeController employeeController;
    @Mock
    TaskRepository taskRepository;
    @InjectMocks
    TasksController tasksController;
    @Mock
    TeamRepository teamRepository;

    @Test
    public void editProfileCorrectly() {

        Employee employee = new Employee("iva123","Iva","Ivić","0910000000",
                "iva@fer.hr",ClearanceLevel.DEVELOPER,null,null);

        Mockito.when(employeeRepository.findByUsername(employee.getUsername())).thenReturn(Optional.of(employee));

        String newSurname="Ivančić";
        Employee employeeWithNewSurname = new Employee(employee.getUsername(),employee.getName(),newSurname,
                employee.getPhoneNumber(), employee.getEmail(),employee.getClearanceLevel(),
                employee.getTeam(),employee.getTasks());

        String[] expected = {newSurname};
        String[] result = {employeeController.updateEmployeeWithUsername(employee.getUsername(),
                employeeWithNewSurname).getBody().getSurname()};
        Assertions.assertArrayEquals(expected,result);

    }

    @Test
    public void editProfileIncorrectly() {

        Employee employee = new Employee("iva123","Iva","Ivić","0910000000",
                "iva@fer.hr",ClearanceLevel.DEVELOPER,null,null);

        String newUsername="iva1234";
        Employee employeeWithNewUsername = new Employee(newUsername,employee.getName(),employee.getSurname(),
                employee.getPhoneNumber(), employee.getEmail(),employee.getClearanceLevel(),
                employee.getTeam(),employee.getTasks());

        String[] expected = {"404 NOT_FOUND"};
        String[] result = {employeeController.updateEmployeeWithUsername(employee.getUsername(),
                employeeWithNewUsername).getStatusCode().toString()};
        Assertions.assertArrayEquals(expected,result);

    }

    @Test
    public void editTaskCorrectly(){

        Employee coordinator = new Employee("iva123","Iva","Ivić","0910000000",
                "iva@fer.hr",ClearanceLevel.COORDINATOR,null,null);
        WorkGroup workGroup = new WorkGroup(coordinator,null);
        Team team = new Team(null,workGroup,null);
        team.setTeamId(Long.valueOf(101));
        Task task = new Task("Testiranje sustava","Potrebno testirati sustav.",TaskPriority.HIGH,
                new Date(2020,1,2),TaskStatus.BACKLOG,null);
        task.id = Long.valueOf(22);

        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        TaskPriority newPriority = TaskPriority.VERY_HIGH;
        Task taskWithNewPriority = new Task(task.getName(), task.getDescription(),newPriority,
                task.getDeadline(),task.getStatus(),task.getEmployee());
        taskWithNewPriority.id = task.getId();

        TaskPriority[] expected = {newPriority};
        TaskPriority[] result = {tasksController.updateTask(task.getId(),taskWithNewPriority).getBody().getPriority()};
        Assertions.assertArrayEquals(expected,result);

    }

    @Test
    public void editTaskIncorrectly(){

        Team team = new Team(null,null,null);
        team.setTeamId(Long.valueOf(101));
        Task task = new Task("Testiranje sustava","Potrebno testirati sustav.",TaskPriority.HIGH,
                new Date(2020,1,2),TaskStatus.BACKLOG,null);
        task.id = Long.valueOf(22);

        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Task taskWithNoName = new Task(null, task.getDescription(),task.getPriority(),
                task.getDeadline(),task.getStatus(),task.getEmployee());
        taskWithNoName.id = task.getId();

        String[] expected = {"400 BAD_REQUEST"};
        String[] result = {tasksController.updateTask(task.getId(),taskWithNoName).getStatusCode().toString()};
        Assertions.assertArrayEquals(expected,result);


    }

    @Test
    public void addTaskCorrectly(){

        Team team = new Team(null,null,null);
        team.setTeamId(Long.valueOf(101));
        Mockito.when(teamRepository.findById(team.getTeamId())).thenReturn(Optional.of(team));

        Task task = new Task("Testiranje sustava","Potrebno testirati sustav.",TaskPriority.HIGH,
                new Date(2020,1,2),TaskStatus.BACKLOG,null);
        Mockito.when(taskRepository.save(any(Task.class))).thenReturn(task);
        Task createdTask = tasksController.createTask(team.getTeamId(),task).getBody();

        String[] expected = {task.getName(),task.getDescription(),task.getPriority().toString(),task.getDeadline().toString()};
        String[] result = {createdTask.getName(),createdTask.getDescription(),createdTask.getPriority().toString(),
                createdTask.getDeadline().toString()};
        Assertions.assertArrayEquals(expected,result);

    }

    @Test
    public void addTaskIncorrectly(){

        Team team = new Team(null,null,null);
        team.setTeamId(Long.valueOf(101));
        Mockito.when(teamRepository.findById(team.getTeamId())).thenReturn(Optional.empty());

        Task taskWithoutName = new Task(null,"Potrebno testirati sustav.",TaskPriority.HIGH,
                new Date(2020,1,2),TaskStatus.BACKLOG,null);

        Task[] expected = {null};
        Task[] result = {tasksController.createTask(team.getTeamId(),taskWithoutName).getBody()};
        Assertions.assertArrayEquals(expected,result);

    }

}
