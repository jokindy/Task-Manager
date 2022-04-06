package taskmanager.tasks;

import taskmanager.utilities.taskservices.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

import static taskmanager.utilities.taskservices.TaskType.SUB;

public class SubTask extends Task {

    private final int epicId;

    public SubTask(int id, String theme, String description, TaskStatus status,
                   Duration duration, LocalDateTime startTime, int epicId) {
        super(id, theme, description, status, duration, startTime);
        this.epicId = epicId;
        this.type = SUB;
    }

    public SubTask(SubTask anotherTask) {
        super(anotherTask);
        this.epicId = anotherTask.epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}