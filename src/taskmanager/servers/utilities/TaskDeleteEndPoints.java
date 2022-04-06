package taskmanager.servers.utilities;

import taskmanager.interfaces.TaskManager;
import taskmanager.tasks.Task;

import java.util.Optional;

public class TaskDeleteEndPoints {

    static String getResponse(TaskManager manager, String path) {
        String[] splitStrings = path.split("/");
        if (path.equals("/tasks")) {
            manager.clearAllTasks();
            return "Cleared!";
        } else {
            String type = splitStrings[2];
            try {
                int id = Integer.parseInt(type);
                Optional<Task> task = manager.getTaskById(id);
                if (task.isPresent()) {
                    manager.removeTask(task.get());
                    return "Removed!";
                } else {
                    return "Error";
                }
            } catch (NumberFormatException ignored) {
                return "406";
            }
        }
    }
}
