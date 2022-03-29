package test.basetests;

import org.junit.jupiter.api.BeforeEach;
import taskmanager.tasks.Task;

public class InMemoryTaskManagerSimpleTest extends BaseTaskManager<Task> {

    @BeforeEach
    public void beforeEach() {
        setParameters(BaseTestTasks.task, BaseTestTasks.changedTask);
    }
}
