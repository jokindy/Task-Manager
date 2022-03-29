package taskmanager.tasks;

import taskmanager.utilities.taskservices.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static taskmanager.utilities.taskservices.TaskStatus.*;
import static taskmanager.utilities.taskservices.TaskType.EPIC;

public class EpicTask extends Task {

    private List<SubTask> listOfSubTasks;

    public EpicTask(int id, String theme, String description, TaskStatus status,
                    Duration duration, LocalDateTime startTime) {
        super(id, theme, description, status, duration, startTime);
        this.type = EPIC;
    }

    public void setListOfSubTasks(List<SubTask> listOfSubTasks) {
        this.listOfSubTasks = listOfSubTasks;
    }

    public List<SubTask> getListOfSubTasks() {
        return listOfSubTasks;
    }

    @Override
    public TaskStatus getStatus() {
        Set<TaskStatus> statuses = new HashSet<>();
        for (SubTask subTask : listOfSubTasks) {
            statuses.add(subTask.getStatus());
        }
        if ((statuses.size() == 1 && statuses.contains(NEW)) || listOfSubTasks.isEmpty()) {
            this.status = NEW;
        } else if (statuses.size() == 1 && statuses.contains(DONE)) {
            this.status = DONE;
        } else {
            this.status = IN_PROGRESS;
        }
        return status;
    }

    @Override
    public Duration getDuration() {
        Duration duration = Duration.ZERO;
        for (SubTask subTask : listOfSubTasks) {
            duration = duration.plus(subTask.getDuration());
        }
        this.duration = duration;
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        if (!listOfSubTasks.isEmpty()) {
            this.startTime = listOfSubTasks.get(0).getStartTime();
        }
        return startTime;
    }
}