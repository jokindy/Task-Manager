package taskmanager.managers;

import taskmanager.tasks.Task;
import taskmanager.tasks.TaskDTO;
import taskmanager.utilities.ManagerSaveException;
import taskmanager.utilities.taskservices.TaskCreator;
import taskmanager.utilities.taskservices.TaskIdGenerator;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final String path;

    public FileBackedTaskManager(String path) {
        this.path = path;
        new File(path);
        load();
    }

    public FileBackedTaskManager() {
        path = null;
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void editTask(Task taskOld, Task taskNew) {
        super.editTask(taskOld, taskNew);
        save();
    }

    @Override
    public void removeTask(Task task) {
        super.removeTask(task);
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public Optional<Task> getTaskById(int number) {
        Optional<Task> task = super.getTaskById(number);
        save();
        return task;
    }

    public void save() {
        try (Writer writer = new FileWriter(path)) {
            writer.write("id,type,theme,description,status,duration,startTime,epicId,subIDs\n");
            Map<Integer, Task> map = TaskRepository.getStorage();
            for (Task task : map.values()) {
                TaskDTO formattedTask = new TaskDTO(task);
                writer.write(formattedTask + "\n");
            }
            List<Task> list = super.getHistory();
            if (!list.isEmpty()) {
                writer.write("\n");
                for (Task task : list) {
                    writer.write(task.getId() + ",");
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла", e);
        }
    }


    public void load() {
        try {
            List<String> list = Files.readAllLines(Path.of(path));
            List<Task> listOfTasks = new ArrayList<>();
            String historyID = null;
            if (list.contains("")) {
                historyID = list.get(list.size() - 1);
                list = list.subList(1, list.size() - 2);
            } else {
                list.remove(0);
            }
            for (String s : list) {
                Task task = TaskCreator.stringToTask(s);
                listOfTasks.add(task);
                super.addTask(task);
            }
            if (!listOfTasks.isEmpty()) {
                TaskIdGenerator.setListOfId(listOfTasks);
                System.out.print("Задачи прочтены из файла. ID: ");
                for (Task task : listOfTasks) {
                    System.out.print(task.getId() + "; ");
                }
                System.out.println();
            }
            if (historyID != null) {
                fillHistory(listOfTasks, historyID);
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла", e);
        }
    }

    private List<Integer> idFromFile(String value) {
        String[] lineContents = value.split(",");
        List<Integer> list = new ArrayList<>();
        for (String lineContent : lineContents) {
            list.add(Integer.parseInt(lineContent));
        }
        return list;
    }

    protected void fillHistory(List<Task> listOfTasks, String value) {
        List<Integer> listOfID = idFromFile(value);
        for (Integer id : listOfID) {
            for (Task task : listOfTasks) {
                if (id == task.getId()) {
                    historyManager.addTaskToHistory(task);
                }
            }
        }
    }
}
