package test.httptests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import taskmanager.managers.Managers;
import taskmanager.tasks.Task;
import test.httptests.testrequests.HttpServerDeleteRequests;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

public class HttpServerDeleteRequestTest extends HttpTaskServerTest {

    @Order(1)
    @Test
    public void shouldDeleteAllTasksByRequest() {
        HttpRequest request = HttpServerDeleteRequests.removeAllTasksRequest();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        fileManager = Managers.getFileManager(path);
        boolean isClear = fileManager.getListOfSimpleTasks().isEmpty() &&
                fileManager.getListOfEpicTasks().isEmpty();
        Assertions.assertTrue(isClear);
    }

    @Order(2)
    @Test
    public void shouldDeleteSimpleTaskByRequest() {
        HttpRequest request = HttpServerDeleteRequests.removeSimpleTaskRequest();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        fileManager = Managers.getFileManager(path);
        Optional<Task> task = fileManager.getTaskById(1);
        Assertions.assertTrue(task.isEmpty());
    }

    @Order(3)
    @Test
    public void shouldRemoveEpicTaskByRequest() {
        HttpRequest request = HttpServerDeleteRequests.removeEpicTaskRequest();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        fileManager = Managers.getFileManager(path);
        Optional<Task> task = fileManager.getTaskById(3);
        Assertions.assertTrue(task.isEmpty());
    }

    @Order(4)
    @Test
    public void shouldRemoveSubTaskByRequest() {
        HttpRequest request = HttpServerDeleteRequests.removeSubTaskRequest();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        fileManager = Managers.getFileManager(path);
        Optional<Task> task = fileManager.getTaskById(4);
        Assertions.assertTrue(task.isEmpty());
    }
}
