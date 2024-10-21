package com.example.gps_g12.goalTracker.model;

import com.example.gps_g12.goalTracker.model.data.Task;
import com.example.gps_g12.goalTracker.model.fsm.DataContext;
import com.example.gps_g12.goalTracker.model.fsm.EState;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ModelManager {

    private DataContext dataContext;

    PropertyChangeSupport pcs;

    public ModelManager(){
        dataContext = new DataContext();
        pcs = new PropertyChangeSupport(this);
    }

    public EState getState() {
        return dataContext.getState();
    }

    public void changeState(EState newState) {
        dataContext.setState(newState);
        System.out.println(newState);
        pcs.firePropertyChange(null, null, null);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){pcs.addPropertyChangeListener(listener);}

    public ArrayList<Task> getUserTasks(){
        return dataContext.getAppData().getUserTasks();
    }

    public ArrayList<Task> getUserOrderedTasks(){
        return dataContext.getAppData().getUserOrderedTasks();
    }

    public void addNewTask(Task newTask){
        dataContext.getAppData().addNewTask(newTask);
        pcs.firePropertyChange(null, null, null);
    }

    public void addToCompletedTask(Task completedTask){
        dataContext.getAppData().addTaskToCompleted(completedTask);
        pcs.firePropertyChange(null, null, null);
    }

    public void addToOrderedTasks(Task newOrderedTask){
        dataContext.getAppData().addToOrderedTasks(newOrderedTask);
        pcs.firePropertyChange(null, null, null);
    }

    public void addToTodayTasks(Task newTodayTask){
        dataContext.getAppData().addToTodayTasks(newTodayTask);
        pcs.firePropertyChange(null, null, null);
    }

    public void addToTomorrowTasks(Task newTomorrowTask){
        dataContext.getAppData().addToTomorrowTasks(newTomorrowTask);
        pcs.firePropertyChange(null, null, null);
    }

    public void addToNoSpecDateTasks(Task newNoSpecDateTask){
        dataContext.getAppData().addToNoSpecDateTasks(newNoSpecDateTask);
        pcs.firePropertyChange(null, null, null);
    }

    public void undoTaskCompletion(Task task){
        dataContext.getAppData().markTaskAsUnCompletedInFile(task);
        pcs.firePropertyChange(null, null, null);
    }

    public void editTaskOnFile(Task taskToEdit){
        dataContext.getAppData().editTaskOnFile(taskToEdit);
        pcs.firePropertyChange(null, null, null);
    }

    public void deleteTask(Task task, boolean deleteFirstFlag){
        dataContext.getAppData().deleteTaskFromFile(task, deleteFirstFlag);
        pcs.firePropertyChange(null, null, null);
    }

    public void loadTasksFromFile(){
        dataContext.getAppData().loadTasksFromFile();
        pcs.firePropertyChange(null, null, null);
    }

    public void streakOnFile(int newStreak){
        dataContext.getAppData().streakOnFile(newStreak);
        pcs.firePropertyChange(null, null, null);
    }

    public ArrayList<Task> getCompletedTasks(){
        return dataContext.getAppData().getCompletedTasks();
    }
    public ArrayList<Task> getTodayTasks(){
        return dataContext.getAppData().getUserTodayTasks();
    }
    public ArrayList<Task> getTomorrowTasks(){
        return dataContext.getAppData().getUserTomorrowTasks();
    }
    public ArrayList<Task> getNoSpecDateTasks(){
        return dataContext.getAppData().getUserNoSpecDateTasks();
    }

    public int getStreak(){ return dataContext.getAppData().getStreak(); }

    public void setUserStreak(int newStreak){ dataContext.getAppData().setUserStreak(newStreak); }

    public int getStreakFromFile() {
        return dataContext.getAppData().getStreakFromFile();
    }
}
