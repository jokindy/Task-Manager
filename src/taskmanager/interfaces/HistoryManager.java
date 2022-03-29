package taskmanager.interfaces;

import taskmanager.tasks.*;

import java.util.List;

public interface HistoryManager {

    void addTaskToHistory(Task task);

    void removeTask(int id);

    void clearHistory();

    List<Task> getHistory();
}
