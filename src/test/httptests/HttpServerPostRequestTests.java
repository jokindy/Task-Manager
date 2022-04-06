package test.httptests;

import org.junit.jupiter.api.*;
import taskmanager.managers.Managers;
import taskmanager.tasks.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static taskmanager.utilities.taskservices.TaskStatus.IN_PROGRESS;
import static taskmanager.utilities.taskservices.TaskStatus.NEW;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpServerPostRequestTests extends HttpTaskServerTest {

    @Order(1)
    @Test
    public void shouldPostSimpleTask() {
        Task task = new Task(10, "POST TASK", "CHECKING",
                IN_PROGRESS, Duration.ofHours(1), null);
        TaskDTO taskDTO = new TaskDTO(task);
        String json = gson.toJson(taskDTO);
        HttpRequest request = makePostRequest(json);
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        fileManager = Managers.getFileManager(path);
        Task addedTask = fileManager.getTaskById(10).get();
        Assertions.assertEquals(task, addedTask);
    }

    @Order(2)
    @Test
    public void shouldPostEpicTask() {
        EpicTask task = new EpicTask(7, "POST EPIC TASK", "CHECKING",
                NEW, Duration.ZERO, null);
        TaskDTO taskDTO = new TaskDTO(task);
        String json = gson.toJson(taskDTO);
        HttpRequest request = makePostRequest(json);
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        fileManager = Managers.getFileManager(path);
        Task addedTask = fileManager.getTaskById(7).get();
        Assertions.assertEquals(task, addedTask);
    }

    @Order(3)
    @Test
    public void shouldPostSubTask() {
        SubTask task = new SubTask(7, "CAT", "POST SUB TASK",
                NEW, Duration.ZERO, null, 3);
        TaskDTO taskDTO = new TaskDTO(task);
        String json = gson.toJson(taskDTO);
        HttpRequest request = makePostRequest(json);
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        fileManager = Managers.getFileManager(path);
        Task addedTask = fileManager.getTaskById(7).get();
        Assertions.assertEquals(task, addedTask);
    }

    @Order(4)
    @Test
    public void shouldPostEditTask() {
        Task task = fileManager.getTaskById(1).get();
        task.setDescription("NEW DESCRIPTION");
        TaskDTO taskDTO = new TaskDTO(task);
        String json = gson.toJson(taskDTO);
        HttpRequest request = makePostRequest(json);
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        fileManager = Managers.getFileManager(path);
        Task changedTask = fileManager.getTaskById(1).get();
        Assertions.assertEquals(task, changedTask);
    }

    private  HttpRequest makePostRequest(String json) {
        return HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }
}
