package taskmanager.utilities.taskservices;

import taskmanager.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static taskmanager.utilities.taskservices.TaskStatus.NEW;

public class TaskCreator {

    public static Task createSimpleTask() {
        String theme = TaskInfo.getTaskTheme();
        String description = TaskInfo.getTaskDescription();
        int id = TaskIdGenerator.generateID();
        TaskStatus status = TaskInfo.getTaskStatus();
        Duration duration = TaskInfo.getTaskDuration();
        LocalDateTime date = TaskInfo.getTaskStartDate();
        return new Task(id, theme, description, status, duration, date);
    }

    public static EpicTask createEpicTask() {
        String theme = TaskInfo.getTaskTheme();
        String description = TaskInfo.getTaskDescription();
        int id = TaskIdGenerator.generateID();
        return new EpicTask(id, theme, description, NEW, Duration.ZERO, null);
    }

    public static SubTask createSubTask(String themeEpic) {
        String description = TaskInfo.getTaskDescription();
        TaskStatus status = TaskInfo.getTaskStatus();
        int id = TaskIdGenerator.generateID();
        Duration duration = TaskInfo.getTaskDuration();
        LocalDateTime date = TaskInfo.getTaskStartDate();
        return new SubTask(id, themeEpic, description, status, duration, date);
    }

    public static Task stringToTask(String value) {
        String[] lineContents = value.split(",");
        int id = Integer.parseInt(lineContents[0]);
        TaskType type = TaskType.valueOf(lineContents[1]);
        String theme = lineContents[2];
        String description = lineContents[3];
        TaskStatus status = TaskStatus.valueOf(lineContents[4]);
        Duration duration = Duration.ZERO;
        if (!lineContents[5].equals("0")) {
            duration = Duration.ofHours(Integer.parseInt(lineContents[5]));
        }
        LocalDateTime date = getDateFromString(lineContents[6]);
        Task task = null;
        switch (type) {
            case SIMPLE:
                task = new Task(id, theme, description, status, duration, date);
                break;
            case EPIC:
                task = new EpicTask(id, theme, description, status, duration, date);
                break;
            case SUB:
                task = new SubTask(id, theme, description, status, duration, date);
                break;
        }
        return task;
    }

    public static Task bodyToTask(String value) {
        StringBuilder body = new StringBuilder();
        String[] contents = value.split(",");
        for (String s : contents) {
            String content = s;
            content = content.replace("\"", "")
                    .replace("{", "")
                    .replace("}", "");
            int index = content.indexOf(":") + 1;
            content = content.substring(index);
            body.append(content).append(",");
        }
        return stringToTask(body.toString());
    }

    private static LocalDateTime getDateFromString(String value) {
        String[] lineContents = value.split("\\.");
        if (!value.equals("null")) {
            int day = Integer.parseInt(lineContents[0]);
            int month = Integer.parseInt(lineContents[1]);
            int year = Integer.parseInt(lineContents[2]);
            int hour = Integer.parseInt(lineContents[3]);
            int minutes = Integer.parseInt(lineContents[4]);
            return LocalDateTime.of(year, month, day, hour, minutes);
        } else {
            return null;
        }
    }
}