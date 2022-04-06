package test.basetests;

import org.junit.jupiter.api.*;
import taskmanager.interfaces.TaskManager;
import taskmanager.managers.Managers;
import taskmanager.tasks.EpicTask;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;
import taskmanager.utilities.taskservices.TaskType;

import java.util.Optional;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseTaskManager<T extends Task> {

    protected T task;
    protected int id;
    protected T changedTask;
    protected static TaskManager manager = Managers.getInMemoryTaskManager();

    public void setParameters(T task, T changedTask) {
        this.task = task;
        this.id = task.getId();
        this.changedTask = changedTask;
    }

    public static void setEpicTask(EpicTask epicTask) {
        manager.addTask(epicTask);
    }

    @Order(1)
    @Test
    public void shouldAddTask() {
        manager.addTask(task);
        boolean isContains = manager.getTaskById(id).isPresent();
        Assertions.assertTrue(isContains);
    }

    @Order(2)
    @Test
    public void shouldGetTask() {
        Optional<Task> receivedTask = manager.getTaskById(id);
        Task task2 = null;
        if (receivedTask.isPresent()) {
            task2 = receivedTask.get();
        }
        Assertions.assertEquals(task2, task);
    }

    @Order(3)
    @Test
    public void shouldEditTask() {
        Task task2 = copyTask(task);
        task2.setDescription("TEST-1");
        manager.editTask(task, task2);
        Optional<Task> receivedTask = manager.getTaskById(id);
        if (receivedTask.isPresent()) {
            task2 = receivedTask.get();
        }
        Assertions.assertEquals(changedTask, task2);
    }

    @Order(4)
    @Test
    public void shouldFindWithWrongId() {
        Optional<Task> receivedTask = manager.getTaskById(33);
        boolean isFound = receivedTask.isPresent();
        Assertions.assertFalse(isFound);
    }

    @Order(5)
    @Test
    public void shouldDeleteTask() {
        manager.removeTask(task);
        Optional<Task> receivedTask = manager.getTaskById(id);
        boolean isEmpty = receivedTask.isEmpty();
        Assertions.assertTrue(isEmpty);
    }

    private Task copyTask(T task) {
        TaskType type = task.getType();
        switch (type) {
            case EPIC:
                return new EpicTask((EpicTask) task);
            case SUB:
                return new SubTask((SubTask) task);
            default:
                return new Task(task);
        }
    }
}
