package taskmanager.utilities;

import taskmanager.interfaces.TaskManager;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;
import taskmanager.utilities.taskservices.TaskCreator;
import taskmanager.utilities.taskservices.TaskInfo;
import taskmanager.utilities.taskservices.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.TreeMap;

public class Functions {

    public static void printMenu() {
        System.out.println("Что вы хотите сделать? ");
        System.out.println("1 - Добавить задачу");
        System.out.println("2 - Добавить подзадачу");
        System.out.println("3 - Вывести список эпиков");
        System.out.println("4 - Вывести список подзадач эпика");
        System.out.println("5 - Вывести список простых задач");
        System.out.println("6 - Операции с задачами по ID");
        System.out.println("7 - Удалить все задачи");
        System.out.println("8 - Вывести список просмотренных задач");
        System.out.println("9 - Вывести список задач, отсортированный по дате");
        System.out.println("0 - Выход");
    }

    public static void createSimpleOrEpicTask(TaskManager manager, Scanner scanner) {
        System.out.println("Хотите cоздать новую задачу? 1 - Простая, 2 - Эпик:");
        Task task;
        while (true) {
            int taskType = scanner.nextInt();
            scanner.nextLine();
            if (taskType == 1) {
                task = TaskCreator.createSimpleTask();
                break;
            } else if (taskType == 2) {
                task = TaskCreator.createEpicTask();
                break;
            } else {
                System.out.println("Неверная команда");
            }
        }
        manager.addTask(task);
        System.out.println("Задача успешно добавлена. Идентификатор: " + task.getId());
        checkTaskTime(manager, task);
    }

    public static void createSubTask(TaskManager manager, Scanner scanner) {
        System.out.println("Введите ID эпика:");
        int epicID = scanner.nextInt();
        if (manager.checkTask(epicID)) {
            SubTask subTask = TaskCreator.createSubTask(epicID);
            manager.addTask(subTask);
            System.out.println("Задача успешно добавлена. Идентификатор: " + subTask.getId());
            checkTaskTime(manager, subTask);
        } else {
            System.out.println("Такого эпика нет.");
        }
    }

    public static void operateWithTasksByID(TaskManager manager, Scanner scanner) {
        System.out.println("Введите идентификатор задачи:");
        int id = scanner.nextInt();
        Optional<Task> task = manager.getTaskById(id);
        if (task.isPresent()) {
            System.out.println("Задача найдена. \nКакую операцию вы хотите совершить? 1 - Вывести инфу, " +
                    "2 - Изменить задачу, 3 - Удалить задачу");
            int operation = scanner.nextInt();
            operateWithTasks(operation, task.get(), manager, scanner);
        } else {
            System.out.println("Задача с данным идентификатором не найдена.");
        }
    }

    public static void operateWithTasks(int operation, Task task1, TaskManager manager, Scanner scanner) {
        switch (operation) {
            case 1:
                printTaskInfo(task1);
                break;
            case 2:
                int command = getCommand(scanner);
                Task buffer = new Task(task1);
                Task task2 = updateTaskInfo(command, task1);
                if (!task2.equals(buffer)) {
                    manager.editTask(task1, task2);
                    checkTaskTime(manager, task2);
                    System.out.println("Задача успешно обновлена!");
                }
                break;
            case 3:
                manager.removeTask(task1);
                System.out.println("Задача успешно удалена!");
                break;
            default:
                System.out.println("Неверная команда");
                break;
        }
    }

    public static Task updateTaskInfo(int command, Task task) {
        switch (command) {
            case 1:
                String theme = TaskInfo.getTaskTheme();
                task.setTheme(theme);
                return task;
            case 2:
                String description = TaskInfo.getTaskDescription();
                task.setDescription(description);
                return task;
            case 3:
                TaskStatus status = TaskInfo.getTaskStatus();
                task.setStatus(status);
                return task;
            case 4:
                Duration duration = TaskInfo.getTaskDuration();
                task.setDuration(duration);
                return task;
            case 5:
                LocalDateTime dateTime = TaskInfo.getTaskStartDate();
                task.setStartTime(dateTime);
                return task;
            default:
                System.out.println("Неверная команда");
                return task;
        }
    }

    public static void printTaskInfo(Task task) {
        System.out.println("Название задачи - " + task.getTheme());
        System.out.println("Описание задачи - " + task.getDescription());
        System.out.println("Идентификатор задачи - " + task.getId());
        System.out.println("Статус задачи - " + task.getStatus());
        System.out.println("Тип задачи - " + task.getType());
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        if (task.getStartTime() != null) {
            System.out.println("Начало выполнения задачи - " + task.getStartTime().format(outputFormatter));
        } else {
            System.out.println("Начало выполнения задачи - неопределенно");
        }
        Duration duration = task.getDuration();
        System.out.printf("Продолжительность - %d дня (дней), %01d часа (часов).\n", (duration.toHours() / 24),
                (duration.toHours() % 24));
    }

    public static <T extends Task> void showListOfTasks(List<T> tasks) {
        if (!tasks.isEmpty()) {
            System.out.println("Список задач:\n-----------------------");
            for (T task : tasks) {
                printTaskInfo(task);
                System.out.println("-----------------------");
            }
        } else {
            System.out.println("Список задач пуст.");
        }
    }

    public static void printSortedTasks(TreeMap<Task, Integer> map) {
        if (!map.isEmpty()) {
            System.out.println("Список отсортированных задач:\n-----------------------");
            for (Task task : map.keySet()) {
                System.out.println("Идентификатор задачи - " + task.getId());
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
                if (task.getStartTime() != null) {
                    System.out.println("Начало выполнения задачи - " + task.getStartTime().format(outputFormatter));
                } else {
                    System.out.println("Начало выполнения задачи - неопределенно");
                }
                System.out.println("-----------------------");
            }
        } else {
            System.out.println("Список задач пуст.");
        }
    }

    public static void checkTaskTime(TaskManager manager, Task task) {
        if (manager.checkTaskIntersections(task)) {
            System.out.println("Время выполнения задачи попадает в период работы над другой задачей." +
                    "\nИсправьте время.");
        }
    }

    public static int getCommand(Scanner scanner) {
        System.out.println("Что вы хотите изменить:");
        System.out.println("1 - Название (Для тасок и эпиков), 2 - Описание (для всех задач), " +
                "3 - Статус (для тасок и сабтасок), \n" +
                "4 - Продолжительность, 5 - Дату начала выполнения задачи.");
        return scanner.nextInt();
    }
}