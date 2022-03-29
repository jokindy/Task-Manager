package test.tasks;

import org.junit.jupiter.api.*;
import taskmanager.tasks.*;
import taskmanager.utilities.taskservices.TaskStatus;

import java.time.Duration;
import java.util.ArrayList;

import static taskmanager.utilities.taskservices.TaskStatus.*;


class EpicTaskTest {

    private EpicTask epicTask;

    private final SubTask subTask1 = new SubTask(2, "EPIC-1", "SUB-1",
            NEW, Duration.ofHours(10), null);
    private final SubTask subTask2 = new SubTask(3, "EPIC-1", "SUB-2",
            NEW, Duration.ofHours(5), null);
    private final SubTask subTask3 = new SubTask(4, "EPIC-1", "SUB-3",
            DONE, Duration.ofHours(5), null);
    private final SubTask subTask4 = new SubTask(5, "EPIC-1", "SUB-4",
            DONE, Duration.ofHours(5), null);
    private final SubTask subTask5 = new SubTask(6, "EPIC-1", "SUB-5",
            IN_PROGRESS, Duration.ofHours(5), null);
    private final SubTask subTask6 = new SubTask(7, "EPIC-1", "SUB-6",
            IN_PROGRESS, Duration.ofHours(5), null);

    @BeforeEach
    public void beforeEach() {
        epicTask = new EpicTask(1, "EPIC-1", "TEST",
                null, Duration.ZERO, null);
        epicTask.setListOfSubTasks(new ArrayList<>());
    }

    @Test
    public void shouldBeNewWithEmptyListOfSubtasks() {
        TaskStatus status = epicTask.getStatus();
        Assertions.assertEquals(NEW, status);
    }

    @Test
    public void shouldBeNewWithNewSubtasks() {
        epicTask.getListOfSubTasks().add(subTask1);
        epicTask.getListOfSubTasks().add(subTask2);
        TaskStatus status = epicTask.getStatus();
        Assertions.assertEquals(NEW, status);
    }

    @Test
    public void shouldBeDoneWithDoneSubtasks() {
        epicTask.getListOfSubTasks().add(subTask3);
        epicTask.getListOfSubTasks().add(subTask4);
        TaskStatus status = epicTask.getStatus();
        Assertions.assertEquals(DONE, status);
    }

    @Test
    public void shouldBeInProgressWithDifferentSubtasks() {
        epicTask.getListOfSubTasks().add(subTask1);
        epicTask.getListOfSubTasks().add(subTask3);
        TaskStatus status = epicTask.getStatus();
        Assertions.assertEquals(IN_PROGRESS, status);
    }

    @Test
    public void shouldBeInProgressWithInProgressSubtasks() {
        epicTask.getListOfSubTasks().add(subTask5);
        epicTask.getListOfSubTasks().add(subTask6);
        TaskStatus status = epicTask.getStatus();
        Assertions.assertEquals(IN_PROGRESS, status);
    }
}