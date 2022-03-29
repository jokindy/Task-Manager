package test.managers;

import org.junit.jupiter.api.*;
import taskmanager.managers.FileBackedTaskManager;
import taskmanager.tasks.*;

import java.io.File;
import java.util.List;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private static final File file = new File("resources\\test\\test.csv");

    @BeforeEach
    public void beforeEach() {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);
        fileManager.addTask(ManagerTestTasks.task1);
        fileManager.addTask(ManagerTestTasks.task2);
        fileManager.addTask(ManagerTestTasks.epicTask);
        fileManager.addTask(ManagerTestTasks.sub1);
        fileManager.addTask(ManagerTestTasks.sub2);
        setParameters(fileManager);
    }

    @AfterEach
    public void afterEach() {
        FileBackedTaskManager fileManager = new FileBackedTaskManager(file);
        fileManager.clearAllTasks();
        setParameters(fileManager);
    }

    @Order(8)
    @Test
    public void shouldReadTasksFromFile() {
        FileBackedTaskManager anotherManager = new FileBackedTaskManager(file);
        List<Task> list = anotherManager.getListOfSimpleTasks();
        List<EpicTask> epicTasks = anotherManager.getListOfEpicTasks();
        for (EpicTask epicTask : epicTasks) {
            list.add(epicTask);
            list.addAll(epicTask.getListOfSubTasks());
        }
        Assertions.assertEquals(ManagerTestTasks.listOfTasks, list);
    }

    @Order(9)
    @Test
    public void shouldReadHistoryFromFile() {
        File history = new File("resources\\test\\historyTest.csv");
        FileBackedTaskManager fileManager = new FileBackedTaskManager(history);
        List<Task> list = fileManager.getHistory();
        Assertions.assertEquals(ManagerTestTasks.history, list);
    }

    @Order(10)
    @Test
    public void shouldReadEmptyHistoryFromFile() {
        File file = new File("resources\\test\\emptyFile.csv");
        FileBackedTaskManager anotherManager = new FileBackedTaskManager(file);
        List<Task> list = anotherManager.getHistory();
        Assertions.assertTrue(list.isEmpty());
    }

    @Order(11)
    @Test
    public void shouldReadEmptyTasksFromFile() {
        File file = new File("resources\\test\\emptyFile.csv");
        FileBackedTaskManager anotherManager = new FileBackedTaskManager(file);
        boolean isEmpty = anotherManager.getListOfEpicTasks().isEmpty() &&
                anotherManager.getListOfSimpleTasks().isEmpty();
        Assertions.assertTrue(isEmpty);
    }
}
