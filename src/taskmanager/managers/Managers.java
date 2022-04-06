package taskmanager.managers;

import taskmanager.interfaces.TaskManager;
import taskmanager.servers.KVTaskClient;

public class Managers {

    public static TaskManager getDefault(KVTaskClient client) {
        return new HttpTaskManager(client);
    }

    public static TaskManager getInMemoryTaskManager() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager getFileManager(String path) {
        return new FileBackedTaskManager(path);
    }
}
