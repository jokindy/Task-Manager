package taskmanager.servers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class KVServer {
    public static final int PORT = 8079;
    private final String API_KEY;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        API_KEY = getApiKey();
        server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        server.createContext("/register", (h) -> {
            try {
                System.out.println("\n/register");
                if ("GET".equals(h.getRequestMethod())) {
                    sendText(h, "API_KEY: " + API_KEY);
                } else {
                    System.out.println("/register ждёт GET-запрос, а получил " + h.getRequestMethod());
                    h.sendResponseHeaders(405, 0);
                }
            } finally {
                h.close();
            }
        });
        server.createContext("/save", (h) -> {
            try {
                if (hasAuth(h)) {
                    return;
                }
                handlePostRequest(h);
            } finally {
                h.close();
            }
        });
        server.createContext("/load", (h) -> {
            try {
                if (hasAuth(h)) {
                    return;
                }
                handleGetRequest(h);
            } finally {
                h.close();
            }
        });
    }

    private String getApiKey() {
        Random random = new Random();
        int key = random.nextInt(10000);
        return String.valueOf(key);
    }

    public void start() {
        System.out.println("Запускаем KV-сервер на порту " + PORT);
        server.start();
    }

    public void close() {
        server.stop(0);
    }

    public URI getAddress() {
        return URI.create("http://localhost:" + PORT);
    }

    protected boolean hasAuth(HttpExchange h) throws IOException {
        String rawQuery = h.getRequestURI().getRawQuery();
        boolean isAuth = rawQuery == null || (!rawQuery.contains("API_KEY=" + API_KEY));
        if (isAuth) {
            System.out.println("Запрос не авторизован, нужен параметр в query API_KEY со значением апи-ключа");
            h.sendResponseHeaders(403, 0);
            return true;
        } else {
            return false;
        }
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    private void handlePostRequest(HttpExchange h) throws IOException {
        if ("POST".equals(h.getRequestMethod())) {
            String key = h.getRequestURI().getPath().substring("/save/".length());
            if (key.isEmpty()) {
                System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                h.sendResponseHeaders(400, 0);
                return;
            }
            String value = readText(h);
            if (value.isEmpty()) {
                System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                h.sendResponseHeaders(400, 0);
                return;
            }
            data.put(key, value);
            System.out.println("Данные сохранены! Key - " + key);
            h.sendResponseHeaders(200, 0);
        } else {
            System.out.println("/save ждёт POST-запрос, а получил: " + h.getRequestMethod());
            h.sendResponseHeaders(405, 0);
        }
    }

    private void handleGetRequest(HttpExchange h) throws IOException {
        if ("GET".equals(h.getRequestMethod())) {
            String key = h.getRequestURI().getPath().substring("/save/".length());
            if (key.isEmpty()) {
                System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                h.sendResponseHeaders(400, 0);
                return;
            }
            if (!data.containsKey(key)) {
                System.out.println("Не могу достать данные для ключа '" + key + "', данные отсутствуют");
                h.sendResponseHeaders(404, 0);
                return;
            }
            sendText(h, data.get(key));
            h.sendResponseHeaders(200, 0);
        } else {
            System.out.println("/save ждёт GET-запрос, а получил: " + h.getRequestMethod());
            h.sendResponseHeaders(405, 0);
        }
    }
}
