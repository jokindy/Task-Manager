package test.httptests;

import org.junit.jupiter.api.*;
import taskmanager.managers.Managers;
import taskmanager.tasks.*;
import test.httptests.testrequests.HttpServerPostRequests;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;

import static taskmanager.utilities.taskservices.TaskStatus.*;
import static taskmanager.utilities.taskservices.TaskType.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpServerPostRequestTests extends HttpTaskServerTest {

    @Order(1)
    @Test
    public void shouldPostSimpleTask() {
        Task task = new Task(10, "POST TASK", "CHECKING",
                IN_PROGRESS, Duration.ofHours(1), null);
        String json = gson.toJson(task);
        HttpRequest request = HttpServerPostRequests.postSimpleTaskRequest(json);
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        fileManager = Managers.getFileManager(file);
        Task addedTask = fileManager.getTaskById(10, SIMPLE);
        Assertions.assertEquals(task, addedTask);
    }

    @Order(2)
    @Test
    public void shouldPostEpicTask() {
        EpicTask task = new EpicTask(7, "POST EPIC TASK", "CHECKING",
                NEW, Duration.ZERO, null);
        task.setListOfSubTasks(new ArrayList<>());
        String json = gson.toJson(task).replace("\"listOfSubTasks\":[],", "");
        HttpRequest request = HttpServerPostRequests.postEpicTaskRequest(json);
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        fileManager = Managers.getFileManager(file);
        Task addedTask = fileManager.getTaskById(7, EPIC);
        Assertions.assertEquals(task, addedTask);
    }

    @Order(3)
    @Test
    public void shouldPostSubTask() {
        SubTask task = new SubTask(7, "CAT", "POST SUB TASK",
                NEW, Duration.ZERO, null);
        String json = gson.toJson(task);
        HttpRequest request = HttpServerPostRequests.postSubTaskRequest(json);
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        fileManager = Managers.getFileManager(file);
        Task addedTask = fileManager.getTaskById(7, SUB);
        Assertions.assertEquals(task, addedTask);
    }

    @Order(4)
    @Test
    public void shouldPostEditTask() {
        Task task = fileManager.getTaskById(1, SIMPLE);
        task.setDescription("NEW DESCRIPTION");
        String json = gson.toJson(task);
        HttpRequest request = HttpServerPostRequests.postSimpleTaskRequest(json);
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        fileManager = Managers.getFileManager(file);
        Task changedTask = fileManager.getTaskById(1, SIMPLE);
        Assertions.assertEquals(task, changedTask);
    }
}
