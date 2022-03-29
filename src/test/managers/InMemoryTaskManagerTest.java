package test.managers;

import org.junit.jupiter.api.*;

import taskmanager.interfaces.TaskManager;
import taskmanager.managers.InMemoryTaskManager;
import taskmanager.managers.Managers;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void beforeEach() {
        TaskManager manager = Managers.getInMemoryTaskManager();
        manager.addTask(ManagerTestTasks.task1);
        manager.addTask(ManagerTestTasks.task2);
        manager.addTask(ManagerTestTasks.epicTask);
        manager.addTask(ManagerTestTasks.sub1);
        manager.addTask(ManagerTestTasks.sub2);
        setParameters((InMemoryTaskManager) manager);
    }
}