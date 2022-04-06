package taskmanager.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import taskmanager.servers.KVTaskClient;
import taskmanager.tasks.Task;
import taskmanager.tasks.TaskDTO;
import taskmanager.utilities.converters.Converters;
import taskmanager.utilities.taskservices.TaskCreator;

import java.util.*;

public class HttpTaskManager extends FileBackedTaskManager {

    private final KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(KVTaskClient client) {
        this.client = client;
        gson = Converters.registerAll(new GsonBuilder().serializeNulls()).create();
        load();
    }

    @Override
    public void save() {
        TreeMap<Task, Integer> map = getPrioritizedTasks();
        List<TaskDTO> list = new ArrayList<>();
        for (Task task : map.keySet()) {
            list.add(new TaskDTO(task));
        }
        String jsonTasks = gson.toJson(list);
        String jsonHistory = gson.toJson(getHistory());
        client.save(jsonTasks, "tasks");
        client.save(jsonHistory, "history");
    }

    @Override
    public void load() {
        try {
            String jsonHistory = client.load("history");
            String jsonTasks = client.load("tasks");
            String[] splitTasks = jsonTasks.split("}");
            List<Task> listOfTasks = new ArrayList<>();
            for (int i = 0; i < splitTasks.length - 1; i++) {
                Task task = TaskCreator.bodyToTask(splitTasks[i].substring(1));
                listOfTasks.add(task);
                super.addTask(task);
            }
            super.fillHistory(listOfTasks, jsonHistory);
            jsonHistory = gson.toJson(getHistory());
            client.save(jsonHistory, "history");
        } catch (NullPointerException e) {
            System.out.println("Сервер пуст");
        }
    }

}
