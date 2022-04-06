package test.basetests;

import org.junit.jupiter.api.*;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.util.Optional;

public class InMemoryTaskManagerSubtaskTest extends BaseTaskManager<SubTask> {

    @BeforeAll
    static void beforeAll() {
        setEpicTask(BaseTestTasks.epic);
    }

    @BeforeEach
    public void beforeEach() {
        setParameters(BaseTestTasks.sub1, BaseTestTasks.changedSub);
    }

    @Order(6)
    @Test
    public void shouldAddSubtaskToNonExistentEpic() {
        int anotherId = BaseTestTasks.anotherSub.getEpicId();
        boolean isContains = manager.checkTask(anotherId);
        if (isContains) {
            manager.addTask(BaseTestTasks.anotherSub);
        }
        Optional<Task> receivedTask = manager.getTaskById(BaseTestTasks.anotherSub.getId());
        Assertions.assertTrue(receivedTask.isEmpty());
    }
}
