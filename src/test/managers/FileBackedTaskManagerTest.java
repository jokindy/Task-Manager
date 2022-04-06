package test.managers;

import org.junit.jupiter.api.*;
import taskmanager.managers.FileBackedTaskManager;
import taskmanager.tasks.EpicTask;
import taskmanager.tasks.Task;

import java.util.List;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private static final String path = "resources\\test\\test.csv";

    @BeforeEach
    public void beforeEach() {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(path);
        fileManager.addTask(ManagerTestTasks.task1);
        fileManager.addTask(ManagerTestTasks.task2);
        fileManager.addTask(ManagerTestTasks.epicTask);
        fileManager.addTask(ManagerTestTasks.sub1);
        fileManager.addTask(ManagerTestTasks.sub2);
        setParameters(fileManager);
    }

    @AfterEach
    public void afterEach() {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(path);
        fileManager.clearAllTasks();
        setParameters(fileManager);
    }

    @Order(8)
    @Test
    public void shouldReadTasksFromFile() {
        FileBackedTaskManager anotherManager = new FileBackedTaskManager(path);
        List<Task> list = anotherManager.getListOfSimpleTasks();
        List<Task> epicTasks = anotherManager.getListOfEpicTasks();
        for (Task epicTask : epicTasks) {
            list.add(epicTask);
            List<Task> listOfSubTasks = ((EpicTask) epicTask).getListOfSubTasks();
            list.addAll(listOfSubTasks);
        }
        Assertions.assertEquals(ManagerTestTasks.listOfTasks, list);
    }

    @Order(9)
    @Test
    public void shouldReadHistoryFromFile() {
        FileBackedTaskManager anotherManager = new FileBackedTaskManager("resources\\test\\historyTest.csv");
        List<Task> list = anotherManager.getHistory();
        Assertions.assertEquals(ManagerTestTasks.history, list);
    }

    @Order(10)
    @Test
    public void shouldReadEmptyHistoryFromFile() {
        FileBackedTaskManager anotherManager = new FileBackedTaskManager("resources\\test\\emptyFile.csv");
        List<Task> list = anotherManager.getHistory();
        Assertions.assertTrue(list.isEmpty());
    }

    @Order(11)
    @Test
    public void shouldReadEmptyTasksFromFile() {
        FileBackedTaskManager anotherManager = new FileBackedTaskManager("resources\\test\\emptyFile.csv");
        boolean isEmpty = anotherManager.getListOfEpicTasks().isEmpty() &&
                anotherManager.getListOfSimpleTasks().isEmpty();
        Assertions.assertTrue(isEmpty);
    }
}
