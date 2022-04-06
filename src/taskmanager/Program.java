package taskmanager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import taskmanager.managers.*;
import taskmanager.servers.*;
import taskmanager.tasks.Task;
import taskmanager.tasks.TaskDTO;
import taskmanager.utilities.converters.Converters;

import java.io.IOException;
import java.net.URI;
import java.util.*;

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
                fillKVServer(serverURL, apiKey, scanner);
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

    public static void fillKVServer(URI uri, int apiKey, Scanner scanner) {
        System.out.println("Заполнить сервер задачи с диска? 1 - Да, 2 - Нет");
        while (true) {
            int command = scanner.nextInt();
            scanner.nextLine();
            if (command == 1) {
                FileBackedTaskManager taskManager = Managers.getFileManager("resources\\tasks.csv");
                TreeMap<Task, Integer> map = taskManager.getPrioritizedTasks();
                List<TaskDTO> list = new ArrayList<>();
                for (Task task : map.keySet()) {
                    list.add(new TaskDTO(task));
                }
                Gson gson = Converters.registerAll(new GsonBuilder().serializeNulls()).create();
                String jsonTasks = gson.toJson(list);
                StringBuilder jsonHistory = new StringBuilder();
                for (Task task : taskManager.getHistory()) {
                    jsonHistory.append(task.getId()).append(",");
                }
                new KVTaskClient(uri, apiKey).save(jsonTasks, "tasks");
                new KVTaskClient(uri, apiKey).save(jsonHistory.toString(), "history");
                break;
            } else if (command == 2) {
                break;
            } else {
                System.out.println("Неверная команда.");
            }
        }
    }
}