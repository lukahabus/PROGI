package hr.fer.progi.worldpiis.backend.model.util;

/**
 * This enumeration represents the priority of a task.
 * If a task higher priority than another task, it should
 * be taken from the backlog sooner than the other task.
 * @author miho
 */
public enum TaskPriority {
    VERY_LOW,
    LOW,
    MID,
    HIGH,
    VERY_HIGH
}
