package taskmanager.servers.utilities;

import com.sun.net.httpserver.HttpExchange;
import taskmanager.interfaces.TaskManager;
import taskmanager.tasks.Task;
import taskmanager.utilities.taskservices.TaskCreator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskPostEndPoints {

    static String getResponse(TaskManager manager, HttpExchange h) {
        String text = new BufferedReader(
                new InputStreamReader(h.getRequestBody(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        Task taskNew = TaskCreator.bodyToTask(text);
        int id = taskNew.getId();
        Optional<Task> task = manager.getTaskById(id);
        if (task.isEmpty()) {
            manager.addTask(taskNew);
            System.out.println("Задача успешно добавлена. Идентификатор: " + id);
            return "Added!";
        } else if (task.get().equals(taskNew)) {
            System.out.println("Пытаются добавить уже существующую задачу.");
            return "406";
        } else {
            manager.editTask(task.get(), taskNew);
            System.out.println("Задача успешно обновлена. Идентификатор: " + id);
            return "Updated!";
        }
    }
}
