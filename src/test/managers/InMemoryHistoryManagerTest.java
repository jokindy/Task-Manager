package test.managers;

import org.junit.jupiter.api.*;
import taskmanager.interfaces.HistoryManager;
import taskmanager.managers.InMemoryHistoryManager;
import taskmanager.tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static taskmanager.utilities.taskservices.TaskStatus.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InMemoryHistoryManagerTest {

    private static final HistoryManager manager = new InMemoryHistoryManager();
    private final Task task1 = new Task(1, "SIMPLE-1", "TEST",
            DONE, Duration.ZERO, LocalDateTime.of(2022, 10, 3, 10, 0));
    private final Task task2 = new Task(2, "SIMPLE-2", "TEST",
            IN_PROGRESS, Duration.ofDays(2), LocalDateTime.of(2022, 8, 3, 10, 0));

    @Order(1)
    @Test
    public void shouldAddTaskToHistory() {
        manager.addTaskToHistory(task1);
        manager.addTaskToHistory(task2);
        List<Task> list = List.of(task1, task2);
        Assertions.assertEquals(list, manager.getHistory());
    }

    @Order(2)
    @Test
    public void shouldGetHistory() {
        List<Task> list = List.of(task1, task2);
        Assertions.assertEquals(list, manager.getHistory());
    }

    @Order(3)
    @Test
    public void shouldAddSameTask() {
        manager.addTaskToHistory(task1);
        List<Task> list = List.of(task2, task1);
        Assertions.assertEquals(list, manager.getHistory());
    }

    @Order(4)
    @Test
    public void shouldRemoveTaskFromHistory() {
        manager.removeTask(task1.getId());
        Assertions.assertEquals(List.of(task2), manager.getHistory());
    }

    @Order(5)
    @Test
    public void shouldClearHistory() {
        manager.clearHistory();
        Assertions.assertTrue(manager.getHistory().isEmpty());
    }
}
