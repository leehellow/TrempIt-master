package com.mycompany.ofytest;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ilay.myapplication.backend.trempitApi.model.Location;
import com.example.ilay.myapplication.backend.trempitApi.model.TrempitUser;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity {

    Map<String,TrempitUser> userList;
    public final static String USER = "com.googleproject.trempit.USER";

    public void login(View view){

        TextView error = (TextView) findViewById(R.id.loginError);
        Intent intent = new Intent(this, HarelTestActivity.class);
        String username = ((TextView) findViewById(R.id.loginUsername)).getText().toString();

        if (username == null){
            error.setText("Invalid username");
    }

    else if(userList.containsKey(username)){
        intent.putExtra(USER, userList.get(username).getId()); //TODO: get user from id using API in the next activity
        startActivity(intent);
    }
    else{
        error.setText("User does not exist");
    }
}

    public void signup(View view){
        TextView error = (TextView) findViewById(R.id.signupError);
        String username = ((TextView) findViewById(R.id.signupUsername)).getText().toString();
        error.setText("Button clicked");
        if (username == null){
            error.setText("Invalid Username");
        }
        else if(userList.containsKey(username)){
            error.setText("User already exists");
        }
        else{
            String country = ((EditText) findViewById(R.id.countryTextfield)).getText().toString();
            String city = ((EditText) findViewById(R.id.cityTextfield)).getText().toString();
            String street = ((EditText) findViewById(R.id.streetTextfield)).getText().toString();
            int houseNumber = Integer.parseInt(((EditText) findViewById(R.id.numberTextfield)).getText().toString());
            Location home = new Location();
            home.setCity(city);
            home.setCountry(country);
            home.setStreet(street);
            TrempitUser user = new TrempitUser();
            user.setFullName(username);
            user.setHomeLocation(home);
            userList.put(username,user);
            error.setText("Signup successed!");
            error.setTextColor(0x00FF00);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        userList = new HashMap<String,TrempitUser>();
        TextView error = (TextView) findViewById(R.id.signupError);
        error.setText("Button was not clicked");
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

