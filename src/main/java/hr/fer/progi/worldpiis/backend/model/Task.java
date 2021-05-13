package hr.fer.progi.worldpiis.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import hr.fer.progi.worldpiis.backend.model.util.TaskPriority;
import hr.fer.progi.worldpiis.backend.model.util.TaskStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * This class represents a task.
 * @author miho
 */
@Entity
@Table(name = "tasks")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Task.class)
public class Task {

    /**
     * Task id
     */
    @Id
    @GeneratedValue
    @Column(name = "task_id", nullable = false)
    public Long id;

    /**
     * Task name
     */
    @Column(name = "task_name", nullable = false)
    public String name;

    /**
     * Description of task
     */
    @Column(name = "task_desc")
    public String description;

    /**
     * Task priority
     */
    @Column(name = "task_prio", nullable = false)
    public TaskPriority priority;

    /**
     * Task deadline
     */
    @Column(name = "task_deadline", nullable = false)
    public Date deadline;

    /**
     * Task status
     */
    @Column(name = "task_status", nullable = false)
    public TaskStatus status;

    /**
     * The employee handling this task
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "emp_id")
    @JsonBackReference(value = "emp-task")
    public Employee employee;

    /**
     * The team this task belongs to.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "team_id")
    @JsonBackReference(value = "team-task")
    public Team team;

    public Task() {
    }

    public Task(String name, String description, TaskPriority priority, Date deadline, TaskStatus status, Employee employee) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.status = status;
        this.employee = employee;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public Date getDeadline() {
        return deadline;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
