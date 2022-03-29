package test.basetests;

import org.junit.jupiter.api.*;
import taskmanager.tasks.*;
import taskmanager.utilities.taskservices.TaskStatus;

import java.util.ArrayList;

import static taskmanager.utilities.taskservices.TaskStatus.*;

public class InMemoryTaskManagerEpicTest extends BaseTaskManager<EpicTask> {

    @BeforeEach
    public void beforeEach() {
        BaseTestTasks.epic.setListOfSubTasks(new ArrayList<>());
        BaseTestTasks.changedEpic.setListOfSubTasks(new ArrayList<>());
        setParameters(BaseTestTasks.epic, BaseTestTasks.changedEpic);
    }

    @Order(6)
    @Test
    public void shouldStatusBeNewWithEmptyListOfSubtasks() {
        TaskStatus status = BaseTestTasks.epic.getStatus();
        Assertions.assertEquals(NEW, status);
    }

    @Order(7)
    @Test
    public void shouldStatusBeNew() {
        manager.addTask(BaseTestTasks.epic);
        manager.addTask(BaseTestTasks.sub1);
        TaskStatus status = BaseTestTasks.epic.getStatus();
        Assertions.assertEquals(NEW, status);
    }

    @Order(8)
    @Test
    public void shouldStatusBeInProgress() {
        manager.addTask(BaseTestTasks.sub1);
        manager.addTask(BaseTestTasks.sub2);
        TaskStatus status = BaseTestTasks.epic.getStatus();
        Assertions.assertEquals(IN_PROGRESS, status);
    }

    @Order(9)
    @Test
    public void shouldStatusBeDone() {
        manager.addTask(BaseTestTasks.sub2);
        TaskStatus status = BaseTestTasks.epic.getStatus();
        Assertions.assertEquals(DONE, status);
    }
}
