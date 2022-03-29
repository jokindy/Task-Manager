package taskmanager;

import taskmanager.managers.FileBackedTaskManager;
import taskmanager.managers.HttpTaskManager;
import taskmanager.managers.Managers;
import taskmanager.servers.taskserver.HttpTaskServer;
import taskmanager.tasks.EpicTask;
import taskmanager.tasks.SubTask;
import taskmanager.tasks.Task;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import static taskmanager.utilities.Functions.*;

public class Program {

    public static void main(String[] args) throws IOException {
        File tasks = new File("resources\\tasks.csv");
        Scanner scanner = new Scanner(System.in);
        System.out.println("ТЕСТ: 1 - Запустить файловый менеджер, 2 - запустить HTTP менеджер");
        while (true) {
            int command = scanner.nextInt();
            if (command == 1) {
                FileBackedTaskManager taskManager = Managers.getFileManager(tasks);
                runMainProgram(taskManager, scanner);
                break;
            } else if (command == 2) {
                HttpTaskServer server = new HttpTaskServer(tasks);
                server.start();
                URI clientURL = server.getKVServerURI();
                HttpTaskManager manager = (HttpTaskManager) Managers.getDefault(tasks, clientURL);
                runHttpServerProgram(server, manager, scanner);
                break;
            } else {
                System.out.println("Неверная команда. Повторите ввод.");
            }
        }
    }

    public static void runMainProgram(FileBackedTaskManager taskManager, Scanner scanner) {
        while (true) {
            printMenu();
            int command = scanner.nextInt();
            scanner.nextLine();
            if (command == 1) {
                createSimpleOrEpicTask(taskManager, scanner);
            } else if (command == 2) {
                createSubTask(taskManager, scanner);
            } else if (command == 3) {
                List<EpicTask> epicTasks = taskManager.getListOfEpicTasks();
                showListOfTasks(epicTasks);
            } else if (command == 4) {
                System.out.println("Введите идентификатор задачи:");
                int id = scanner.nextInt();
                List<SubTask> subTasks = taskManager.getEpicSubtasks(id);
                showListOfTasks(subTasks);
            } else if (command == 5) {
                List<Task> simpleTasks = taskManager.getListOfSimpleTasks();
                showListOfTasks(simpleTasks);
            } else if (command == 6) {
                operateWithTasksByID(taskManager, scanner);
            } else if (command == 7) {
                taskManager.clearAllTasks();
                System.out.println("Задачи удалены.");
            } else if (command == 8) {
                List<Task> list = taskManager.getHistory();
                showListOfTasks(list);
            } else if (command == 9) {
                TreeMap<Task, Integer> sortedTasks = taskManager.getPrioritizedTasks();
                printSortedTasks(sortedTasks);
            } else if (command == 0) {
                System.out.println("Программа завершена.");
                taskManager.save();
                break;
            } else {
                System.out.println("Извините, такой команды пока нет.");
            }
        }
    }

    public static void runHttpServerProgram(HttpTaskServer server, HttpTaskManager manager, Scanner scanner) {
        System.out.println("Введите API KEY для работы HTTP-менеджера:");
        int apiKey = scanner.nextInt();
        System.out.println("Введите ключ для пути:");
        String key = scanner.next();
        while (true) {
            System.out.println("COMMAND: 1 - kv, 2 - exit");
            int exit = scanner.nextInt();
            if (exit == 1) {
                System.out.println("1 - Сохранить данные, 2 - Загрузить данные.");
                int com = scanner.nextInt();
                if (com == 1) {
                    String json = server.getManagerAsJson(manager.getClient());
                    manager.saveManager(key, apiKey, json);
                    System.out.println("Данные сохранены на KV-сервере. Вы великолепны!");
                } else if (com == 2) {
                    String json = manager.loadManager(key, apiKey);
                    if (json != null) {
                        server.setManagerByJson(json);
                        System.out.println("Данные загружены из KV-сервера. Вы великолепны!.");
                    }
                }
            } else if (exit == 2) {
                System.out.println("Сервера закрыты. Выход из программы.");
                server.close();
                break;
            } else {
                System.out.println("Неверная команда");
            }
        }
    }
}