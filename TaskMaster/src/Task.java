import java.io.Serializable;
import java.time.LocalDate;

public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String description;
    private int priority;
    private LocalDate deadline;
    private boolean completed;

    public Task(String name, String description, int priority, LocalDate deadline) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.deadline = deadline;
        this.completed = false;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return "Задача: " + name + " | Приоритет: " + priority + " | Дедлайн: " + deadline + " | Выполнено: " + (completed ? "Да" : "Нет");
    }
}