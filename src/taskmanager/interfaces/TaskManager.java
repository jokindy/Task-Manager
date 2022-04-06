package taskmanager.interfaces;

import taskmanager.tasks.Task;

import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

public interface TaskManager {

    boolean checkTask(int id);

    void addTask(Task task);

    List<Task> getListOfSimpleTasks();

    List<Task> getListOfEpicTasks();

    List<Task> getEpicSubtasks(int id);

    Optional<Task> getTaskById(int number);

    void editTask(Task task1, Task task2);

    void removeTask(Task task);

    void clearAllTasks();

    List<Task> getHistory();

    TreeMap<Task, Integer> getPrioritizedTasks();

    boolean checkTaskIntersections(Task task);
}