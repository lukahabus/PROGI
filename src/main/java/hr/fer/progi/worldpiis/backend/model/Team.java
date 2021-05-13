package hr.fer.progi.worldpiis.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents a team.
 * @author miho
 */
@Entity
@Table(name = "teams")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "teamId")
public class Team {

    /**
     * Team ID
     */
    @Id
    @GeneratedValue
    @Column(name = "team_id")
    public Long teamId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    @JsonBackReference(value = "emp-team")
    public List<Employee> employees;

    /**
     * The work group this team belongs to, can be null
     */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wg_id")
    @JsonBackReference(value = "team-wg")
    public WorkGroup workGroup;

    /**
     * The tasks this team owns.
     */
    @OneToMany(orphanRemoval = true, mappedBy = "team")
    //@JsonManagedReference(value = "team-task")
    public List<Task> tasks;

    public Team() {
    }

    public Team(List<Employee> employees, WorkGroup workGroupId, List<Task> tasks) {
        this.employees = employees;
        this.workGroup = workGroupId;
        this.tasks = tasks;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public WorkGroup getWorkGroupId() {
        return workGroup;
    }

    public void setWorkGroupId(WorkGroup workGroupId) {
        this.workGroup = workGroupId;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
