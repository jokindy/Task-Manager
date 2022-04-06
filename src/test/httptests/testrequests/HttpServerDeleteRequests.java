package test.httptests.testrequests;

import java.net.URI;
import java.net.http.HttpRequest;

public class HttpServerDeleteRequests extends HttpServerGetRequests {

    private static HttpRequest makeRequest(URI url) {
        return HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();
    }

    public static HttpRequest removeAllTasksRequest() {
        URI requestURI = URI.create("http://localhost:8080/tasks");
        return makeRequest(requestURI);
    }

    public static HttpRequest removeSimpleTaskRequest() {
        URI requestURI = URI.create("http://localhost:8080/tasks/1");
        return makeRequest(requestURI);
    }

    public static HttpRequest removeEpicTaskRequest() {
        URI requestURI = URI.create("http://localhost:8080/tasks/3");
        return makeRequest(requestURI);
    }

    public static HttpRequest removeSubTaskRequest() {
        URI requestURI = URI.create("http://localhost:8080/tasks/4");
        return makeRequest(requestURI);
    }
}
