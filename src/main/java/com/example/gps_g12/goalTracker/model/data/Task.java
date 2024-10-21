package com.example.gps_g12.goalTracker.model.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Timer;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Task {

    public static int idS = 0;
    private int id;
    private String name;
    private Date date;
    private Hour hour;
    private String frequency;
    private String priority;
    private int TimesPerDay;
    private boolean reminder;
    private BooleanProperty isCompleted = new SimpleBooleanProperty(false);
    private boolean isUndone;
    private String type;

    private int parentId;

    private static int getNextIdFromTasksFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/com/example/gps_g12/goalTracker/files/tasks.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] taskDetails = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (taskDetails.length > 0) {
                    return Integer.parseInt(taskDetails[0]) + 1;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 1; // Valor padrão se não conseguir ler o arquivo ou encontrar um ID válido
    }

    public Task(String _name, Date _date, Hour _hour, String _frequency, String _priority, int _timesPerDay, boolean _reminder){
        this.id             = getNextIdFromTasksFile();
        this.name           = _name;
        this.date           = _date;
        this.hour           = _hour;
        this.frequency      = _frequency;
        this.priority       = _priority;
        this.TimesPerDay    = _timesPerDay;
        this.reminder       = _reminder;
        this.isUndone       = false;
        this.type           = "";
        this.parentId       = id;
    }

    public Task(int _id, String _name, Date _date, Hour _hour, String _frequency, String _priority, int _timesPerDay, boolean _reminder, boolean _isCompleted, int _parentId){
        this.id             = _id;
        this.name           = _name;
        this.date           = _date;
        this.hour           = _hour;
        this.frequency      = _frequency;
        this.priority       = _priority;
        this.TimesPerDay    = _timesPerDay;
        this.reminder       = _reminder;
        this.isUndone       = false;
        this.type           = "";
        this.isCompleted.set(_isCompleted);
        this.parentId       = _parentId;
    }

    public Task(int _id, String _name, Date _date, Hour _hour, String _frequency, String _priority, int _timesPerDay, boolean _reminder){
        this.id             = _id;
        this.name           = _name;
        this.date           = _date;
        this.hour           = _hour;
        this.frequency      = _frequency;
        this.priority       = _priority;
        this.TimesPerDay    = _timesPerDay;
        this.reminder       = _reminder;
        this.isUndone       = false;
        this.type           = "";
        this.parentId       = id;
    }

    public Task(String _name, Date _date, Hour _hour, String _frequency, String _priority, int _timesPerDay, boolean _reminder, int _parentId){
        this.id             = getNextIdFromTasksFile();
        this.name           = _name;
        this.date           = _date;
        this.hour           = _hour;
        this.frequency      = _frequency;
        this.priority       = _priority;
        this.TimesPerDay    = _timesPerDay;
        this.reminder       = _reminder;
        this.isUndone       = false;
        this.type           = "";
        this.parentId       = _parentId;
    }

    public Task(int _id, String _name, Date _date, Hour _hour, String _frequency, String _priority, int _timesPerDay, boolean _reminder, int _parentId){
        this.id             = _id;
        this.name           = _name;
        this.date           = _date;
        this.hour           = _hour;
        this.frequency      = _frequency;
        this.priority       = _priority;
        this.TimesPerDay    = _timesPerDay;
        this.reminder       = _reminder;
        this.isUndone       = false;
        this.type           = "";
        this.parentId       = _parentId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Hour getHour() {
        return hour;
    }

    public void setHour(Hour hour) {
        this.hour = hour;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getTimesPerDay() {
        return TimesPerDay;
    }

    public void setTimesPerDay(int TimesPerDay) {
        this.TimesPerDay = TimesPerDay;
    }

    public boolean getReminder() {
        return reminder;
    }

    public void setReminder(boolean reminder) {
        this.reminder = reminder;
    }

    public boolean isCompleted() {
        return isCompleted.get();
    }

    public BooleanProperty completedProperty() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted.set(completed);
    }

    public boolean isUndone() {
        return isUndone;
    }

    public void setUndone(boolean undone) {
        isUndone = undone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return TimesPerDay == task.TimesPerDay && reminder == task.reminder && Objects.equals(name, task.name) && Objects.equals(date, task.date) && Objects.equals(hour, task.hour) && Objects.equals(frequency, task.frequency) && Objects.equals(priority, task.priority) && Objects.equals(isCompleted, task.isCompleted);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, date, hour, frequency, priority, TimesPerDay, reminder, isCompleted);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", hour=" + hour +
                ", frequency='" + frequency + '\'' +
                ", priority='" + priority + '\'' +
                ", reminder=" + reminder +
                '}';
    }

    public void scheduleReminder() {
        if (!this.reminder) return; // Não agende se o lembrete não estiver ativo

        Timer timer = new Timer();
        long delay = calculateDelayForReminder();

        TimerTask reminderTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Task Reminder");
                    alert.setHeaderText("Reminder for the Task");
                    alert.setContentText("The task '" + getName() + "' is about to finish!");
                    alert.showAndWait();
                });
            }
        };

        timer.schedule(reminderTask, delay);
    }

    private long calculateDelayForReminder() {
        // Substitua com a lógica adequada para calcular o delay
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime taskTime = LocalDateTime.of(this.date.getYear(), this.date.getMonth(), this.date.getDay(), this.hour.getHours(), this.hour.getMinutes());
        long delayInMillis = java.time.Duration.between(now, taskTime).toMillis() - 3600000; // 60 minutos antes

        // Certifique-se de que o atraso não seja negativo
        return Math.max(delayInMillis, 0);
    }
}
