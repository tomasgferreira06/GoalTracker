package com.example.gps_g12.goalTracker.controller;

import com.almasb.fxgl.app.FXGLPane;
import com.example.gps_g12.goalTracker.model.ModelManager;
import com.example.gps_g12.goalTracker.model.data.Date;
import com.example.gps_g12.goalTracker.model.data.Hour;
import com.example.gps_g12.goalTracker.model.data.Task;
import com.example.gps_g12.goalTracker.model.fsm.EState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class taskGoalIntroductionController {
    ModelManager modelManager;

    @FXML
    private Button setTask;
    @FXML
    private Button cancel;
    @FXML
    private TextField TaskName;
    @FXML
    private TitledPane titledPane;
    @FXML
    private Label nameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label hourLabel;
    @FXML
    private Label priorityLabel;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField hourTextField;
    @FXML
    private TextField minuteTextField;
    @FXML
    private ChoiceBox<String> priorityChoiceBox;
    @FXML
    private ChoiceBox<String> frequencyChoiceBox;
    @FXML
    private CheckBox reminderButton;
    @FXML
    private TextField TimesPerDay;


    @FXML
    public void initialize() throws IOException {
    }

    public void setModelManager(ModelManager modelManager) {
        this.modelManager = modelManager;
        registerHandlers();
        update();
    }

    public void registerHandlers() {
        modelManager.addPropertyChangeListener(evt -> {
            update();
        });
    }

    private void update() {
        /*TaskName.clear();
        datePicker.setValue(null);
        hourTextField.clear();
        minuteTextField.clear();
        priorityChoiceBox.setValue(null);
        frequencyChoiceBox.setValue(null);
        reminderButton.setSelected(false);
        TimesPerDay.clear();*/

        if (modelManager != null) {
            setTask.setVisible(modelManager.getState() == EState.ADD_TASKS);
            cancel.setVisible(modelManager.getState() == EState.ADD_TASKS);
            TaskName.setVisible(modelManager.getState() == EState.ADD_TASKS);
            titledPane.setVisible(modelManager.getState() == EState.ADD_TASKS);
            nameLabel.setVisible(modelManager.getState() == EState.ADD_TASKS);
            dateLabel.setVisible(modelManager.getState() == EState.ADD_TASKS);
            hourLabel.setVisible(modelManager.getState() == EState.ADD_TASKS);
            priorityLabel.setVisible(modelManager.getState() == EState.ADD_TASKS);
            datePicker.setVisible(modelManager.getState() == EState.ADD_TASKS);
            hourTextField.setVisible(modelManager.getState() == EState.ADD_TASKS);
        } else {
            setTask.setVisible(false);
            cancel.setVisible(false);
            TaskName.setVisible(false);
            titledPane.setVisible(false);
            nameLabel.setVisible(false);
            dateLabel.setVisible(false);
            hourLabel.setVisible(false);
            priorityLabel.setVisible(false);
            datePicker.setVisible(false);
            hourTextField.setVisible(false);
        }
    }

    //functions to use on xml

    public void addTask(){
        String taskName = TaskName.getText();
        if (taskName.isEmpty()) {
            // Exiba um aviso ao usuário (pode ser um alerta, uma mensagem, etc.)
            showAlert("Task/Goal name is required.");
            return;
        }

        int day = datePicker.getValue() == null ? 0 : datePicker.getValue().getDayOfMonth();
        int month = datePicker.getValue() == null ? 0 : datePicker.getValue().getMonthValue();
        int year = datePicker.getValue() == null ? 0 : datePicker.getValue().getYear();
        int hour = 0;
        int minute = 0;
        try {
            hour = Integer.parseInt(hourTextField.getText());
            minute = Integer.parseInt(minuteTextField.getText());
        } catch (NumberFormatException e) {
            // Exiba um aviso se as entradas de hora/minuto não forem números válidos
            showAlert("Invalid hour or minute format.");
            return;
        }

        // Verifique se as horas e minutos estão dentro dos limites permitidos
        if (hour < 0 || hour > 23 || minute < 0 || minute > 59) {
            // Exiba um aviso se estiverem fora dos limites
            showAlert("Invalid hour or minute range.");
            return;
        }
        String priority = priorityChoiceBox.getValue();
        String frequency = frequencyChoiceBox.getValue();
        int timesPerDay = getTimesPerDay();  //Use o método que você já definiu
        boolean reminder = reminderButton.isSelected();

        int timesToRepeat;
        if ("weekly".equalsIgnoreCase(frequency)) {
            timesToRepeat = 4;
        } else if ("daily".equalsIgnoreCase(frequency)) {
            timesToRepeat = 7;
        } else if ("monthly".equalsIgnoreCase(frequency)) {
            timesToRepeat = 2;
        } else {
            // Handle other frequencies if needed
            timesToRepeat = 1;
        }

        int taskParentId = 0;

        for (int i = 0; i < timesToRepeat; i++) {

            // Calculate the new date based on the frequency
            LocalDate newDate;
            if ("weekly".equalsIgnoreCase(frequency)) {
                newDate = datePicker.getValue().plusDays(i * 7);
            } else if ("daily".equalsIgnoreCase(frequency)) {
                newDate = datePicker.getValue().plusDays(i);
            } else if ("monthly".equalsIgnoreCase(frequency)) {
                newDate = datePicker.getValue().plusMonths(i);
            } else {
                // Handle other frequencies if needed
                newDate = datePicker.getValue();
            }

            /*Now we need to check the date and add to the proper TaskList tab
                -> if the date isnt TODAY, TOMORROW and it HAS specific date goes to defaultTask
                -> if the date is TODAY add to todayTaskList
                -> if the date is TOMORROW add to tomorrowTaskList
                -> if HAS NO SPECIFIC date add to noSpecDateTaskList
            * */

            LocalDate systemDate = LocalDate.now();

            int systemDay = systemDate.getDayOfMonth();
            int systemMonth = systemDate.getMonthValue();
            int systemYear = systemDate.getYear();

            if(i == 0){
                if(systemDay == newDate.getDayOfMonth() && systemMonth == newDate.getMonthValue() && systemYear == newDate.getYear()){ //today
                    // Create a new task with the updated date
                    Task newTask = new Task(" " + taskName, new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()), new Hour(hour, minute), frequency, priority, timesPerDay, reminder);
                    newTask.setType("Today");

                    taskParentId = newTask.getId();

                    modelManager.addToTodayTasks(newTask);
                    newTask.scheduleReminder();
                }else if(systemDay + 1 == newDate.getDayOfMonth() && systemMonth == newDate.getMonthValue() && systemYear == newDate.getYear()){ //tomorrow
                    // Create a new task with the updated date
                    Task newTask = new Task(" " + taskName, new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()), new Hour(hour, minute), frequency, priority, timesPerDay, reminder);
                    newTask.setType("Tomorrow");

                    taskParentId = newTask.getId();

                    modelManager.addToTomorrowTasks(newTask);
                    newTask.scheduleReminder();
                }else if(day == 0 && month == 0 && year == 0){ //no spec date
                    // Create a new task with the updated date
                    Task newTask = new Task(" " + taskName, new Date(0, 0, 0), new Hour(hour, minute), frequency, priority, timesPerDay, reminder);
                    newTask.setType("NoSpecDate");

                    taskParentId = newTask.getId();

                    modelManager.addToNoSpecDateTasks(newTask);
                    newTask.scheduleReminder();
                }else{ //random day
                    Task newTask = new Task(" " + taskName, new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()), new Hour(hour, minute), frequency, priority, timesPerDay, reminder);
                    newTask.setType("Random");

                    taskParentId = newTask.getId();

                    modelManager.addNewTask(newTask);
                    newTask.scheduleReminder();
                }

            }else{

                if(systemDay == newDate.getDayOfMonth() && systemMonth == newDate.getMonthValue() && systemYear == newDate.getYear()){ //today
                    // Create a new task with the updated date
                    Task newTask = new Task(" " + taskName, new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()), new Hour(hour, minute), frequency, priority, timesPerDay, reminder, taskParentId);
                    newTask.setType("Today");

                    modelManager.addToTodayTasks(newTask);
                    newTask.scheduleReminder();
                }else if(systemDay + 1 == newDate.getDayOfMonth() && systemMonth == newDate.getMonthValue() && systemYear == newDate.getYear()){ //tomorrow
                    // Create a new task with the updated date
                    Task newTask = new Task(" " + taskName, new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()), new Hour(hour, minute), frequency, priority, timesPerDay, reminder, taskParentId);
                    newTask.setType("Tomorrow");

                    modelManager.addToTomorrowTasks(newTask);
                    newTask.scheduleReminder();
                }else if(day == 0 && month == 0 && year == 0){ //no spec date
                    // Create a new task with the updated date
                    Task newTask = new Task(" " + taskName, new Date(0, 0, 0), new Hour(hour, minute), frequency, priority, timesPerDay, reminder, taskParentId);
                    newTask.setType("NoSpecDate");

                    modelManager.addToNoSpecDateTasks(newTask);
                    newTask.scheduleReminder();
                }else{ //random day
                    Task newTask = new Task(" " + taskName, new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()), new Hour(hour, minute), frequency, priority, timesPerDay, reminder, taskParentId);
                    newTask.setType("Random");

                    modelManager.addNewTask(newTask);
                    newTask.scheduleReminder();
                }
            }


        }

        modelManager.changeState(EState.LIST_TASKS);


    }

    public void backToListTasks() {
        modelManager.changeState(EState.LIST_TASKS);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getTaskName() {
        return TaskName.getText();
    }

    public LocalDate getDate() {
        return datePicker.getValue();
    }

    public String getHour() {
        return hourTextField.getText();
    }

    public String getMinute() {
        return minuteTextField.getText();
    }

    public String getPriority() {
        return priorityChoiceBox.getValue();
    }

    public String getFrequency() {
        return frequencyChoiceBox.getValue();
    }

    public boolean getReminder() {
        return reminderButton.isSelected();
    }

    public int getTimesPerDay() {
        try {
            return Integer.parseInt(TimesPerDay.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid format.");
            return 1;

        }
    }
}