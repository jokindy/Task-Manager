package taskmanager.servers.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import taskmanager.interfaces.TaskManager;
import taskmanager.tasks.Task;
import taskmanager.tasks.TaskDTO;
import taskmanager.utilities.converters.Converters;

import java.util.*;

public class TaskGetEndPoints {

    static GsonBuilder gsonBuilder = new GsonBuilder().serializeNulls();
    static Gson gson = Converters.registerAll(gsonBuilder).create();

    static String getResponse(TaskManager manager, String query, String path) {
        if (path.equals("/tasks")) {
            TreeMap<Task, Integer> map = manager.getPrioritizedTasks();
            List<TaskDTO> list = new ArrayList<>();
            for (Task task : map.keySet()) {
                list.add(new TaskDTO(task));
            }
            return gson.toJson(list);
        } else {
            String[] splitStrings = path.replace(" ", "").split("/");
            String type = splitStrings[2];
            try {
                int id = Integer.parseInt(type);
                Optional<Task> task = manager.getTaskById(id);
                if (task.isPresent()) {
                    TaskDTO t = new TaskDTO(task.get());
                    System.out.println(gson.toJson(t));
                    return gson.toJson(t);
                } else {
                    return "Error";
                }
            } catch (NumberFormatException ignored) {
            }
            if (query != null && type.equals("epic")) {
                return processWithQuery(manager, query);
            } else if (type != null && query == null) {
                return processWithType(manager, type);
            } else {
                return "Error";
            }
        }
    }

    private static String processWithQuery(TaskManager manager, String query) {
        String[] splitStrings = query.split("&");
        int id = Integer.parseInt(splitStrings[0].substring(3));
        String filter = splitStrings[1].substring(7);
        System.out.println(id);
        System.out.println(filter);
        if (filter.equals("sub")) {
            List<Task> list = manager.getEpicSubtasks(id);
            return gson.toJson(list);
        } else {
            return "Error";
        }
    }

    private static String processWithType(TaskManager manager, String type) {
        switch (type) {
            case "simple":
                List<Task> simpleTasks = manager.getListOfSimpleTasks();
                return gson.toJson(simpleTasks);
            case "epic":
                List<Task> list = manager.getListOfEpicTasks();
                return gson.toJson(list);
            case "history":
                List<Task> history = manager.getHistory();
                return gson.toJson(history);
            default:
                return "Error";
        }
    }
}
