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

import static taskmanager.utilities.taskservices.TaskType.*;

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
        fileManager = Managers.getFileManager(file);
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
        fileManager = Managers.getFileManager(file);
        Task task = fileManager.getTaskById(1, SIMPLE);
        Assertions.assertNull(task);
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
        fileManager = Managers.getFileManager(file);
        Task task = fileManager.getTaskById(3, EPIC);
        Assertions.assertNull(task);
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
        fileManager = Managers.getFileManager(file);
        Task task = fileManager.getTaskById(4, SUB);
        Assertions.assertNull(task);
    }
}
