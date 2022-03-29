package test.managers;

import taskmanager.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static taskmanager.utilities.taskservices.TaskStatus.*;

public class ManagerTestTasks {
    static Task task1 = new Task(1, "SIMPLE-1", "TEST",
            DONE, Duration.ZERO, LocalDateTime.of(2022, 10, 3, 10, 0));
    static Task task2 = new Task(2, "SIMPLE-2", "TEST",
            IN_PROGRESS, Duration.ofDays(2), LocalDateTime.of(2022, 7, 5, 10, 0));
    static EpicTask epicTask = new EpicTask(3, "EPIC-1", "TEST",
            NEW, Duration.ZERO, LocalDateTime.of(2022, 8, 3, 10, 0));
    static SubTask sub1 = new SubTask(4, "EPIC-1", "SUB-1",
            NEW, Duration.ofDays(2), LocalDateTime.of(2022, 8, 3, 10, 0));
    static SubTask sub2 = new SubTask(5, "EPIC-1", "SUB-2",
            NEW, Duration.ofHours(2), LocalDateTime.of(2022, 9, 3, 10, 0));
    static Task taskCrossed = new Task(6, "SIMPLE-2", "TEST",
            IN_PROGRESS, Duration.ofDays(2), LocalDateTime.of(2022, 8, 4, 10, 0));
    static List<Task> listOfTasks = List.of(task1, task2, epicTask, sub1, sub2);
    static List<Task> history = List.of(task1, sub1);
}
