package hr.fer.progi.worldpiis.backend.model.util;

/**
 * This enumeration represents the clearance level of an employee.
 * An employee of the company can be an ordinary developer,
 * a developer who leads a team, a.k.a. the team lead,
 * a coordinator, which coordinates multiple teams in a work group,
 * and a board member, which can oversee projects and task statuses.
 * @author miho
 */
public enum ClearanceLevel {
    DEVELOPER,
    TEAM_LEAD,
    COORDINATOR,
    BOARD
}
