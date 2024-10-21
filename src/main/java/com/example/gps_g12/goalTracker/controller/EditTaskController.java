package com.example.gps_g12.goalTracker.controller;

import com.example.gps_g12.goalTracker.model.ModelManager;
import com.example.gps_g12.goalTracker.model.data.Date;
import com.example.gps_g12.goalTracker.model.data.Hour;
import com.example.gps_g12.goalTracker.model.data.Task;
import com.example.gps_g12.goalTracker.model.fsm.EState;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EditTaskController {

    ModelManager modelManager;
    Task taskToEdit;

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
    public void initialize(){
    }


    public void setModelManager(ModelManager modelManager) {
        this.modelManager = modelManager;
        registerHandlers();
        update();
    }

    public void registerHandlers(){
        modelManager.addPropertyChangeListener(evt -> { update(); });
    }

    private void update(){
        if (modelManager != null) {
            setTask.setVisible(modelManager.getState() == EState.EDIT_TASK);
            cancel.setVisible(modelManager.getState() == EState.EDIT_TASK);
            TaskName.setVisible(modelManager.getState() == EState.EDIT_TASK);
            titledPane.setVisible(modelManager.getState() == EState.EDIT_TASK);
            nameLabel.setVisible(modelManager.getState() == EState.EDIT_TASK);
            dateLabel.setVisible(modelManager.getState() == EState.EDIT_TASK);
            hourLabel.setVisible(modelManager.getState() == EState.EDIT_TASK);
            priorityLabel.setVisible(modelManager.getState() == EState.EDIT_TASK);
            datePicker.setVisible(modelManager.getState() == EState.EDIT_TASK);
            hourTextField.setVisible(modelManager.getState() == EState.EDIT_TASK);
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

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void setEditedTask(){

        String taskName = TaskName.getText();
        taskToEdit.setName(taskName);
        if (taskName.isEmpty()) {
            // Exiba um aviso ao usuário (pode ser um alerta, uma mensagem, etc.)
            showAlert("Task/Goal name is required.");
            return;
        }

        int day = datePicker.getValue() == null ? 0 : datePicker.getValue().getDayOfMonth();
        int month = datePicker.getValue() == null ? 0 : datePicker.getValue().getMonthValue();
        int year = datePicker.getValue() == null ? 0 : datePicker.getValue().getYear();
        taskToEdit.setDate(new Date(day, month, year));
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
        taskToEdit.setHour(new Hour(hour, minute));
        String priority = priorityChoiceBox.getValue();
        taskToEdit.setPriority(priority);
        String frequency = frequencyChoiceBox.getValue();
        taskToEdit.setFrequency(frequency);
        int timesPerDay = getTimesPerDay();
        taskToEdit.setTimesPerDay(timesPerDay);
        boolean reminder = reminderButton.isSelected();
        taskToEdit.setReminder(reminder);

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

        int taskParentId = taskToEdit.getParentId();
        modelManager.deleteTask(taskToEdit, true);

        if(timesToRepeat == 1){
            String type = taskToEdit.getType();

            if(type.equalsIgnoreCase("today")){
                modelManager.getTodayTasks().remove(taskToEdit);
            } else if (type.equalsIgnoreCase("tomorrow")) {
                modelManager.getTomorrowTasks().remove(taskToEdit);
            }else if (type.equalsIgnoreCase("nospecdate")) {
                modelManager.getNoSpecDateTasks().remove(taskToEdit);
            }else{
                modelManager.getUserTasks().remove(taskToEdit);
            }

            modelManager.deleteTask(taskToEdit, true);
        }

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

            //for the cases that the frequency is different than one day new tasks need to be added and the first one updated
            if(i == 0){
                if(systemDay == newDate.getDayOfMonth() && systemMonth == newDate.getMonthValue() && systemYear == newDate.getYear()){ //today
                    taskToEdit.setDate(new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()));
                    taskToEdit.setType("Today");

                    modelManager.addToTodayTasks(taskToEdit);
                }else if(systemDay + 1 == newDate.getDayOfMonth() && systemMonth == newDate.getMonthValue() && systemYear == newDate.getYear()){ //tomorrow
                    taskToEdit.setDate(new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()));
                    taskToEdit.setType("Tomorrow");

                    modelManager.addToTomorrowTasks(taskToEdit);
                }else if(day == 0 && month == 0 && year == 0){ //no spec date
                    taskToEdit.setDate(new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()));
                    taskToEdit.setType("NoSpecDate");

                    modelManager.addToNoSpecDateTasks(taskToEdit);
                }else{ //random day
                    taskToEdit.setDate(new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()));
                    taskToEdit.setType("Random");

                    modelManager.addNewTask(taskToEdit);
                }
            }else{
                if(systemDay == newDate.getDayOfMonth() && systemMonth == newDate.getMonthValue() && systemYear == newDate.getYear()){ //today
                    // Create a new task with the updated date
                    Task newTask = new Task(" " + taskName, new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()), new Hour(hour, minute), frequency, priority, timesPerDay, reminder, taskParentId);
                    newTask.setType("Today");

                    modelManager.addToTodayTasks(newTask);
                }else if(systemDay + 1 == newDate.getDayOfMonth() && systemMonth == newDate.getMonthValue() && systemYear == newDate.getYear()){ //tomorrow
                    // Create a new task with the updated date
                    Task newTask = new Task(" " + taskName, new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()), new Hour(hour, minute), frequency, priority, timesPerDay, reminder, taskParentId);
                    newTask.setType("Tomorrow");

                    modelManager.addToTomorrowTasks(newTask);
                }else if(day == 0 && month == 0 && year == 0){ //no spec date
                    // Create a new task with the updated date
                    Task newTask = new Task(" " + taskName, new Date(0, 0, 0), new Hour(hour, minute), frequency, priority, timesPerDay, reminder, taskParentId);
                    newTask.setType("NoSpecDate");

                    modelManager.addToNoSpecDateTasks(newTask);
                }else{ //random day
                    Task newTask = new Task(" " + taskName, new Date(newDate.getDayOfMonth(), newDate.getMonthValue(), newDate.getYear()), new Hour(hour, minute), frequency, priority, timesPerDay, reminder, taskParentId);
                    newTask.setType("Random");

                    modelManager.addNewTask(newTask);
                }
            }

            modelManager.editTaskOnFile(taskToEdit);

        }

        modelManager.changeState(EState.LIST_TASKS);

    }

    public void backToListTasks() {
        modelManager.changeState(EState.LIST_TASKS);
    }

    public Task getTaskToEdit() {
        return taskToEdit;
    }

    public void setTaskToEdit(Task taskToEdit) {
        this.taskToEdit = taskToEdit;

        //set the rest of the properties
        TaskName.setText(taskToEdit.getName());

        if(!taskToEdit.getDate().toString().equals("0/0/0")){
            LocalDate stringToDate = LocalDate.parse(taskToEdit.getDate().toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            datePicker.setValue(stringToDate);
        }

        hourTextField.setText(String.valueOf(taskToEdit.getHour().getHours()));
        minuteTextField.setText(String.valueOf(taskToEdit.getHour().getMinutes()));
        priorityChoiceBox.setValue(taskToEdit.getPriority());
        frequencyChoiceBox.setValue(taskToEdit.getFrequency());
        reminderButton.setSelected(taskToEdit.getReminder());
        TimesPerDay.setText(String.valueOf(taskToEdit.getTimesPerDay()));
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
