package taskmanager.utilities.taskservices;

import taskmanager.tasks.Task;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TaskIdGenerator {

    private static int id = 0;
    private static final Set<Integer> listOfId = new HashSet<>();

    public static int generateID() {
        id++;
        while (listOfId.contains(id)) {
            id++;
        }
        return id;
    }

    public static void setListOfId(List<Task> list) {
        for (Task task : list) {
            listOfId.add(task.getId());
        }
    }
}
