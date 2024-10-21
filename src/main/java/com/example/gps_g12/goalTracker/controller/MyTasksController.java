package com.example.gps_g12.goalTracker.controller;

import com.example.gps_g12.goalTracker.GoalTrackerApplication;
import com.example.gps_g12.goalTracker.model.ModelManager;
import com.example.gps_g12.goalTracker.model.data.Task;
import com.example.gps_g12.goalTracker.model.fsm.EState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.text.SimpleDateFormat;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import java.util.Optional;

public class MyTasksController {


    public class TaskAdapter extends ListCell<Task> {

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (task == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                // Crie um HBox personalizado para exibir a tarefa
                HBox taskBox = new HBox();
                CheckBox checkBox = new CheckBox();
                checkBox.getStyleClass().add("checkbox");
                checkBox.setId("checkBox");
                Label nameLabel = new Label(" " + task.getName());

                // Use bind para sincronizar as propriedades selected e completed
                checkBox.selectedProperty().bindBidirectional(task.completedProperty());

                // Desative a caixa de seleção se a tarefa estiver completa
                checkBox.disableProperty().bind(task.completedProperty());

                taskBox.getChildren().addAll(checkBox, nameLabel);

                // Segunda HBox para exibir detalhes da tarefa
                HBox detailsBox = new HBox();
                Label dateLabel = new Label(task.getDate().getDay() + "/" + task.getDate().getMonth() + "/" + task.getDate().getYear());
                Label timeLabel = new Label(" - " + task.getHour().getHours() + ":" + task.getHour().getMinutes());
                Label frequencyLabel = new Label(" - " + task.getFrequency());
                Label priorityLabel = new Label(" - " + task.getPriority() + " Priority");
                Label timesPerDayLabel = new Label(" - " + task.getTimesPerDay() + " Time(s) per day");

                //dateLabel.setStyle();

                detailsBox.getChildren().addAll(dateLabel, timeLabel, frequencyLabel, priorityLabel, timesPerDayLabel);
                detailsBox.setStyle("-fx-font-size: 8px;" + "-fx-padding: 7 0 ;");

                Button botaoInvisivel = new Button("");
                botaoInvisivel.getStyleClass().add("delete-button");
                botaoInvisivel.setStyle(
                        "-fx-background-color: white;" + "-fx-pref-width: 260;"
                );

                Button editButton = new Button("Edit");
                editButton.getStyleClass().add("edit-button");
                editButton.setOnAction(event -> onEditTaskClick(task));
                editButton.setStyle(
                        "-fx-cursor: hand;" +
                                "-fx-background-color: #64b5f6;" +
                                "-fx-text-fill: white;" +
                                "-fx-padding: 5 10;" +
                                "-fx-font-family: 'Segoe UI Emoji';" +
                                "-fx-pref-width: 50;"
                );

                Button botaoInvisivel2 = new Button("");
                botaoInvisivel2.getStyleClass().add("delete-button");
                botaoInvisivel2.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-pref-width: 1;"
                );

                Button deleteButton = new Button("❌");
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> showDeleteConfirmation(task));
                deleteButton.setStyle(
                        "-fx-cursor: hand;" +
                                "-fx-background-color: #e57373;" +
                                "-fx-text-fill: white;" +
                                "-fx-padding: 5 10;" +
                                "-fx-margin: 0 50;" +// Defina o tamanho preferencial desejado
                                "-fx-pref-width: 40;"
                );


                HBox deleteBox = new HBox();
                deleteBox.getChildren().addAll(botaoInvisivel, editButton, botaoInvisivel2, deleteButton);
                deleteBox.setStyle("-fx-padding: 0 0 0 10;");

                // Adicione ambas as HBoxes ao VBox principal
                VBox container = new VBox();
                container.getChildren().addAll(taskBox, detailsBox, deleteBox);

                // Defina o VBox como o gráfico para a célula da lista
                setGraphic(container);

