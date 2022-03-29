package taskmanager.servers.taskserver;

import com.sun.net.httpserver.HttpServer;
import taskmanager.managers.FileBackedTaskManager;
import taskmanager.managers.Managers;
import taskmanager.servers.KVServer;
import taskmanager.tasks.Task;
import taskmanager.utilities.taskservices.TaskCreator;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static FileBackedTaskManager manager;
    private final HttpServer httpServer;
    private final KVServer kvServer;


    public HttpTaskServer(File file) throws IOException {
        manager = Managers.getFileManager(file);
        httpServer = HttpServer.create();
        kvServer = new KVServer();
    }

    public void start() throws IOException {
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler());
        httpServer.start();
        kvServer.start();
        System.out.println("Рабочий сервер открыт на порту " + PORT + ". Можете отправлять запросы через Insomnia!");
        System.out.println("Если Вы хотите сохранить или загрузить данные на KV-сервер введите в консоль kv");
        System.out.println("Для выхода и закрытия серверов введите в консоль exit");
    }

    public URI getKVServerURI() {
        return kvServer.getAddress();
    }

    public String getManagerAsJson(HttpClient httpClient) {
        URI requestURI = URI.create("http://localhost:" + PORT + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(requestURI)
                .GET()
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request,
                    HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return response.body();
            } else {
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setManagerByJson(String json) {
        manager.clearAllTasks();
        String[] splitTasks = json.split("\"");
        for (int i = 0; i < splitTasks.length; i++) {
            if (i % 2 == 1) {
                Task task = TaskCreator.bodyToTask(splitTasks[i]);
                manager.addTask(task);
            }
        }
    }

    public void close() {
        httpServer.stop(0);
        kvServer.close();
    }

    protected static FileBackedTaskManager getManager() {
        return manager;
    }
}
