package test.httptests;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import taskmanager.managers.FileBackedTaskManager;
import taskmanager.servers.taskserver.HttpTaskServer;
import taskmanager.utilities.converters.Converters;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HttpTaskServerTest {

    protected static File file = new File("resources\\test\\http\\httptest.csv");
    private final File buffer = new File("resources\\test\\http\\buffer.csv");
    private HttpTaskServer server;
    protected FileBackedTaskManager fileManager;
    protected HttpClient client;
    protected Gson gson;

    @BeforeEach
    public void beforeEach() throws IOException {
        server = new HttpTaskServer(file);
        server.start();
        client = HttpClient.newHttpClient();
        fileManager = new FileBackedTaskManager(file);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        gson = Converters.registerAll(gsonBuilder).create();
    }

    @AfterEach
    public void afterEach() throws IOException {
        server.close();
        FileWriter fileWriter = new FileWriter(file, false);
        List<String> list = Files.readAllLines(Path.of(buffer.getPath()));
        for (String s : list) {
            fileWriter.write(s + "\n");
        }
        fileWriter.close();
    }
}
