package test.managers;

import org.junit.jupiter.api.*;
import taskmanager.interfaces.TaskManager;
import taskmanager.tasks.EpicTask;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.util.List;
import java.util.TreeMap;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public abstract class TaskManagerTest<T extends TaskManager> {

    private T manager;
    private Task task1;
    private Task task2;
    private EpicTask epicTask;
    private SubTask sub1;
    private SubTask sub2;
    private Task taskCrossed;

    public void setParameters(T manager) {
        this.manager = manager;
        task1 = ManagerTestTasks.task1;
        task2 = ManagerTestTasks.task2;
        epicTask = ManagerTestTasks.epicTask;
        sub1 = ManagerTestTasks.sub1;
        sub2 = ManagerTestTasks.sub2;
        taskCrossed = ManagerTestTasks.taskCrossed;
    }

    @Order(2)
    @Test
    public void shouldGetListOfSimpleTasks() {
        List<Task> list = List.of(task1, task2);
        Assertions.assertEquals(list, manager.getListOfSimpleTasks());
    }

    @Order(3)
    @Test
    public void shouldGetListOfEpicTasks() {
        List<EpicTask> list = List.of(epicTask);
        Assertions.assertEquals(list, manager.getListOfEpicTasks());
    }

    @Order(4)
    @Test
    public void shouldGetListOfSubTasks() {
        List<SubTask> list = List.of(sub1, sub2);
        List<Task> listOfSubTasks = manager.getEpicSubtasks(3);
        Assertions.assertEquals(list, listOfSubTasks);
    }

    @Order(5)
    @Test
    public void shouldGetHistory() {
        Task task = manager.getTaskById(1).get();
        SubTask subTask = (SubTask) manager.getTaskById(4).get();
        List<Task> list = List.of(task, subTask);
        Assertions.assertEquals(list, manager.getHistory());
    }

    @Order(6)
    @Test
    public void shouldGetPrioritizedTasks() {
        TreeMap<Task, Integer> map = manager.getPrioritizedTasks();
        TreeMap<Task, Integer> map1 = new TreeMap<>();
        map1.put(task1, task1.getId());
        map1.put(task2, task2.getId());
        map1.put(epicTask, epicTask.getId());
        map1.put(sub1, sub1.getId());
        map1.put(sub2, sub2.getId());
        Assertions.assertEquals(map, map1);
    }

    @Order(7)
    @Test
    public void shouldCheckTaskIntersections() {
        Assertions.assertTrue(manager.checkTaskIntersections(taskCrossed));
    }

    @Order(12)
    @Test
    public void shouldClearAllTasks() {
        manager.clearAllTasks();
        boolean isClear = manager.getListOfSimpleTasks().isEmpty() &&
                manager.getListOfEpicTasks().isEmpty();
        Assertions.assertTrue(isClear);
    }
}
