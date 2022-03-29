package taskmanager.managers;

import taskmanager.servers.KVTaskClient;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;

public class HttpTaskManager extends FileBackedTaskManager {

    private final KVTaskClient client;

    public HttpTaskManager(File file, URI url) {
        super(file);
        this.client = new KVTaskClient(url);
    }

    public void saveManager(String key, int api_key, String json) {
        client.saveManager(key, api_key, json);
    }

    public String loadManager(String key, int apiKey) {
        return client.loadManager(key, apiKey);
    }

    public HttpClient getClient() {
        return client.getHttpClient();
    }
}
