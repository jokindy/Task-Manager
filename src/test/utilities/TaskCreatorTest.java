package test.utilities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import taskmanager.tasks.Task;
import taskmanager.utilities.taskservices.TaskCreator;

import java.time.Duration;
import java.time.LocalDateTime;

import static taskmanager.utilities.taskservices.TaskStatus.NEW;

public class TaskCreatorTest {

    @Test
    public void shouldCreateTaskFromString() {
        String text = "1;SIMPLE;SIMPLE-1;TEST;NEW;0;10.04.2022.12.35";
        Task testTask = new Task(1, "SIMPLE-1", "TEST",
                NEW, Duration.ZERO, LocalDateTime.of(2022, 4, 10, 12, 35));
        Task task = TaskCreator.stringToTask(text);
        Assertions.assertEquals(testTask, task);
    }
}
