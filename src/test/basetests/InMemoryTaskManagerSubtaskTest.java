package test.basetests;

import org.junit.jupiter.api.*;
import taskmanager.tasks.SubTask;
import taskmanager.utilities.taskservices.TaskType;

import java.util.ArrayList;

public class InMemoryTaskManagerSubtaskTest extends BaseTaskManager<SubTask> {

    @BeforeAll
    static void beforeAll() {
        BaseTestTasks.epic.setListOfSubTasks(new ArrayList<>());
        setEpicTask(BaseTestTasks.epic);
    }

    @BeforeEach
    public void beforeEach() {
        setParameters(BaseTestTasks.sub1, BaseTestTasks.changedSub);
    }

    @Order(6)
    @Test
    public void shouldAddSubtaskToNonExistentEpic() {
        manager.addTask(BaseTestTasks.anotherSub);
        SubTask findTask = (SubTask) manager.getTaskById(BaseTestTasks.anotherSub.getId(),
                TaskType.SUB);
        Assertions.assertNull(findTask);
    }
}
