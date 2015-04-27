package com.mycompany.ofytest;

import android.app.Application;

import com.example.ilay.myapplication.backend.trempitApi.model.TrempitUser;

/**
 * Created by Lee on 4/27/2015.
 */
public class GlobalState extends Application {

    private TrempitUser currentUser;

    public TrempitUser getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(TrempitUser currentUser) {
        this.currentUser = currentUser;
    }

}
