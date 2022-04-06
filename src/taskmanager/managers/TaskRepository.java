package taskmanager.managers;

import taskmanager.tasks.EpicTask;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;
import taskmanager.utilities.taskservices.TaskType;

import java.util.*;
import java.util.stream.Collectors;

import static taskmanager.utilities.taskservices.TaskType.SUB;

public class TaskRepository {

    private static Map<Integer, Task> storage;

    public TaskRepository() {
        storage = new HashMap<>();
    }

    public void add(Task task) {
        int id = task.getId();
        storage.put(id, task);
        handleTask(task);
    }

    public List<Task> getListOfTasks(TaskType type) {
        return storage.values()
                .stream()
                .filter(task -> task.getType().equals(type))
                .collect(Collectors.toList());
    }

    public List<Task> getListOfSubTasks(Set<Integer> set) {
        return set.stream()
                .map(storage::get)
                .collect(Collectors.toList());
    }

    public Optional<Task> getTaskById(int id) {
        return storage.containsKey(id) ? Optional.of(storage.get(id)) : Optional.empty();
    }

    public void remove(Task task) {
        int id = task.getId();
        storage.remove(id);
    }

    public void updateTask(Task taskOld, Task taskNew) {
        int id = taskOld.getId();
        storage.replace(id, taskOld, taskNew);
    }

    public void clear() {
        storage.clear();
    }

    public static Map<Integer, Task> getStorage() {
        return storage;
    }

    private void handleTask(Task task) {
        if (task.getType().equals(SUB)) {
            int id = task.getId();
            int epicID = ((SubTask) task).getEpicId();
            ((EpicTask) storage.get(epicID)).addIdToList(id);
        }
    }
}
