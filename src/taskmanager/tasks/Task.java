package taskmanager.tasks;

import taskmanager.utilities.taskservices.TaskStatus;
import taskmanager.utilities.taskservices.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static taskmanager.utilities.taskservices.TaskType.SIMPLE;

public class Task implements Comparable<Task> {

    private final int id;
    protected TaskType type;
    private String theme;
    private String description;
    protected TaskStatus status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(int id, String theme, String description, TaskStatus status,
                Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.type = SIMPLE;
        this.theme = theme;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public TaskType getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        String text = this.id + ","
                + this.type + ","
                + this.theme + ","
                + this.description + ","
                + this.status + ","
                + this.duration.toHours() + ",";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH.mm");
        if (this.startTime != null) {
            return text + this.startTime.format(formatter);
        } else {
            return text + null;
        }
    }

    @Override
    public int compareTo(Task task) {
        LocalDateTime date = task.getStartTime();
        int compare;
        if (this.startTime == null) {
            compare = 0;
        } else if (date == null) {
            compare = 0;
        } else {
            compare = this.startTime.compareTo(date);
        }
        if (compare == 0) {
            compare = Integer.compare(this.id, task.getId());
        }
        return compare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return getId() == task.getId() &&
                getType() == task.getType() &&
                Objects.equals(getTheme(), task.getTheme()) &&
                Objects.equals(getDescription(), task.getDescription()) &&
                getStatus() == task.getStatus() &&
                Objects.equals(getDuration(), task.getDuration()) &&
                Objects.equals(getStartTime(), task.getStartTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getType(), getTheme(), getDescription(),
                getStatus(), getDuration(), getStartTime());
    }
}