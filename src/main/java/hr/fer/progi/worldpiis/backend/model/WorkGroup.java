package hr.fer.progi.worldpiis.backend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.util.List;

/**
 * This class represents a work group.
 * A work group consists of multiple teams, and has
 * a coordinator.
 * @author miho
 */
@Entity
@Table(name = "workgroups")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class WorkGroup {

    /**
     * Work group ID
     */
    @Id
    @GeneratedValue
    @Column(name = "wg_id")
    public Long id;

    /**
     * The coordinator of this work group
     */
    @OneToOne
    public Employee coordinator;

    /**
     * Teams in this work group
     */
    @OneToMany(mappedBy = "workGroup")
    @JsonBackReference(value = "team-wg")
    public List<Team> teams;

    public WorkGroup() {
    }

    public WorkGroup(Employee coordinator, List<Team> teams) {
        this.coordinator = coordinator;
        this.teams = teams;
    }

    public Long getId() {
        return id;
    }

    public Employee getCoordinator() {
        return coordinator;
    }

    public List<Team> getTeams() {
        return teams;
    }
}
