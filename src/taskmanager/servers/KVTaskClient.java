package taskmanager.servers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final URI url;
    private final HttpClient httpClient;
    private final int API_KEY;

    public KVTaskClient(URI path, int key) {
        httpClient = HttpClient.newHttpClient();
        this.url = path;
        this.API_KEY = key;
    }

    public void save(String tasks, String key) {
        URI requestURI = URI.create(this.url + "/save/" + key + "?API_KEY=" + API_KEY);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(requestURI)
                .POST(HttpRequest.BodyPublishers.ofString(tasks))
                .build();
        try {
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String load(String key) {
        URI requestURI = URI.create(this.url + "/load/" + key + "?API_KEY=" + API_KEY);
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
                return "Error";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}

