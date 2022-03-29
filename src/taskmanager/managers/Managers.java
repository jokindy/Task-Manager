package taskmanager.managers;

import taskmanager.interfaces.TaskManager;

import java.io.File;
import java.net.URI;

public class Managers {

    public static TaskManager getDefault(File file, URI url) {
        return new HttpTaskManager(file, url);
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager getFileManager(File file) {
        return new FileBackedTaskManager(file);
    }
}
