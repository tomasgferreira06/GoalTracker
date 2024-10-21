package com.example.gps_g12.goalTracker.controller;

import com.example.gps_g12.goalTracker.controller.MyTasksController;
import com.example.gps_g12.goalTracker.model.ModelManager;
import com.example.gps_g12.goalTracker.model.data.Date;
import com.example.gps_g12.goalTracker.model.data.Hour;
import com.example.gps_g12.goalTracker.model.data.Task;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

class MyTasksControllerTest extends ApplicationTest {

    /*private MyTasksController controller;

    @BeforeAll
    static void setUpHeadlessMode() {
        // Configuração do JavaFX para modo headless (sem GUI)
        Platform.startup(() -> new JFXPanel());
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gps_g12/goalTracker/MyTasksController.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        stage.toFront();

        // Obtenha a referência do controlador
        controller = loader.getController();
    }

    @Test
    void initialize() {
        // Teste se a inicialização ocorre sem exceções
        assertDoesNotThrow(() -> controller.initialize());
    }

    @Test
    void onAddTaskClick() {
        // Teste se a mudança de estado ocorre sem exceções
        assertDoesNotThrow(() -> controller.onAddTaskClick());
        // Verifique se o estado foi alterado corretamente
        assertEquals(EState.ADD_TASKS, controller.modelManager.getState());
    }

    @Test
    void setTaskCompleted() {
        // Crie uma instância de ModelManager para simular o comportamento
        ModelManager modelManager = new ModelManager();
        controller.modelManager = modelManager;

        // Certifique-se de que não há tarefas concluídas inicialmente
        assertTrue(modelManager.getCompletedTasks().isEmpty());

        // Simule uma tarefa selecionada no ListView
        Task task = new Task("Test Task", new Date(), new Hour(), "Once", "High", 1, false);
        controller.taskListView.getItems().add(task);
        controller.taskListView.getSelectionModel().select(task);

        // Chame o método setTaskCompleted
        controller.setTaskCompleted(task);

        // Verifique se a tarefa foi movida para a lista de tarefas concluídas
        assertFalse(modelManager.getCompletedTasks().isEmpty());
        assertTrue(modelManager.getCompletedTasks().contains(task));
        assertFalse(controller.taskListView.getItems().contains(task));
    }*/
}
