package taskmanager.managers;

import taskmanager.interfaces.*;
import taskmanager.tasks.*;
import taskmanager.utilities.taskservices.TaskType;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected List<Task> simpleTasks;
    protected List<EpicTask> epicTasks;
    protected HistoryManager historyManager;
    private final TreeMap<Task, Integer> sortedTasks;

    public InMemoryTaskManager() {
        simpleTasks = new ArrayList<>();
        epicTasks = new ArrayList<>();
        historyManager = new InMemoryHistoryManager();
        sortedTasks = new TreeMap<>();
    }

    @Override
    public boolean addTask(Task task) {
        TaskType type = task.getType();
        switch (type) {
            case SIMPLE:
                simpleTasks.add(task);
                sortedTasks.put(task, task.getId());
                return true;
            case EPIC:
                epicTasks.add((EpicTask) task);
                ((EpicTask) task).setListOfSubTasks(new ArrayList<>());
                sortedTasks.put(task, task.getId());
                return true;
            case SUB:
                for (EpicTask epicTask : epicTasks) {
                    if (task.getTheme().equals(epicTask.getTheme())) {
                        epicTask.getListOfSubTasks().add((SubTask) task);
                        sortedTasks.put(task, task.getId());
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    @Override
    public List<Task> getListOfSimpleTasks() {
        return simpleTasks;
    }

    @Override
    public List<EpicTask> getListOfEpicTasks() {
        return epicTasks;
    }

    @Override
    public List<SubTask> getEpicSubtasks(int id) {
        for (EpicTask epicTask : epicTasks) {
            List<SubTask> listOfSubTasks = epicTask.getListOfSubTasks();
            if (epicTask.getId() == id && !listOfSubTasks.isEmpty()) {
                return listOfSubTasks;
            }
        }
        return null;
    }

    @Override
    public Task getTaskById(int number, TaskType type) {
        Task task = null;
        switch (type) {
            case SIMPLE:
                for (Task simpleTask : simpleTasks) {
                    if (number == simpleTask.getId()) {
                        task = simpleTask;
                    }
                }
                break;
            case EPIC:
                for (EpicTask epicTask : epicTasks) {
                    if (number == epicTask.getId()) {
                        task = epicTask;
                    }
                }
                break;
            case SUB:
                for (EpicTask epicTask : epicTasks) {
                    List<SubTask> listOfSubTasks = epicTask.getListOfSubTasks();
                    for (SubTask subTask : listOfSubTasks) {
                        if (number == subTask.getId()) {
                            task = subTask;
                        }
                    }
                }
                break;
        }
        if (task != null) {
            historyManager.addTaskToHistory(task);
        }
        return task;
    }

    @Override
    public <T extends Task> void removeTask(T task) {
        TaskType type = task.getType();
        switch (type) {
            case SIMPLE:
                simpleTasks.remove(task);
                break;
            case EPIC:
                epicTasks.remove((EpicTask) task);
                for (SubTask subTask : ((EpicTask) task).getListOfSubTasks()) {
                    int subTaskID = subTask.getId();
                    historyManager.removeTask(subTaskID);
                    sortedTasks.remove(subTask);
                }
                break;
            case SUB:
                for (EpicTask epicTask : epicTasks) {
                    epicTask.getListOfSubTasks().remove((SubTask) task);
                }
                break;
        }
        historyManager.removeTask(task.getId());
        sortedTasks.remove(task);
    }

    @Override
    public <T extends Task> void editTask(T taskOld, T taskNew) {
        TaskType type = taskOld.getType();
        switch (type) {
            case SIMPLE:
                int position = simpleTasks.indexOf(taskOld);
                simpleTasks.set(position, taskNew);
                break;
            case EPIC:
                position = epicTasks.indexOf((EpicTask) taskOld);
                epicTasks.set(position, (EpicTask) taskNew);
                break;
            case SUB:
                for (EpicTask epicTask : epicTasks) {
                    position = epicTask.getListOfSubTasks().indexOf((SubTask) taskOld);
                    epicTask.getListOfSubTasks().set(position, (SubTask) taskNew);
                }
                break;
        }
        sortedTasks.remove(taskOld);
        sortedTasks.put(taskNew, taskNew.getId());
    }

    @Override
    public void clearAllTasks() {
        simpleTasks.clear();
        epicTasks.clear();
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
}