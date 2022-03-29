package taskmanager.interfaces;

import taskmanager.tasks.*;
import taskmanager.utilities.taskservices.TaskType;

import java.util.List;
import java.util.TreeMap;

public interface TaskManager {

    boolean addTask(Task task);

    List<Task> getListOfSimpleTasks();

    List<EpicTask> getListOfEpicTasks();

    List<SubTask> getEpicSubtasks(int id);

    Task getTaskById(int number, TaskType type);

    <T extends Task> void editTask(T task1, T task2);

    <T extends Task> void removeTask(T task);

    void clearAllTasks();

    List<Task> getHistory();

    TreeMap<Task, Integer> getPrioritizedTasks();

    boolean checkTaskIntersections(Task task);
}