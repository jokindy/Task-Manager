package test.basetests;

import taskmanager.tasks.EpicTask;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.time.Duration;

import static taskmanager.utilities.taskservices.TaskStatus.*;

public class BaseTestTasks {
    static Task task = new Task(1, "SIMPLE-1", "TEST",
            DONE, Duration.ZERO, null);
    static Task changedTask = new Task(1, "SIMPLE-1", "TEST-1",
            DONE, Duration.ZERO, null);
    static EpicTask epic = new EpicTask(3, "EPIC-1", "TEST",
            null, Duration.ZERO, null);
    static EpicTask changedEpic = new EpicTask(3, "EPIC-1", "TEST-1",
            null, Duration.ZERO, null);
    static SubTask sub1 = new SubTask(4, "EPIC-1", "SUB-1",
            NEW, Duration.ofDays(2), null);
    static SubTask sub2 = new SubTask(5, "EPIC-1", "SUB-2",
            DONE, Duration.ofDays(2), null);
    static SubTask changedSub = new SubTask(4, "EPIC-1", "TEST-1",
            NEW, Duration.ofDays(2), null);
    static SubTask anotherSub = new SubTask(6, "EPIC-5", "SUB-1",
            DONE, Duration.ofDays(2), null);
}
