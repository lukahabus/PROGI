package hr.fer.progi.worldpiis.backend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

/**
 * This class represents a project.
 */
@Entity
@Table(name = "projects")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Project {

    /**
     * Project ID
     */
    @Id
    @GeneratedValue
    @Column(name = "project_id", nullable = false)
    public Long id;

    /**
     * Project name
     */
    @Column(name = "project_name", nullable = false)
    public String name;

    /**
     * Project description
     */
    @Column(name = "project_desc")
    public String description;

    /**
     * The team handling this project
     */
    @OneToOne(optional = false, orphanRemoval = true, cascade = CascadeType.ALL)
    //@JsonManagedReference(value = "project-team")
    public Team team;

    public Project() {
    }

    public Project(String name, String description, Team team) {
        this.name = name;
        this.description = description;
        this.team = team;
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

    public Team getTeam() {
        return team;
    }
}
