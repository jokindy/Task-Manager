package taskmanager.managers;

import taskmanager.tasks.*;
import taskmanager.utilities.*;
import taskmanager.utilities.taskservices.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
        readFromFile();
    }

    @Override
    public boolean addTask(Task task) {
        boolean isAdded = super.addTask(task);
        save();
        return isAdded;
    }

    @Override
    public <T extends Task> void editTask(T taskOld, T taskNew) {
        super.editTask(taskOld, taskNew);
        save();
    }

    @Override
    public <T extends Task> void removeTask(T task) {
        super.removeTask(task);
        save();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public Task getTaskById(int number, TaskType type) {
        Task task = super.getTaskById(number, type);
        save();
        return task;
    }

    public void save() {
        String path = file.getPath();
        try (Writer writer = new FileWriter(path)) {
            writer.write("id,type,theme,description,status,duration,startTime\n");
            for (Task simpleTask : simpleTasks) {
                writer.write(simpleTask.toString() + "\n");
            }
            for (EpicTask epicTask : epicTasks) {
                writer.write(epicTask.toString() + "\n");
                List<SubTask> listOfSubTasks = epicTask.getListOfSubTasks();
                for (SubTask subTask : listOfSubTasks) {
                    writer.write(subTask.toString() + "\n");
                }
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


    public void readFromFile() {
        try {
            List<String> list = Files.readAllLines(Path.of(file.getPath()));
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
            TaskIdGenerator.setListOfId(listOfTasks);
            if (!listOfTasks.isEmpty()) {
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

    private void fillHistory(List<Task> listOfTasks, String value) {
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
