package taskmanager.servers.taskserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import taskmanager.managers.FileBackedTaskManager;
import taskmanager.tasks.*;
import taskmanager.utilities.converters.Converters;
import taskmanager.utilities.taskservices.TaskType;

import java.util.*;

import static taskmanager.utilities.taskservices.TaskType.EPIC;

public class TaskGetEndPoints {

    static String getResponse(FileBackedTaskManager manager, String query, String path) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        Gson gson = Converters.registerAll(gsonBuilder).create();
        boolean isEpicSubtask = false;
        String[] splitStrings = path.split("/");
        if (path.equals("/tasks")) {
            TreeMap<Task, Integer> map = manager.getPrioritizedTasks();
            return gson.toJson(map);
        } else {
            String type = splitStrings[2];
            if (splitStrings.length == 4) {
                isEpicSubtask = splitStrings[3].equals("epic");
            }
            if (query != null) {
                int id = Integer.parseInt(query.substring(3));
                Task task = manager.getTaskById(id, TaskType.valueOf(type.toUpperCase(Locale.ROOT)));
                if (isEpicSubtask) {
                    List<SubTask> list = ((EpicTask) manager.getTaskById(id, EPIC)).getListOfSubTasks();
                    return gson.toJson(list);
                } else {
                    if (task != null) {
                        return gson.toJson(task);
                    } else {
                        return "Такой задачи на сервере нет.";
                    }
                }
            } else if (type != null) {
                switch (type) {
                    case "simple":
                        List<Task> simpleTasks = manager.getListOfSimpleTasks();
                        return gson.toJson(simpleTasks);
                    case "epic":
                        List<EpicTask> list = manager.getListOfEpicTasks();
                        return gson.toJson(list);
                    case "history":
                        List<Task> history = manager.getHistory();
                        return gson.toJson(history);
                    default:
                        return "Неверная команда";
                }
            } else {
                return "Неверная команда";
            }
        }
    }
}
