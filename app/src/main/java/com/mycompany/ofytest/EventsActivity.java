package com.mycompany.ofytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.example.ilay.myapplication.backend.trempitApi.model.TrempitUser;

//import com.example.Ilay.myapplication.backend.TrempitUser;

/**
* Created by Harel on 26/04/2015.
*/
public class EventsActivity extends ActionBarActivity {

    TrempitUser user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Intent intent = getIntent();
        //user = (TrempitUser)intent.getSerializableExtra(MainActivity.USER);
        TextView username = (TextView) findViewById(R.id.username);
        //username.setText(user.getFullName());
    }
}
