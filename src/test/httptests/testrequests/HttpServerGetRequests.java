package test.httptests.testrequests;

import java.net.URI;
import java.net.http.HttpRequest;

public class HttpServerGetRequests {

    private static HttpRequest makeRequest(URI url) {
        return HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();
    }

    public static HttpRequest getAllTasksRequest() {
        URI requestURI = URI.create("http://localhost:8080/tasks");
        return makeRequest(requestURI);
    }

    public static HttpRequest getSimpleTasksRequest() {
        URI requestURI = URI.create("http://localhost:8080/tasks/simple");
        return makeRequest(requestURI);
    }

    public static HttpRequest getEpicTasksRequest() {
        URI requestURI = URI.create("http://localhost:8080/tasks/epic");
        return makeRequest(requestURI);
    }

    public static HttpRequest getEpicSubtasksRequest() {
        URI requestURI = URI.create("http://localhost:8080/tasks/epic?id=3&filter=sub");
        return makeRequest(requestURI);
    }

    public static HttpRequest getSimpleTaskRequest() {
        URI requestURI = URI.create("http://localhost:8080/tasks/1");
        return makeRequest(requestURI);
    }

    public static HttpRequest getEpicTaskRequest() {
        URI requestURI = URI.create("http://localhost:8080/tasks/3");
        return makeRequest(requestURI);
    }

    public static HttpRequest getSubTaskRequest() {
        URI requestURI = URI.create("http://localhost:8080/tasks/4");
        return makeRequest(requestURI);
    }

    public static HttpRequest getHistoryRequest() {
        URI requestURI = URI.create("http://localhost:8080/tasks/history");
        return makeRequest(requestURI);
    }
}
