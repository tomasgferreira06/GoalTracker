package com.example.gps_g12.goalTracker.model.data;

import com.example.gps_g12.goalTracker.model.data.Task;

import java.util.ArrayList;

public class User {

    private String name;
    private String email;
    private String password;
    public ArrayList<Task> tasks; //Tasks with date but it is neither today or tommorow
    public ArrayList<Task> todayTasks; //today Tasks
    public ArrayList<Task> tomorrowTasks; //tomorrow Tasks
    public ArrayList<Task> noSpecificDateTasks; //no specific date Tasks
    public ArrayList<Task> completedTasks;

    public ArrayList<Task> orderedTasks;

    private int streak;

    public User(){
        this.tasks                  = new ArrayList<>();
        this.todayTasks             = new ArrayList<>();
        this.tomorrowTasks          = new ArrayList<>();
        this.noSpecificDateTasks    = new ArrayList<>();
        this.completedTasks         = new ArrayList<>();
        this.orderedTasks           = new ArrayList<>();
        this.streak                 = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Task> getTasks(){return this.tasks;}

    public void addNewTask(Task newTask){
        this.tasks.add(newTask);
    }

    public void addTaskToCompleted(Task completedTask){
        this.completedTasks.add(completedTask);

        todayTasks.removeIf(task -> completedTask == task);
        tomorrowTasks.removeIf(task -> completedTask == task);
        noSpecificDateTasks.removeIf(task -> completedTask == task);
    }

    public void addToOrderedTasks(Task newOrderedTask){
        this.orderedTasks.add(newOrderedTask);
    }
    public void addToTodayTasks(Task newTodayTask){this.todayTasks.add(newTodayTask);}
    public void addToTomorrowTasks(Task newTomorrowTask){
        this.tomorrowTasks.add(newTomorrowTask);
    }
    public void addToNoSpecDateTasks(Task newNoSpecDateTask){this.noSpecificDateTasks.add(newNoSpecDateTask);}

    public ArrayList<Task> getCompletedTasks(){return this.completedTasks;}

    public ArrayList<Task> getOrderedTasks() {
        return orderedTasks;
    }
    public ArrayList<Task> getTodayTasks() {
        return todayTasks;
    }

    public ArrayList<Task> getTomorrowTasks() {
        return tomorrowTasks;
    }

    public ArrayList<Task> getNoSpecDateTasks() {
        return noSpecificDateTasks;
    }

    public int getStreak(){ return this.streak; }

    public void setStreak(int newStreak){ this.streak = newStreak; }

}
