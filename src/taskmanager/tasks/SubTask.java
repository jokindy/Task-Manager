package taskmanager.tasks;

import taskmanager.utilities.taskservices.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static taskmanager.utilities.taskservices.TaskType.SUB;

public class SubTask extends Task {

    public SubTask(int id, String theme, String description, TaskStatus status,
                   Duration duration, LocalDateTime startTime) {
        super(id, theme, description, status, duration, startTime);
        this.type = SUB;
    }
}