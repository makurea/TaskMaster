import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.io.*;

public class TaskManager {
    private List<Task> tasks;
    private Scanner scanner;
    private static final String FILE_NAME = "tasks.dat";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public TaskManager() {
        tasks = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadTasks();
    }

    public void run() {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = getIntInput("Выберите действие: ");

            switch (choice) {
                case 1:
                    addTask();
                    break;
                case 2:
                    viewTasks();
                    break;
                case 3:
                    editTask();
                    break;
                case 4:
                    deleteTask();
                    break;
                case 5:
                    markTaskComplete();
                    break;
                case 6:
                    sortTasks();
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
        saveTasks();
    }

    private void printMenu() {
        System.out.println("\n--- TaskMaster: Умный планировщик задач ---");
        System.out.println("1. Добавить задачу");
        System.out.println("2. Просмотреть задачи");
        System.out.println("3. Редактировать задачу");
        System.out.println("4. Удалить задачу");
        System.out.println("5. Отметить задачу как выполненную");
        System.out.println("6. Сортировать задачи");
        System.out.println("7. Выйти");
    }

    private void addTask() {
        String name = getStringInput("Введите название задачи: ");
        String description = getStringInput("Введите описание задачи: ");
        int priority = getIntInput("Введите приоритет (1-5): ");
        LocalDate deadline = getDateInput("Введите дедлайн (дд-мм-гггг): ");

        Task newTask = new Task(name, description, priority, deadline);
        tasks.add(newTask);
        System.out.println("Задача добавлена успешно!");
    }

    private void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("Список задач пуст.");
        } else {
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                System.out.println((i + 1) + ". Задача: " + task.getName() +
                        " | Приоритет: " + task.getPriority() +
                        " | Дедлайн: " + task.getDeadline().format(DATE_FORMATTER) +
                        " | Выполнено: " + (task.isCompleted() ? "Да" : "Нет"));
            }
        }
    }

    private void editTask() {
        viewTasks();
        if (!tasks.isEmpty()) {
            int index = getIntInput("Введите номер задачи для редактирования: ") - 1;
            if (index >= 0 && index < tasks.size()) {
                Task task = tasks.get(index);
                task.setName(getStringInput("Новое название задачи: "));
                task.setDescription(getStringInput("Новое описание задачи: "));
                task.setPriority(getIntInput("Новый приоритет (1-5): "));
                task.setDeadline(getDateInput("Новый дедлайн (дд-мм-гггг): "));
                System.out.println("Задача обновлена успешно!");
            } else {
                System.out.println("Неверный номер задачи.");
            }
        }
    }

    private void deleteTask() {
        viewTasks();
        if (!tasks.isEmpty()) {
            int index = getIntInput("Введите номер задачи для удаления: ") - 1;
            if (index >= 0 && index < tasks.size()) {
                tasks.remove(index);
                System.out.println("Задача удалена успешно!");
            } else {
                System.out.println("Неверный номер задачи.");
            }
        }
    }

    private void markTaskComplete() {
        viewTasks();
        if (!tasks.isEmpty()) {
            int index = getIntInput("Введите номер задачи для отметки как выполненной: ") - 1;
            if (index >= 0 && index < tasks.size()) {
                Task task = tasks.get(index);
                task.setCompleted(true);
                System.out.println("Задача отмечена как выполненная!");
            } else {
                System.out.println("Неверный номер задачи.");
            }
        }
    }

    private void sortTasks() {
        System.out.println("Выберите способ сортировки:");
        System.out.println("1. По приоритету");
        System.out.println("2. По дедлайну");
        int choice = getIntInput("Ваш выбор: ");

        if (choice == 1) {
            tasks.sort(Comparator.comparingInt(Task::getPriority).reversed());
        } else if (choice == 2) {
            tasks.sort(Comparator.comparing(Task::getDeadline));
        } else {
            System.out.println("Неверный выбор.");
            return;
        }
        System.out.println("Задачи отсортированы.");
        viewTasks();
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите целое число.");
            }
        }
    }

    private LocalDate getDateInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return LocalDate.parse(scanner.nextLine().trim(), DATE_FORMATTER);
            } catch (DateTimeParseException e) {
                System.out.println("Пожалуйста, введите дату в формате дд-мм-гггг.");
            }
        }
    }

    private void saveTasks() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(tasks);
            System.out.println("Задачи сохранены в файл.");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении задач: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTasks() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                tasks = (List<Task>) ois.readObject();
                System.out.println("Задачи загружены из файла.");
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Ошибка при загрузке задач: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        manager.run();
    }
}