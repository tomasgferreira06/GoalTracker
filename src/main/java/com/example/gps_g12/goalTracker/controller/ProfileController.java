package com.example.gps_g12.goalTracker.controller;

import com.example.gps_g12.goalTracker.model.fsm.EState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import com.example.gps_g12.goalTracker.model.ModelManager;

import java.io.IOException;

public class ProfileController {

    ModelManager modelManager;

    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private Text completedTaskText;

    @FXML
    private Text trophy_1, trophy_2, trophy_3, trophy_4, trophy_5, trophy_6, trophy_7, trophy_8, trophy_9, trophy_10, trophy_11, trophy_12, trophy_13, trophy_14, trophy_15;

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

    @FXML
    private void handleBackButton() {
        modelManager.changeState(EState.LIST_TASKS);
    }

    private void updateStreakText(){
        completedTaskText.setText(String.valueOf(modelManager.getStreak()));
    }

    private void update(){

        updateStreakText();

        if (modelManager != null) {
            anchorPane.setVisible(modelManager.getState() == EState.PROFILE);
            completedTaskText.setVisible(modelManager.getState() == EState.PROFILE);
            updateTrophyVisibility(modelManager.getStreak());
        } else {
            anchorPane.setVisible(false);
            completedTaskText.setVisible(false);
        }

    }

    private void updateTrophyVisibility(int streak) {
        System.out.println(streak);
        trophy_1.setVisible(streak >= 5); // Torna o primeiro troféu visível quando o streak é 10 ou mais
        trophy_2.setVisible(streak >= 10); // Exemplo para o segundo troféu
        trophy_3.setVisible(streak >= 15); // Exemplo para o segundo troféu
        trophy_4.setVisible(streak >= 20); // Exemplo para o segundo troféu
        trophy_5.setVisible(streak >= 25); // Exemplo para o segundo troféu
        trophy_6.setVisible(streak >= 30); // Exemplo para o segundo troféu
        trophy_7.setVisible(streak >= 35); // Exemplo para o segundo troféu
        trophy_8.setVisible(streak >= 40); // Exemplo para o segundo troféu
        trophy_9.setVisible(streak >= 45); // Exemplo para o segundo troféu
        trophy_10.setVisible(streak >= 50); // Exemplo para o segundo troféu
        trophy_11.setVisible(streak >= 55); // Exemplo para o segundo troféu
        trophy_12.setVisible(streak >= 60); // Exemplo para o segundo troféu
        trophy_13.setVisible(streak >= 65); // Exemplo para o segundo troféu
        trophy_14.setVisible(streak >= 70); // Exemplo para o segundo troféu
        trophy_15.setVisible(streak >= 75); // Exemplo para o segundo troféu
    }

}
