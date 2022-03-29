package taskmanager.servers.taskserver;

import taskmanager.managers.FileBackedTaskManager;
import taskmanager.tasks.Task;
import taskmanager.utilities.taskservices.TaskType;

import static taskmanager.utilities.taskservices.TaskType.*;


public class TaskDeleteEndPoints {

    static String getResponse(FileBackedTaskManager manager, String query, String path) {
        String[] splitStrings = path.split("/");
        if (path.equals("/tasks")) {
            manager.clearAllTasks();
            return "Все задачи удалены!";
        } else {
            if (query != null) {
                TaskType type = getType(splitStrings[2]);
                int id = Integer.parseInt(query.substring(3));
                Task task = manager.getTaskById(id, type);
                manager.removeTask(task);
                return "Задача с ID: " + id + " удалена!";
            } else {
                return "Ошибочка!";
            }
        }
    }

    private static TaskType getType(String type) {
        switch (type) {
            case "simple":
                return SIMPLE;
            case "epic":
                return EPIC;
            case "sub":
                return SUB;
            default:
                return null;
        }
    }
}