                checkBox.setOnAction(e -> {
                    setTaskCompleted(task);
                });
            }
        }

        private void showDeleteConfirmation(Task task) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this task?");

            ButtonType confirmButtonType = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(confirmButtonType, cancelButtonType);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == confirmButtonType) {
                // Usuário confirmou a exclusão
                deleteTask(task);
            }
        }

        private void deleteTask(Task task) {
            // Lógica para excluir a tarefa
            // Remova a tarefa da lista de tarefas

            String type = task.getType();
            if(type.equalsIgnoreCase("today")){
                modelManager.getTodayTasks().remove(task);
            }else if(type.equalsIgnoreCase("tomorrow")){
                modelManager.getTomorrowTasks().remove(task);
            }else if(type.equalsIgnoreCase("nospecdate")){
                modelManager.getNoSpecDateTasks().remove(task);
            }else{
                modelManager.getUserTasks().remove(task);
            }

            modelManager.deleteTask(task, true);

            updateTaskList();
            updateTomorrowList();
            updateNoSpecDateList();
            updateCompletedList();
        }
    }

    public class TaskAdapterCompleted extends ListCell<Task> {

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (task == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                // Crie um HBox personalizado para exibir a tarefa
                HBox taskBox = new HBox();
                Label label = new Label(task.getName());




                taskBox.getChildren().addAll(label);

                // Segunda HBox para exibir detalhes da tarefa
                HBox detailsBox = new HBox();
                Label dateLabel = new Label(task.getDate().getDay() + "/" + task.getDate().getMonth() + "/" + task.getDate().getYear());
                Label timeLabel = new Label(" - " + task.getHour().getHours() + ":" + task.getHour().getMinutes());
                Label frequencyLabel = new Label(" - " + task.getFrequency());
                Label priorityLabel = new Label(" - " + task.getPriority() + " Priority");
                Label timesPerDayLabel = new Label(" - " + task.getTimesPerDay() + " Time(s) per day");

                //dateLabel.setStyle();

                detailsBox.getChildren().addAll(dateLabel, timeLabel, frequencyLabel, priorityLabel, timesPerDayLabel);
                detailsBox.setStyle("-fx-font-size: 8px;" + "-fx-padding: 7 0 ;");

                Button undoButton = new Button("Undo");
                undoButton.getStyleClass().add("undoButton");
                undoButton.setOnAction(e -> undoTaskCompletion(task));

                HBox deleteBox = new HBox();
                deleteBox.getChildren().addAll(undoButton);
                deleteBox.setStyle("-fx-padding: 0 0 0 10;");

                // Adicione ambas as HBoxes ao VBox principal
                VBox container = new VBox();
                container.getChildren().addAll(taskBox, detailsBox,deleteBox);

                // Defina o VBox como o gráfico para a célula da lista
                setGraphic(container);
            }
        }

        private void showDeleteConfirmation(Task task) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this task?");

            ButtonType confirmButtonType = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(confirmButtonType, cancelButtonType);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == confirmButtonType) {
                // Usuário confirmou a exclusão
                deleteTask(task);
            }
        }

        private void deleteTask(Task task) {
            // Lógica para excluir a tarefa
            // Remova a tarefa da lista de tarefas

            String type = task.getType();
            if(type.equalsIgnoreCase("today")){
                modelManager.getTodayTasks().remove(task);
            }else if(type.equalsIgnoreCase("tomorrow")){
                modelManager.getTomorrowTasks().remove(task);
            }else if(type.equalsIgnoreCase("nospecdate")){
                modelManager.getNoSpecDateTasks().remove(task);
            }else{
                modelManager.getUserTasks().remove(task);
            }

            modelManager.deleteTask(task, true);

            // Atualize a interface gráfica
            updateTaskList();
            updateTomorrowList();
            updateNoSpecDateList();
            updateCompletedList();
        }

        /*@Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (task == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                HBox taskBox = new HBox();
                Label label = new Label(task.getName());

                Button undoButton = new Button("Undo");
                undoButton.getStyleClass().add("undoButton");
                undoButton.setOnAction(e -> undoTaskCompletion(task));

                taskBox.getChildren().addAll(label, undoButton);
                setGraphic(taskBox);
            }
        }*/
    }
    public class TaskAdapterTomorrowList extends ListCell<Task> {

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (task == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                // Crie um HBox personalizado para exibir a tarefa
                HBox taskBox = new HBox();
                CheckBox checkBox = new CheckBox();
                checkBox.getStyleClass().add("checkbox");
                checkBox.setId("checkBox");
                Label nameLabel = new Label(" " + task.getName());

                // Use bind para sincronizar as propriedades selected e completed
                checkBox.selectedProperty().bindBidirectional(task.completedProperty());

                // Desative a caixa de seleção se a tarefa estiver completa
                checkBox.disableProperty().bind(task.completedProperty());

                taskBox.getChildren().addAll(checkBox, nameLabel);

                // Segunda HBox para exibir detalhes da tarefa
                HBox detailsBox = new HBox();
                Label dateLabel = new Label(task.getDate().getDay() + "/" + task.getDate().getMonth() + "/" + task.getDate().getYear());
                Label timeLabel = new Label(" - " + task.getHour().getHours() + ":" + task.getHour().getMinutes());
                Label frequencyLabel = new Label(" - " + task.getFrequency());
                Label priorityLabel = new Label(" - " + task.getPriority() + " Priority");
                Label timesPerDayLabel = new Label(" - " + task.getTimesPerDay() + " Time(s) per day");

                //dateLabel.setStyle();

                detailsBox.getChildren().addAll(dateLabel, timeLabel, frequencyLabel, priorityLabel, timesPerDayLabel);
                detailsBox.setStyle("-fx-font-size: 8px;" + "-fx-padding: 7 0 ;");

                Button botaoInvisivel = new Button("");
                botaoInvisivel.getStyleClass().add("delete-button");
                botaoInvisivel.setStyle(
                        "-fx-background-color: white;" + "-fx-pref-width: 260;"
                );

                Button editButton = new Button("Edit");
                editButton.getStyleClass().add("edit-button");
                editButton.setOnAction(event -> onEditTaskClick(task));
                editButton.setStyle(
                        "-fx-cursor: hand;" +
                                "-fx-background-color: #64b5f6;" +
                                "-fx-text-fill: white;" +
                                "-fx-padding: 5 10;" +
                                "-fx-font-family: 'Segoe UI Emoji';" +
                                "-fx-pref-width: 50;"
                );

                Button botaoInvisivel2 = new Button("");
                botaoInvisivel2.getStyleClass().add("delete-button");
                botaoInvisivel2.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-pref-width: 1;"
                );

                Button deleteButton = new Button("❌");
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> showDeleteConfirmation(task));
                deleteButton.setStyle(
                        "-fx-cursor: hand;" +
                                "-fx-background-color: #e57373;" +
                                "-fx-text-fill: white;" +
                                "-fx-padding: 5 10;" +
                                "-fx-margin: 0 50;" +// Defina o tamanho preferencial desejado
                                "-fx-pref-width: 40;"
                );

                HBox deleteBox = new HBox();
                deleteBox.getChildren().addAll(botaoInvisivel, editButton, botaoInvisivel2, deleteButton);
                deleteBox.setStyle("-fx-padding: 0 0 0 10;");

                // Adicione ambas as HBoxes ao VBox principal
                VBox container = new VBox();
                container.getChildren().addAll(taskBox, detailsBox,deleteBox);

                // Defina o VBox como o gráfico para a célula da lista
                setGraphic(container);

                checkBox.setOnAction(e -> {
                    setTaskCompleted(task);
                });
            }
        }

        private void showDeleteConfirmation(Task task) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this task?");

            ButtonType confirmButtonType = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(confirmButtonType, cancelButtonType);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == confirmButtonType) {
                // Usuário confirmou a exclusão
                deleteTask(task);
            }
        }

        private void deleteTask(Task task) {
            // Lógica para excluir a tarefa
            // Remova a tarefa da lista de tarefas

            String type = task.getType();
            if(type.equalsIgnoreCase("today")){
                modelManager.getTodayTasks().remove(task);
            }else if(type.equalsIgnoreCase("tomorrow")){
                modelManager.getTomorrowTasks().remove(task);
            }else if(type.equalsIgnoreCase("nospecdate")){
                modelManager.getNoSpecDateTasks().remove(task);
            }else{
                modelManager.getUserTasks().remove(task);
            }

            modelManager.deleteTask(task, true);

            // Atualize a interface gráfica
            updateTaskList();
            updateTomorrowList();
            updateNoSpecDateList();
            updateCompletedList();
        }
    }
    public class TaskAdapterNoDateList extends ListCell<Task> {

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);

            if (task == null || empty) {
                setText(null);
                setGraphic(null);
            } else {
                // Crie um HBox personalizado para exibir a tarefa
                HBox taskBox = new HBox();
                CheckBox checkBox = new CheckBox();
                checkBox.getStyleClass().add("checkbox");
                checkBox.setId("checkBox");
                Label nameLabel = new Label(" " + task.getName());

                // Use bind para sincronizar as propriedades selected e completed
                checkBox.selectedProperty().bindBidirectional(task.completedProperty());

                // Desative a caixa de seleção se a tarefa estiver completa
                checkBox.disableProperty().bind(task.completedProperty());

                taskBox.getChildren().addAll(checkBox, nameLabel);

                // Segunda HBox para exibir detalhes da tarefa
                HBox detailsBox = new HBox();
                Label timeLabel = new Label(" - " + task.getHour().getHours() + ":" + task.getHour().getMinutes());
                Label frequencyLabel = new Label(" - " + task.getFrequency());
                Label priorityLabel = new Label(" - " + task.getPriority() + " Priority");
                Label timesPerDayLabel = new Label(" - " + task.getTimesPerDay() + " Time(s) per day");

                //dateLabel.setStyle();

                detailsBox.getChildren().addAll(timeLabel, frequencyLabel, priorityLabel, timesPerDayLabel);
                detailsBox.setStyle("-fx-font-size: 8px;");

                Button botaoInvisivel = new Button("");
                botaoInvisivel.getStyleClass().add("delete-button");
                botaoInvisivel.setStyle(
                        "-fx-background-color: white;" + "-fx-pref-width: 260;"
                );

                Button editButton = new Button("Edit");
                editButton.getStyleClass().add("edit-button");
                editButton.setOnAction(event -> onEditTaskClick(task));
                editButton.setStyle(
                        "-fx-cursor: hand;" +
                                "-fx-background-color: #64b5f6;" +
                                "-fx-text-fill: white;" +
                                "-fx-padding: 5 10;" +
                                "-fx-font-family: 'Segoe UI Emoji';" +
                                "-fx-pref-width: 50;"
                );

                Button botaoInvisivel2 = new Button("");
                botaoInvisivel2.getStyleClass().add("delete-button");
                botaoInvisivel2.setStyle(
                        "-fx-background-color: white;" +
                                "-fx-pref-width: 1;"
                );

                Button deleteButton = new Button("❌");
                deleteButton.getStyleClass().add("delete-button");
                deleteButton.setOnAction(event -> showDeleteConfirmation(task));
                deleteButton.setStyle(
                        "-fx-cursor: hand;" +
                                "-fx-background-color: #e57373;" +
                                "-fx-text-fill: white;" +
                                "-fx-padding: 5 10;" +
                                "-fx-margin: 0 50;" +// Defina o tamanho preferencial desejado
                                "-fx-pref-width: 40;"
                );

                HBox deleteBox = new HBox();
                deleteBox.getChildren().addAll(botaoInvisivel, editButton, botaoInvisivel2, deleteButton);
                deleteBox.setStyle("-fx-padding: 0 0 0 10;");

                // Adicione ambas as HBoxes ao VBox principal
                VBox container = new VBox();
                container.getChildren().addAll(taskBox, detailsBox,deleteBox);

                // Defina o VBox como o gráfico para a célula da lista
                setGraphic(container);

                checkBox.setOnAction(e -> {
                    setTaskCompleted(task);
                });
            }
        }

        private void showDeleteConfirmation(Task task) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this task?");

            ButtonType confirmButtonType = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(confirmButtonType, cancelButtonType);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == confirmButtonType) {
                // Usuário confirmou a exclusão
                deleteTask(task);
            }
        }

        private void deleteTask(Task task) {
            // Lógica para excluir a tarefa
            // Remova a tarefa da lista de tarefas

            String type = task.getType();
            if(type.equalsIgnoreCase("today")){
                modelManager.getTodayTasks().remove(task);
            }else if(type.equalsIgnoreCase("tomorrow")){
                modelManager.getTomorrowTasks().remove(task);
            }else if(type.equalsIgnoreCase("nospecdate")){
                modelManager.getNoSpecDateTasks().remove(task);
            }else{
                modelManager.getUserTasks().remove(task);
            }

            modelManager.deleteTask(task, true);
            // Atualize a interface gráfica
            updateTaskList();
            updateTomorrowList();
            updateNoSpecDateList();
            updateCompletedList();
        }
    }


    ModelManager modelManager;

    EditTaskController editTaskController;
    ProfileController profileController;
    @FXML private Pane pane;
    @FXML private Text titleText;
    //@FXML private Label priorityLabel;
    //@FXML private ComboBox<String> orderBy;
    @FXML private ListView<Task> taskListView;
    @FXML private ListView<Task> tomorrowListView;
    @FXML private ListView<Task> noDateListView;
    @FXML private ListView<Task> completedListView;
    @FXML private Button addButton;
    @FXML private Button profileButton;
    @FXML private ToggleButton orderByPriority;
    @FXML private Button editButton;
    @FXML private Button undoButton;
    @FXML private CheckBox checkBox;
    @FXML private TabPane tabPane;

    @FXML
    public void initialize() throws IOException {
        this.modelManager = new ModelManager();
        modelManager.loadTasksFromFile();
        modelManager.setUserStreak(modelManager.getStreakFromFile());

        System.out.println(modelManager.getStreak());

        // Carregar o arquivo CSS para estilizar o layout
        String css = this.getClass().getResource("/com/example/gps_g12/goalTracker/css/myTasks.css").toExternalForm();
        pane.getStylesheets().add(css);

        taskListView.setCellFactory(param -> new TaskAdapter());
        tomorrowListView.setCellFactory(param -> new TaskAdapterTomorrowList());
        noDateListView.setCellFactory(param -> new TaskAdapterNoDateList());
        completedListView.setCellFactory(param -> new TaskAdapterCompleted());

        orderByPriority.setText("Order by Priority");

        titleText.setText("MY TASKS");
        addButton.setText("Add Task");

        undoButton = new Button("Undo");
        undoButton.setOnAction(e -> undoButtonClick());

        createViews();
        registerHandlers();
        update();
    }

    private void undoButtonClick() {
        // Handle the undo logic here
        Task completedTask = completedListView.getSelectionModel().getSelectedItem();
        if (completedTask != null) {
            undoTaskCompletion(completedTask);
        }
    }

    private void createViews() throws IOException {
        loadAddTaskView();
        loadEditTaskView();
        loadProfileView();
    }

    private void loadAddTaskView() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(GoalTrackerApplication.class.getResource("/com/example/gps_g12/goalTracker/taskGoalIntroduction.fxml"));
        Parent child = fxmlLoader.load();
        taskGoalIntroductionController addScreenController = fxmlLoader.getController();
        addScreenController.setModelManager(this.modelManager);
        addScreenController.registerHandlers();
        pane.getChildren().add(child);
    }

    private void loadEditTaskView(/*Task selectedTask*/) throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/gps_g12/goalTracker/editTask-view.fxml"));
        Parent editTaskView = fxmlLoader.load();
        editTaskController = fxmlLoader.getController();
        editTaskController.setModelManager(modelManager);
        editTaskController.registerHandlers();
        pane.getChildren().add(editTaskView);
    }

    private void loadProfileView() throws IOException{

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/gps_g12/goalTracker/profile.fxml"));
        Parent profileView = fxmlLoader.load();
        profileController = fxmlLoader.getController();
        profileController.setModelManager(modelManager);
        profileController.registerHandlers();
        pane.getChildren().add(profileView);
    }

    private void registerHandlers(){
        modelManager.addPropertyChangeListener(evt -> { update(); });

        orderByPriority.setOnAction(evt -> {
            //updateTaskList();
            update();
        });
    }

    private void update(){
        if(modelManager != null){

            updateTaskList();
            updateTomorrowList();
            updateNoSpecDateList();
            updateCompletedList();

            taskListView.setVisible(modelManager.getState() == EState.LIST_TASKS);
            tomorrowListView.setVisible(modelManager.getState() == EState.LIST_TASKS);
            noDateListView.setVisible(modelManager.getState() == EState.LIST_TASKS);
            orderByPriority.setVisible(modelManager.getState() == EState.LIST_TASKS);
            addButton.setVisible(modelManager.getState() == EState.LIST_TASKS);
            titleText.setVisible(modelManager.getState() == EState.LIST_TASKS);
            //priorityLabel.setVisible(modelManager.getState() == EState.LIST_TASKS);
            if(checkBox != null)
                checkBox.setVisible(modelManager.getState() == EState.LIST_TASKS);
            tabPane.setVisible(modelManager.getState() == EState.LIST_TASKS);
            completedListView.setVisible(modelManager.getState() == EState.LIST_TASKS);
            profileButton.setVisible(modelManager.getState() == EState.LIST_TASKS);
        }else{
            taskListView.setVisible(false);
            tomorrowListView.setVisible(false);
            noDateListView.setVisible(false);
            orderByPriority.setVisible(false);
            addButton.setVisible(false);
            titleText.setVisible(false);
            // priorityLabel.setVisible(false);
            if(checkBox != null)
                checkBox.setVisible(false);
            tabPane.setVisible(false);
            completedListView.setVisible(false);
            profileButton.setVisible(false);
        }
    }

    @FXML
    public void openProfile(){
        modelManager.changeState(EState.PROFILE);
    }

    public void onEditTaskClick(Task selectedTask) {
        //Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
        /*if (selectedTask != null) {
            loadEditTaskView(selectedTask);
        }*/
        editTaskController.setTaskToEdit(selectedTask);
        modelManager.changeState(EState.EDIT_TASK);
    }

    //functions to use on FXML
    public void onAddTaskClick(){
        modelManager.changeState(EState.ADD_TASKS);
    }

    public void undoTaskCompletion(Task completedTask) {
        modelManager.getCompletedTasks().remove(completedTask);

        String type = completedTask.getType();
        if(type.equalsIgnoreCase("today")){
            modelManager.getTodayTasks().add(completedTask);
        }else if(type.equalsIgnoreCase("tomorrow")){
            modelManager.getTomorrowTasks().add(completedTask);
        }else if(type.equalsIgnoreCase("nospecdate")){
            modelManager.getNoSpecDateTasks().add(completedTask);
        }else{
            modelManager.getUserTasks().add(completedTask);
        }

        modelManager.undoTaskCompletion(completedTask);

        //modelManager.setUserStreak(modelManager.getStreak() - 1);
        //modelManager.streakOnFile(modelManager.getStreak());

        // Atualize a interface gráfica
        updateTaskList();
        updateTomorrowList();
        updateNoSpecDateList();
        updateCompletedList();
        completedTask.setCompleted(false);
    }

    private void updateTaskList() { //today
        //List<Task> tasks = modelManager.getUserTasks();
        boolean isSelectedPriority = orderByPriority.isSelected();

        //System.out.println(selectedPriority);

        List<Task> tasks = modelManager.getTodayTasks();

        if (isSelectedPriority) {
            tasks = tasks.stream()
                    .filter(task -> task.getPriority().equalsIgnoreCase("high"))
                    .collect(Collectors.toList());
        }


        ObservableList<Task> taskObservableList = FXCollections.observableArrayList(tasks);
        taskListView.setItems(taskObservableList);
    }

    private void updateTomorrowList(){
        boolean isSelectedPriority = orderByPriority.isSelected();

        //System.out.println(selectedPriority);

        List<Task> tomorrowTasks = modelManager.getTomorrowTasks();

        //TODO review later
        if (isSelectedPriority) {
            tomorrowTasks = tomorrowTasks.stream()
                    .filter(task -> task.getPriority().equalsIgnoreCase("high"))
                    .collect(Collectors.toList());
        }

        ObservableList<Task> tomorrowTaskObservableList = FXCollections.observableArrayList(tomorrowTasks);
        tomorrowListView.setItems(tomorrowTaskObservableList);
    }

    private void updateNoSpecDateList(){
        boolean isSelectedPriority = orderByPriority.isSelected();

        //System.out.println(selectedPriority);

        List<Task> noSpecDateTasks = modelManager.getNoSpecDateTasks();

        //TODO review later
        if (isSelectedPriority) {
            noSpecDateTasks = noSpecDateTasks.stream()
                    .filter(task -> task.getPriority().equalsIgnoreCase("high"))
                    .collect(Collectors.toList());
        }

        ObservableList<Task> noSpecDateTaskObservableList = FXCollections.observableArrayList(noSpecDateTasks);
        noDateListView.setItems(noSpecDateTaskObservableList);
    }

    private void updateCompletedList() {
        List<Task> completedTasks = modelManager.getCompletedTasks();
        ObservableList<Task> completedObservableList = FXCollections.observableArrayList(completedTasks);
        completedListView.setItems(completedObservableList);
    }

    public void setTaskCompleted(Task selectedTask) {
        selectedTask.setCompleted(true);

        // Adicionar a tarefa à completedListView
        completedListView.getItems().add(selectedTask);

        // Remover a tarefa da taskListView
        taskListView.getItems().remove(selectedTask);
        tomorrowListView.getItems().remove(selectedTask);
        noDateListView.getItems().remove(selectedTask);

        // Atualizar a interface gráfica
        updateTaskList();
        updateTomorrowList();
        updateNoSpecDateList();
        updateCompletedList();

        modelManager.addToCompletedTask(selectedTask);
    }

    /*public void undoTaskCompletion(Task completedTask) {
        completedTask.setCompleted(false);

        modelManager.getCompletedTasks().remove(completedTask);

        String type = completedTask.getType();
        if(type.equalsIgnoreCase("today")){
            modelManager.getTodayTasks().add(completedTask);
            //modelManager.addToTodayTasks(completedTask);
        }else if(type.equalsIgnoreCase("tomorrow")){
            modelManager.getTomorrowTasks().add(completedTask);
            //modelManager.addToTomorrowTasks(completedTask);
        }else if(type.equalsIgnoreCase("nospecdate")){
            modelManager.getNoSpecDateTasks().add(completedTask);
            //modelManager.addToNoSpecDateTasks(completedTask);
        }else{
            modelManager.getUserTasks().add(completedTask);
            //modelManager.addNewTask(completedTask);
        }

        modelManager.undoTaskCompletion(completedTask);
        // Atualize a interface gráfica
        updateTaskList();
        updateTomorrowList();
        updateNoSpecDateList();
        updateCompletedList();
    }*/

    private void applyFadeOutTransition(ListView<Task> listView, Task task) {
        // Encontrar a célula da tarefa na lista
        int index = listView.getItems().indexOf(task);
        ListCell<Task> cell = listView.getCellFactory().call(listView);

        // Criar uma transição de fade para a célula
        FadeTransition fadeOutTransition = new FadeTransition(Duration.seconds(1), cell);
        fadeOutTransition.setFromValue(1.0);
        fadeOutTransition.setToValue(0.0);

        // Evento para ocorrer quando a transição terminar
        fadeOutTransition.setOnFinished(event -> {
            // Atualizar a interface gráfica
            updateTaskList();
            updateCompletedList();
        });

        // Iniciar a transição
        fadeOutTransition.play();
    }

}

//addButton.getStyleableNode().setStyle("-fx-background-color: red");

//priorityComboBox.getItems().set(0, "Ola");

//priorityComboBox.getStyleClass().add("custom-combo-box");

//MUDAR A COR DE UM ITEM DA ARRAY LIST
        /*priorityComboBox.setCellFactory(param -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                } else {
                    setText(item);

                    // Check if this is the item you want to style differently
                    if ("High Priority".equals(item)) {
                        setStyle("-fx-text-fill: red; -fx-font-size: 18px;");
                    }
                }
            }
        });*/

        /*
            List<SeuTipo> seusElementos = modelManager.getSeusElementos();

            ObservableList<SeuTipo> elementosObservable = FXCollections.observableArrayList(seusElementos);

            taskListView.setItems(elementosObservable);
         */

