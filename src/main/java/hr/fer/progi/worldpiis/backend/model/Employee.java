package hr.fer.progi.worldpiis.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.sun.istack.Nullable;
import hr.fer.progi.worldpiis.backend.model.util.ClearanceLevel;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents an employee.
 * An employee has an ID and a username, which are primary keys.
 * An employee also has a name, surname, phone number, email address,
 * a clearance level, and a team he belongs to.
 * One employee can have many tasks.
 * @author miho
 */
@Entity
@Table(name = "employees")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id", scope=Employee.class)
public class Employee {

    /**
     * Employee ID
     */
    @Id
    @GeneratedValue
    @Column(name = "emp_id")
    public Long id;

    /**
     * Employee website username
     */
    @Column(name = "emp_username", unique = true, nullable = false)
    public String username;

    /**
     * Employee password
     */
    @Column(name = "emp_password", nullable = false)
    public String password;

    /**
     * Employee name
     */
    @Column(name = "emp_name", nullable = false)
    public String name;

    /**
     * Employee surname
     */
    @Column(name = "emp_surname", nullable = false)
    public String surname;

    /**
     * Employee phone number
     */
    @Column(name = "emp_phone")
    public String phoneNumber;

    /**
     * Employee email
     */
    @Column(name = "emp_email")
    public String email;

    /**
     * Employee clearance level
     */
    @Column(name = "emp_clearance", nullable = false)
    public ClearanceLevel clearanceLevel;

    /**
     * ID of employees team
     */
    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    //@JsonManagedReference(value = "emp-team") OVO JE POPRAVILO GRESKE ZA PUT
    public Team team;

    /**
     * List of tasks the employee has taken upon himself.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "employee")
    //@JsonManagedReference(value = "emp-task")
    public List<Task> tasks;

    public Employee() {
    }

    public Employee(String username, String name, String surname,
                    String phoneNumber, String email,
                    ClearanceLevel clearanceLevel, Team team, List<Task> tasks) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.clearanceLevel = clearanceLevel;
        this.team = team;
        this.tasks = tasks;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ClearanceLevel getClearanceLevel() {
        return clearanceLevel;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
