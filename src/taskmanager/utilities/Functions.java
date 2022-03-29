package taskmanager.utilities;

import taskmanager.interfaces.TaskManager;
import taskmanager.tasks.*;
import taskmanager.utilities.taskservices.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static taskmanager.utilities.taskservices.TaskType.*;

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
        while (true) {
            int taskType = scanner.nextInt();
            scanner.nextLine();
            if (taskType == 1) {
                Task task = TaskCreator.createSimpleTask();
                boolean isAdded = manager.addTask(task);
                checkTaskIsAdded(isAdded, task.getId());
                checkTaskTime(manager, task);
                break;
            } else if (taskType == 2) {
                EpicTask epicTask = TaskCreator.createEpicTask();
                boolean isAdded = manager.addTask(epicTask);
                checkTaskIsAdded(isAdded, epicTask.getId());
                checkTaskTime(manager, epicTask);
                break;
            } else {
                System.out.println("Неверная команда");
            }
        }
    }

    public static void createSubTask(TaskManager manager, Scanner scanner) {
        System.out.println("Введите название эпика:");
        String epicName = scanner.next();
        SubTask subTask = TaskCreator.createSubTask(epicName);
        boolean isAdded = manager.addTask(subTask);
        checkTaskIsAdded(isAdded, subTask.getId());
        checkTaskTime(manager, subTask);
    }

    public static void operateWithTasksByID(TaskManager manager, Scanner scanner) {
        System.out.println("Какой тип задачи вы ищете? 1 - Простая, 2 - Эпик, 3 - Сабтаска:");
        int taskType = scanner.nextInt();
        System.out.println("Введите идентификатор задачи:");
        int id = scanner.nextInt();
        Task task = null;
        boolean taskTypeError = false;
        if (taskType == 1) {
            task = manager.getTaskById(id, SIMPLE);
        } else if (taskType == 2) {
            task = manager.getTaskById(id, EPIC);
        } else if (taskType == 3) {
            task = manager.getTaskById(id, SUB);
        } else {
            taskTypeError = true;
        }
        if (task != null) {
            System.out.println("Задача найдена. \nКакую операцию вы хотите совершить? 1 - Вывести инфу, " +
                    "2 - Изменить задачу, 3 - Удалить задачу");
            int operation = scanner.nextInt();
            operateWithTasks(operation, task, manager, scanner);
        } else if (taskTypeError) {
            System.out.println("Неверно выбран тип задачи.");
        } else {
            System.out.println("Задача с данным идентификатором не найдена.");
        }
    }

    public static <T extends Task> void operateWithTasks(int operation, T task1, TaskManager manager,
                                                         Scanner scanner) {
        int command = 0;
        if (operation == 2) {
            System.out.println("Что вы хотите изменить:");
            System.out.println("1 - Название (Для тасок и эпиков), 2 - Описание (для всех задач), " +
                    "3 - Статус (для тасок и сабтасок), \n" +
                    "4 - Продолжительность, 5 - Дату начала выполнения задачи.");
            command = scanner.nextInt();
            scanner.nextLine();
        }
        switch (operation) {
            case 1:
                printTaskInfo(task1);
                break;
            case 2:
                T task2 = updateTaskInfo(command, task1);
                if (task2 != null) {
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

    public static <T extends Task> T updateTaskInfo(int command, T task) {
        switch (command) {
            case 1:
                if (task.getType() != SUB) {
                    String theme = TaskInfo.getTaskTheme();
                    task.setTheme(theme);
                    return task;
                } else {
                    System.out.println("Название у сабтаски изменить нельзя. \nЗадача не обновлена.");
                    return null;
                }
            case 2:
                String description = TaskInfo.getTaskDescription();
                task.setDescription(description);
                return task;
            case 3:
                if (task.getType() != EPIC) {
                    TaskStatus status = TaskInfo.getTaskStatus();
                    task.setStatus(status);
                    return task;
                } else {
                    System.out.println("Статус у эпика рассчитывается автоматически. \nЗадача не обновлена.");
                    return null;
                }
            case 4:
                if (task.getType() != EPIC) {
                    Duration duration = TaskInfo.getTaskDuration();
                    task.setDuration(duration);
                    return task;
                } else {
                    System.out.println("Продолжительность эпика рассчитывается автоматически. \nЗадача не обновлена.");
                    return null;
                }
            case 5:
                if (task.getType() != EPIC) {
                    LocalDateTime dateTime = TaskInfo.getTaskStartDate();
                    task.setStartTime(dateTime);
                    return task;
                } else {
                    System.out.println("Дата старта эпика рассчитывается автоматически. \nЗадача не обновлена.");
                    return null;
                }
            default:
                System.out.println("Неверная команда");
                return null;
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

    public static void checkTaskIsAdded(boolean isAdded, int id) {
        if (isAdded) {
            System.out.println("Задача успешно добавлена. Идентификатор: " + id);
        } else {
            System.out.println("Задача не добавлена.");
        }
    }
}