//package com.mycompany.ofytest;
//
//import android.support.v7.app.ActionBarActivity;
//import android.os.Bundle;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.content.Intent;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.example.ilay.myapplication.backend.trempitApi.model.TrempitUser;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class MainActivity extends ActionBarActivity {
//
//    Map<String,TrempitUser> userList;
//    public final static String USER = "com.googleproject.trempit.USER";
//
//    public void login(View view){
//
//        TextView error = (TextView) findViewById(R.id.loginError);
//        Intent intent = new Intent(this, EventsActivity.class);
//        String username = ((EditText) findViewById(R.id.loginUsername)).getText().toString();
//
//        if (username == null){
//            error.setText("Invalid username");
//        }
//
//        else if(userList.containsKey(username)){
//            intent.putExtra(USER, userList.get(username));
//            startActivity(intent);
//        }
//        else{
//            error.setText("User does not exist");
//        }
//    }
//
//    public void signup(View view){
//        TextView error = (TextView) findViewById(R.id.signupError);
//        String username = ((EditText) findViewById(R.id.signupUsername)).getText().toString();
//        error.setText("Button clicked");
//        if (username == null){
//            error.setText("Invalid Username");
//        }
//        else if(userList.containsKey(username)){
//            error.setText("User already exists");
//        }
//        else{
//            String country = ((EditText) findViewById(R.id.countryTextfield)).getText().toString();
//            String city = ((EditText) findViewById(R.id.cityTextfield)).getText().toString();
//            String street = ((EditText) findViewById(R.id.streetTextfield)).getText().toString();
//            int houseNumber = Integer.parseInt(((EditText) findViewById(R.id.numberTextfield)).getText().toString());
//            Location home = new Location(country,city,street);
//            TrempitUser user = new TrempitUser(username,home);
//            userList.put(username,user);
//            error.setText("Signup successed!");
//            error.setTextColor(0x00FF00);
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_trempit);
//        userList = new HashMap<String,TrempitUser>();
//        TextView error = (TextView) findViewById(R.id.signupError);
//        error.setText("Button was not clicked");
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}
//
