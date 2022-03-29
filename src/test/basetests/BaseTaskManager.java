package test.basetests;

import org.junit.jupiter.api.*;
import taskmanager.interfaces.TaskManager;
import taskmanager.managers.Managers;
import taskmanager.tasks.EpicTask;
import taskmanager.tasks.Task;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class BaseTaskManager<T extends Task> {

    protected T task;
    protected T changedTask;
    protected static EpicTask epicTask;
    protected static TaskManager manager = Managers.getInMemoryTaskManager();

    public void setParameters(T task, T changedTask) {
        this.task = task;
        this.changedTask = changedTask;
    }

    public static void setEpicTask(EpicTask task) {
        epicTask = task;
        manager.addTask(epicTask);
    }

    @Order(1)
    @Test
    public void shouldAddTask() {
        manager.addTask(task);
        boolean isContains = task.equals(manager.getTaskById(task.getId(), task.getType()));
        Assertions.assertTrue(isContains);
    }

    @Order(2)
    @Test
    public void shouldGetTask() {
        Task task2 = manager.getTaskById(task.getId(), task.getType());
        Assertions.assertEquals(task2, task);
    }

    @Order(3)
    @Test
    public void shouldEditTask() {
        Task task2 = task;
        task2.setDescription("TEST-1");
        manager.editTask(task, task2);
        task2 = manager.getTaskById(task.getId(), task.getType());
        Assertions.assertEquals(changedTask, task2);
    }

    @Order(4)
    @Test
    public void shouldFindWithWrongId() {
        int id = task.getId() + 1;
        Task task2 = manager.getTaskById(id, task.getType());
        Assertions.assertNotEquals(task2, task);
    }

    @Order(5)
    @Test
    public void shouldDeleteTask() {
        manager.removeTask(task);
        Task task2 = manager.getTaskById(task.getId(), task.getType());
        Assertions.assertNull(task2);
    }
}
