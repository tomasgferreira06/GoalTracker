package com.example.gps_g12.goalTracker.model.fsm;

import com.example.gps_g12.goalTracker.model.data.Data;
import com.example.gps_g12.goalTracker.model.data.User;

public class DataContext {

    Data appData;
    EState state;

    public DataContext(){
        this.appData = new Data();
        this.state = EState.LIST_TASKS;
    }

    public Data getAppData() {
        return appData;
    }

    public void setAppDataUser(Data newAppData) {
        this.appData = newAppData;
    }

    public EState getState() {
        return state;
    }

    public void setState(EState newState) {
        this.state = newState;
    }
}
