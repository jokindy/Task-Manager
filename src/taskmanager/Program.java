package taskmanager;

import taskmanager.managers.FileBackedTaskManager;
import taskmanager.managers.HttpTaskManager;
import taskmanager.managers.Managers;
import taskmanager.servers.HttpTaskServer;
import taskmanager.servers.KVServer;
import taskmanager.servers.KVTaskClient;
import taskmanager.tasks.Task;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import static taskmanager.utilities.Functions.*;

public class Program {

    public static void main(String[] args) throws IOException {
        String path = "resources\\tasks.csv";
        Scanner scanner = new Scanner(System.in);
        System.out.println("ТЕСТ: 1 - Запустить файловый менеджер, 2 - запустить HTTP сервер, " +
                "3 - запустить HTTP менеджер");
        while (true) {
            int command = scanner.nextInt();
            if (command == 1) {
                FileBackedTaskManager taskManager = Managers.getFileManager(path);
                runMainProgram(taskManager, scanner);
                break;
            } else if (command == 2) {
                HttpTaskServer server = new HttpTaskServer(path);
                server.start();
                break;
            } else if (command == 3) {
                KVServer kvServer = new KVServer();
                kvServer.start();
                URI serverURL = kvServer.getAddress();
                System.out.println("Введите API KEY для работы HTTP-менеджера:");
                int apiKey = scanner.nextInt();
                KVTaskClient client = new KVTaskClient(serverURL, apiKey);
                HttpTaskManager manager = (HttpTaskManager) Managers.getDefault(client);
                runMainProgram(manager, scanner);
                kvServer.close();
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
                List<Task> epicTasks = taskManager.getListOfEpicTasks();
                showListOfTasks(epicTasks);
            } else if (command == 4) {
                System.out.println("Введите идентификатор задачи:");
                int id = scanner.nextInt();
                List<Task> subTasks = taskManager.getEpicSubtasks(id);
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
}