package taskmanager.tasks;

import taskmanager.utilities.taskservices.TaskStatus;
import taskmanager.utilities.taskservices.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;

public class TaskDTO {

    private final int id;
    private final TaskType type;
    private final String theme;
    private final String description;
    private final TaskStatus status;
    private final Duration duration;
    private final LocalDateTime startTime;
    private final int epicId;
    private final Set<Integer> listOfSubId;

    public TaskDTO(Task task) {
        this.id = task.getId();
        this.type = task.getType();
        this.theme = task.getTheme();
        this.description = task.getDescription();
        this.status = task.status;
        this.duration = task.duration;
        this.startTime = task.startTime;
        if (task instanceof SubTask) {
            this.epicId = ((SubTask) task).getEpicId();
        } else {
            this.epicId = 0;
        }
        if (task instanceof EpicTask) {
            this.listOfSubId = ((EpicTask) task).getSetOfSubId();
        } else {
            this.listOfSubId = null;
        }
    }

    @Override
    public String toString() {
        String text = this.id + ";"
                + this.type + ";"
                + this.theme + ";"
                + this.description + ";"
                + this.status + ";"
                + this.duration.toHours() + ";";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm");
        if (this.startTime != null) {
            text += this.startTime.format(formatter) + ";";
        } else {
            text += null + ";";
        }
        return text + this.epicId + ";"
                + this.listOfSubId;
    }

}
