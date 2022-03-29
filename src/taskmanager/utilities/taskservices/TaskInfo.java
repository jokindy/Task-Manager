package taskmanager.utilities.taskservices;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static taskmanager.utilities.taskservices.TaskStatus.*;

public class TaskInfo {

    private static final Scanner scanner = new Scanner(System.in);

    public static String getTaskTheme() {
        System.out.println("Введите тему задачи:");
        return scanner.nextLine();
    }

    public static String getTaskDescription() {
        System.out.println("Введите описание задачи:");
        return scanner.nextLine();
    }

    public static TaskStatus getTaskStatus() {
        TaskStatus status;
        System.out.println("Введите статус задачи: 1 - Новая, 2 - В работе, 3 - Выполнена:");
        while (true) {
            int command = scanner.nextInt();
            scanner.nextLine();
            if (command == 1) {
                status = NEW;
                break;
            } else if (command == 2) {
                status = IN_PROGRESS;
                break;
            } else if (command == 3) {
                status = DONE;
                break;
            } else {
                System.out.println("Неверная команда");
            }
        }
        return status;
    }

    public static Duration getTaskDuration() {
        System.out.println("Сколько потребуется дней на выполнение задачи? Если задача выполняется в течении дня," +
                " то введите 0.");
        Duration duration;
        while (true) {
            int days = scanner.nextInt();
            scanner.nextLine();
            if (days == 0) {
                System.out.println("Сколько потребуется часов на выполнение задачи?");
                int hours = scanner.nextInt();
                scanner.nextLine();
                duration = Duration.ofHours(hours);
                break;
            } else if (days > 0) {
                duration = Duration.ofDays(days);
                break;
            } else {
                System.out.println("Введено отрицательное значение. Повторите ввод.");
            }
        }
        return duration;
    }

    public static LocalDateTime getTaskStartDate() {
        System.out.println("Установить дату начала выполнения задачи? 1 - Да, 2 - Нет:");
        LocalDateTime time = null;
        int command = scanner.nextInt();
        scanner.nextLine();
        if (command == 1) {
            System.out.println("Когда планируете приступить к задаче? Формат ввода данных: дд.мм.гг чч.мм");
            boolean loop = true;
            while (loop) {
                String date = scanner.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH.mm");
                try {
                    time = LocalDateTime.parse(date, formatter);
                    loop = false;
                } catch (DateTimeParseException e) {
                    System.out.println("Ошибка ввода. Повторите ввод в формате дд.мм.гг чч.мм.");
                }
            }
        } else if (command == 2) {
            System.out.println("Дата не установлена. Не забудьте заполнить ее в последующем.");
        }
        return time;
    }
}