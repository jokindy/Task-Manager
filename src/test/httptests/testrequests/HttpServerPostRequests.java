package test.httptests.testrequests;

import java.net.URI;
import java.net.http.HttpRequest;

public class HttpServerPostRequests {

    private static HttpRequest makePostRequest(URI url, String json) {
        return HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    public static HttpRequest postSimpleTaskRequest(String json) {
        URI requestURI = URI.create("http://localhost:8080/tasks/simple");
        return makePostRequest(requestURI, json);
    }

    public static HttpRequest postEpicTaskRequest(String json) {
        URI requestURI = URI.create("http://localhost:8080/tasks/epic");
        return makePostRequest(requestURI, json);
    }

    public static HttpRequest postSubTaskRequest(String json) {
        URI requestURI = URI.create("http://localhost:8080/tasks/sub");
        return makePostRequest(requestURI, json);
    }
}
