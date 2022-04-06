package test.tasks;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskmanager.managers.TaskRepository;
import taskmanager.tasks.EpicTask;
import taskmanager.tasks.SubTask;
import taskmanager.utilities.taskservices.TaskStatus;

import java.time.Duration;

import static taskmanager.utilities.taskservices.TaskStatus.*;


class EpicTaskTest {

    private TaskRepository repository;

    private EpicTask epicTask;

    private final SubTask subTask1 = new SubTask(2, "EPIC-1", "SUB-1",
            NEW, Duration.ofHours(10), null, 1);
    private final SubTask subTask2 = new SubTask(3, "EPIC-1", "SUB-2",
            NEW, Duration.ofHours(5), null, 1);
    private final SubTask subTask3 = new SubTask(4, "EPIC-1", "SUB-3",
            DONE, Duration.ofHours(5), null, 1);
    private final SubTask subTask4 = new SubTask(5, "EPIC-1", "SUB-4",
            DONE, Duration.ofHours(5), null, 1);
    private final SubTask subTask5 = new SubTask(6, "EPIC-1", "SUB-5",
            IN_PROGRESS, Duration.ofHours(5), null, 1);
    private final SubTask subTask6 = new SubTask(7, "EPIC-1", "SUB-6",
            IN_PROGRESS, Duration.ofHours(5), null, 1);

    @BeforeEach
    public void beforeEach() {
        repository = new TaskRepository();
        epicTask = new EpicTask(1, "EPIC-1", "TEST",
                null, Duration.ZERO, null);
        repository.add(epicTask);

    }

    @Test
    public void shouldBeNewWithEmptyListOfSubtasks() {
        TaskStatus status = epicTask.getStatus();
        Assertions.assertEquals(NEW, status);
    }

    @Test
    public void shouldBeNewWithNewSubtasks() {
        repository.add(subTask1);
        repository.add(subTask2);
        TaskStatus status = epicTask.getStatus();
        Assertions.assertEquals(NEW, status);
    }

    @Test
    public void shouldBeDoneWithDoneSubtasks() {
        repository.add(subTask3);
        repository.add(subTask4);
        TaskStatus status = epicTask.getStatus();
        Assertions.assertEquals(DONE, status);
    }

    @Test
    public void shouldBeInProgressWithDifferentSubtasks() {
        repository.add(subTask1);
        repository.add(subTask3);
        TaskStatus status = epicTask.getStatus();
        Assertions.assertEquals(IN_PROGRESS, status);
    }

    @Test
    public void shouldBeInProgressWithInProgressSubtasks() {
        repository.add(subTask5);
        repository.add(subTask6);
        TaskStatus status = epicTask.getStatus();
        Assertions.assertEquals(IN_PROGRESS, status);
    }
}