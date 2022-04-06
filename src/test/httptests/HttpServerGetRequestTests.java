package test.httptests;

import org.junit.jupiter.api.*;
import taskmanager.tasks.Task;
import taskmanager.tasks.TaskDTO;
import test.httptests.testrequests.HttpServerGetRequests;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpServerGetRequestTests extends HttpTaskServerTest {

    @Order(1)
    @Test
    public void shouldGetAllTasksByRequest() {
        List<TaskDTO> list = new ArrayList<>();
        for (Task task : fileManager.getPrioritizedTasks().keySet()) {
            list.add(new TaskDTO(task));
        }
        String json = gson.toJson(list);
        HttpRequest request = HttpServerGetRequests.getAllTasksRequest();
        String anotherJson = getRequestBody(request);
        Assertions.assertEquals(json, anotherJson);
    }

    @Order(2)
    @Test
    public void shouldGetSimpleTasksByRequest() {
        String json = gson.toJson(fileManager.getListOfSimpleTasks());
        HttpRequest request = HttpServerGetRequests.getSimpleTasksRequest();
        String anotherJson = getRequestBody(request);
        Assertions.assertEquals(json, anotherJson);
    }

    @Order(3)
    @Test
    public void shouldGetEpicTasksByRequest() {
        String json = gson.toJson(fileManager.getListOfEpicTasks());
        HttpRequest request = HttpServerGetRequests.getEpicTasksRequest();
        String anotherJson = getRequestBody(request);
        Assertions.assertEquals(json, anotherJson);
    }

    @Order(4)
    @Test
    public void shouldGetEpicSubtasksByRequest() {
        String json = gson.toJson(fileManager.getEpicSubtasks(3));
        HttpRequest request = HttpServerGetRequests.getEpicSubtasksRequest();
        String anotherJson = getRequestBody(request);
        Assertions.assertEquals(json, anotherJson);
    }

    @Order(5)
    @Test
    public void shouldGetSimpleTaskByRequest() {
        TaskDTO task = new TaskDTO(fileManager.getTaskById(1).get());
        String json = gson.toJson(task);
        HttpRequest request = HttpServerGetRequests.getSimpleTaskRequest();
        String anotherJson = getRequestBody(request);
        Assertions.assertEquals(json, anotherJson);
    }

    @Order(6)
    @Test
    public void shouldGetEpicTaskByRequest() {
        TaskDTO task = new TaskDTO(fileManager.getTaskById(3).get());
        String json = gson.toJson(task);
        HttpRequest request = HttpServerGetRequests.getEpicTaskRequest();
        String anotherJson = getRequestBody(request);
        Assertions.assertEquals(json, anotherJson);
    }

    @Order(7)
    @Test
    public void shouldGetSubTaskByRequest() {
        TaskDTO task = new TaskDTO(fileManager.getTaskById(4).get());
        String json = gson.toJson(task);
        HttpRequest request = HttpServerGetRequests.getSubTaskRequest();
        String anotherJson = getRequestBody(request);
        Assertions.assertEquals(json, anotherJson);
    }

    @Order(8)
    @Test
    public void shouldGetHistoryRequest() {
        String json = gson.toJson(fileManager.getHistory());
        HttpRequest request = HttpServerGetRequests.getHistoryRequest();
        String anotherJson = getRequestBody(request);
        Assertions.assertEquals(json, anotherJson);
    }

    private String getRequestBody(HttpRequest request) {
        String anotherJson = null;
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                anotherJson = response.body();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return anotherJson;
    }
}
