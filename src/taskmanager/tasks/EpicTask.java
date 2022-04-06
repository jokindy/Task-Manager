package taskmanager.tasks;

import taskmanager.managers.TaskRepository;
import taskmanager.utilities.taskservices.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static taskmanager.utilities.taskservices.TaskStatus.*;
import static taskmanager.utilities.taskservices.TaskType.EPIC;

public class EpicTask extends Task {

    private Set<Integer> setOfSubId;

    public EpicTask(int id, String theme, String description, TaskStatus status,
                    Duration duration, LocalDateTime startTime) {
        super(id, theme, description, status, duration, startTime);
        this.type = EPIC;
        this.setOfSubId = new HashSet<>();
    }

    public EpicTask(EpicTask anotherEpic) {
        super(anotherEpic);
        this.setOfSubId = anotherEpic.setOfSubId;
    }

    public void addIdToList(int id) {
        setOfSubId.add(id);
    }

    public void setSetOfSubId(Set<Integer> setOfSubId) {
        this.setOfSubId = setOfSubId;
    }

    public Set<Integer> getSetOfSubId() {
        return setOfSubId;
    }

    @Override
    public TaskStatus getStatus() {
        List<Task> listOfSubTasks = getListOfSubTasks();
        Set<TaskStatus> statuses = new HashSet<>();
        for (Task subTask : listOfSubTasks) {
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
    public void setStatus(TaskStatus status) {
        System.out.println("Статус у эпика рассчитывается автоматически. \nЗадача не обновлена.");
    }

    @Override
    public void setDuration(Duration duration) {
        System.out.println("Продолжительность эпика рассчитывается автоматически. \nЗадача не обновлена.");
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        System.out.println("Дата старта эпика рассчитывается автоматически. \nЗадача не обновлена.");
    }

    @Override
    public Duration getDuration() {
        List<Task> listOfSubTasks = getListOfSubTasks();
        Duration duration = Duration.ZERO;
        for (Task subTask : listOfSubTasks) {
            duration = duration.plus(subTask.getDuration());
        }
        this.duration = duration;
        return duration;
    }

    @Override
    public LocalDateTime getStartTime() {
        List<Task> listOfSubTasks = getListOfSubTasks();
        if (!listOfSubTasks.isEmpty()) {
            this.startTime = listOfSubTasks.get(0).getStartTime();
        }
        return startTime;
    }

    public List<Task> getListOfSubTasks() {
        Map<Integer, Task> subMap = TaskRepository.getStorage()
                .entrySet().stream()
                .filter(x -> setOfSubId.contains(x.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return new ArrayList<>(subMap.values());
    }
}