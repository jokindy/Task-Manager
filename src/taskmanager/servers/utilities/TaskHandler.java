package taskmanager.servers.utilities;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import taskmanager.interfaces.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class TaskHandler implements HttpHandler {

    private final TaskManager manager;

    public TaskHandler(TaskManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(HttpExchange h) throws IOException {
        URI requestURI = h.getRequestURI();
        String path = requestURI.getPath();
        String query = requestURI.getQuery();
        String method = h.getRequestMethod();
        try {
            switch (method) {
                case "GET": {
                    String response = TaskGetEndPoints.getResponse(manager, query, path);
                    sendText(h, response);
                    break;
                }
                case "POST": {
                    String response = TaskPostEndPoints.getResponse(manager, h);
                    sendText(h, response);
                    break;
                }
                case "DELETE": {
                    String response = TaskDeleteEndPoints.getResponse(manager, path);
                    sendText(h, response);
                    break;
                }
                default:
                    h.sendResponseHeaders(405, 0);
                    break;
            }
        } finally {
            h.close();
        }
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        if (text.equals("Error")) {
            h.sendResponseHeaders(404, 0);
        } else if (text.equals("406")) {
            h.sendResponseHeaders(406, 0);
        } else {
            byte[] resp = text.getBytes(StandardCharsets.UTF_8);
            h.getResponseHeaders().add("Content-Type", "application/json");
            h.sendResponseHeaders(200, resp.length);
            h.getResponseBody().write(resp);
        }
    }


}
