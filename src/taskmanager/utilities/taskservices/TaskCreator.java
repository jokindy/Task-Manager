package taskmanager.utilities.taskservices;

import taskmanager.tasks.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    public static SubTask createSubTask(int epicId) {
        String theme = TaskInfo.getTaskTheme();
        String description = TaskInfo.getTaskDescription();
        TaskStatus status = TaskInfo.getTaskStatus();
        int id = TaskIdGenerator.generateID();
        Duration duration = TaskInfo.getTaskDuration();
        LocalDateTime date = TaskInfo.getTaskStartDate();
        return new SubTask(id, theme, description, status, duration, date, epicId);
    }

    public static Task stringToTask(String value) {
        String[] lineContents = value.split(";");
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
                Set<Integer> setOfId = parseSubTaskId(lineContents[8]);
                task = new EpicTask(id, theme, description, status, duration, date);
                ((EpicTask) task).setSetOfSubId(setOfId);
                break;
            case SUB:
                int epicId = Integer.parseInt(lineContents[7]);
                task = new SubTask(id, theme, description, status, duration, date, epicId);
                break;
        }
        return task;
    }

    public static Task bodyToTask(String value) {
        StringBuilder body = new StringBuilder();
        String[] contents = value.split(",");
        String[] setOfSubId = Arrays.copyOfRange(contents, 8, contents.length);
        for (int i = 0; i < 8; i++) {
            String content = contents[i];
            content = content.replace("\"", "")
                    .replace("{", "")
                    .replace("}", "");
            int index = content.indexOf(":") + 1;
            content = content.substring(index);
            body.append(content).append(";");
        }
        if (setOfSubId.length == 1 && !body.toString().contains("EPIC")) {
            body.append("null").append(";");
        } else if (body.toString().contains("EPIC")) {
            body.append("[]");
        } else {
            for (String id : setOfSubId) {
                String content = id;
                content = content.replaceAll("[^0-9]", "");
                body.append(content).append(",");
            }
        }
        return stringToTask(body.toString());
    }

    private static Set<Integer> parseSubTaskId(String subId) {
        subId = subId.replace("[", "")
                .replace("]", "")
                .replace(" ", "");
        if (!subId.isEmpty()) {
            String[] contents = subId.split(",");
            Set<Integer> set = new HashSet<>();
            for (String content : contents) {
                set.add(Integer.parseInt(content));
            }
            return set;
        } else {
            return new HashSet<>();
        }
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