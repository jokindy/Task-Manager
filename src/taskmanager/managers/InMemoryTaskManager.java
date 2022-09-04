package taskmanager.managers;

import taskmanager.interfaces.EpicHandler;
import taskmanager.interfaces.HistoryManager;
import taskmanager.interfaces.TaskManager;
import taskmanager.tasks.EpicTask;
import taskmanager.tasks.Task;

import java.time.LocalDateTime;
import java.util.*;

import static taskmanager.utilities.taskservices.TaskType.*;

public class InMemoryTaskManager implements TaskManager, EpicHandler {

    protected HistoryManager historyManager;
    private final TreeMap<Task, Integer> sortedTasks;
    protected TaskRepository repository;

    public InMemoryTaskManager() {
        repository = new TaskRepository();
        historyManager = new InMemoryHistoryManager();
        sortedTasks = new TreeMap<>();
    }

    @Override
    public boolean checkTask(int id) {
        Optional<Task> task = repository.getTaskById(id);
        return task.isPresent() && task.get().getType().equals(EPIC);
    }


    @Override
    public void addTask(Task task) {
        repository.add(task);
        sortedTasks.put(task, task.getId());
    }

    @Override
    public List<Task> getListOfSimpleTasks() {
        return repository.getListOfTasks(SIMPLE);
    }

    @Override
    public List<Task> getListOfEpicTasks() {
        return repository.getListOfTasks(EPIC);
    }

    @Override
    public List<Task> getEpicSubtasks(int id) {
        Optional<Task> task = repository.getTaskById(id);
        if (task.isPresent()) {
            EpicTask epicTask = (EpicTask) task.get();
            Set<Integer> subId = epicTask.getSetOfSubId();
            return repository.getListOfSubTasks(subId);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        Optional<Task> task = repository.getTaskById(id);
        if (task.isPresent()) {
            historyManager.addTaskToHistory(task.get());
            return task;
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void removeTask(Task task) {
        handleEpic(task);
        repository.remove(task);
        historyManager.removeTask(task.getId());
        sortedTasks.remove(task);
    }

    @Override
    public void editTask(Task taskOld, Task taskNew) {
        repository.updateTask(taskOld, taskNew);
        sortedTasks.remove(taskOld);
        sortedTasks.put(taskNew, taskNew.getId());
    }

    @Override
    public void clearAllTasks() {
        repository.clear();
        historyManager.clearHistory();
        sortedTasks.clear();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public TreeMap<Task, Integer> getPrioritizedTasks() {
        return sortedTasks;
    }

    @Override
    public boolean checkTaskIntersections(Task task) {
        LocalDateTime startTime = task.getStartTime();
        if (startTime != null) {
            for (Task sortedTask : sortedTasks.keySet()) {
                LocalDateTime start = sortedTask.getStartTime();
                if (start != null) {
                    LocalDateTime end = start.plus(sortedTask.getDuration());
                    if (startTime.isAfter(start) && startTime.isBefore(end)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void handleEpic(Task task) {
        if (task.getType().equals(EPIC)) {
            List<Task> listOfSubTasks = ((EpicTask) task).getListOfSubTasks();
            for (Task t: listOfSubTasks) {
                removeTask(t);
            }
        }
    }
}