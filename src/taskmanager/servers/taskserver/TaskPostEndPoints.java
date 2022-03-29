package taskmanager.servers.taskserver;

import taskmanager.managers.FileBackedTaskManager;
import taskmanager.tasks.Task;
import taskmanager.utilities.taskservices.TaskCreator;
import taskmanager.utilities.taskservices.TaskType;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static taskmanager.utilities.taskservices.TaskType.*;

public class TaskPostEndPoints {

    static String getResponse(FileBackedTaskManager manager, InputStream body, String path) {
        String text = new BufferedReader(
                new InputStreamReader(body, StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        Task taskNew = TaskCreator.bodyToTask(text);
        int id = taskNew.getId();
        TaskType type = taskNew.getType();
        Task taskOld = manager.getTaskById(id, type);
        if (taskOld == null) {
            if (path.equals("/tasks/simple") && type.equals(SIMPLE)) {
                manager.addTask(taskNew);
                return "Задача успешно добавлена! Идентификатор: " + id;
            } else if (path.equals("/tasks/epic") && type.equals(EPIC)) {
                manager.addTask(taskNew);
                return "Задача успешно добавлена! Идентификатор: " + id;
            } else if (path.equals("/tasks/sub") && type.equals(SUB)) {
                manager.addTask(taskNew);
                return "Задача успешно добавлена! Идентификатор: " + id;
            } else {
                return "Задача не добавлена. Проверьте правильность пути и данные задачи";
            }
        } else if (!taskOld.equals(taskNew)) {
            manager.editTask(taskOld, taskNew);
            return "Задача успешно обновлена";
        } else {
            return "Вы пытаетесь добавить уже существующую задачу";
        }
    }
}
