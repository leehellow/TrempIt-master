package com.mycompany.ofytest;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ilay.myapplication.backend.attenderApi.model.TrempitUser;
import com.example.ilay.myapplication.backend.trempitApi.model.Driver;
//import com.example.ilay.myapplication.backend.trempitUserApi.model.TrempitUser;

//import com.example.Ilay.myapplication.backend.Driver;
//import com.example.Ilay.myapplication.backend.TrempitUser;


/**
* Created by Harel on 24/04/2015.
*/
public class DriverProfileActivity extends ActionBarActivity {

    TrempitUser user;
    Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverprofile);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

