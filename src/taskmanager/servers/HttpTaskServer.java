package taskmanager.servers;

import com.sun.net.httpserver.HttpServer;
import taskmanager.managers.FileBackedTaskManager;
import taskmanager.managers.Managers;
import taskmanager.servers.utilities.TaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private static FileBackedTaskManager manager;
    private final HttpServer httpServer;


    public HttpTaskServer(String path) throws IOException {
        manager = Managers.getFileManager(path);
        httpServer = HttpServer.create();
    }

    public void start() throws IOException {
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(manager));
        httpServer.start();
        System.out.println("Рабочий сервер открыт на порту " + PORT + ". Можете отправлять запросы через Insomnia!");
    }

    public void close() {
        httpServer.stop(0);
    }
}
