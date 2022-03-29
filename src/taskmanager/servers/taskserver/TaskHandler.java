package taskmanager.servers.taskserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.managers.FileBackedTaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

public class TaskHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String response;
        FileBackedTaskManager manager = HttpTaskServer.getManager();
        String method = httpExchange.getRequestMethod();
        URI requestURI = httpExchange.getRequestURI();
        String path = requestURI.getPath();
        String query = requestURI.getQuery();
        InputStream body = httpExchange.getRequestBody();
        switch (method) {
            case "GET":
                response = TaskGetEndPoints.getResponse(manager, query, path);
                break;
            case "DELETE":
                response = TaskDeleteEndPoints.getResponse(manager, query, path);
                break;
            case "POST":
                response = TaskPostEndPoints.getResponse(manager, body, path);
                break;
            default:
                response = "WRONG";
        }
        httpExchange.sendResponseHeaders(200, 0);
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
