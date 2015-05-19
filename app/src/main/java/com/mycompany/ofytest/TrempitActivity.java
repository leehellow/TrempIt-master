package com.mycompany.ofytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ilay.myapplication.backend.trempitApi.model.TrempitUser;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


public class TrempitActivity extends ActionBarActivity {

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private AccessToken accessToken;
    GlobalState globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_trempit);

        globalState = (GlobalState) getApplicationContext();

        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");

        Log.d("Trempit", "before");
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Trempit", "success");
                accessToken = loginResult.getAccessToken();
                updateCurrentTrempitUser();
            }

            @Override
            public void onCancel() {
                Log.d("Trempit", "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("Trempit", "error");
            }
        });
    }

    public void updateCurrentTrempitUser() {
        TrempitUser currentUser = new TrempitUser();
        currentUser.setFullName(Profile.getCurrentProfile().getName());

        globalState.setCurrentUser(currentUser);
        Toast.makeText(this, "logged in to facebook:" + globalState.getCurrentUser().getFullName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trempit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void goToEvents(View view) {
        Intent intent = new Intent(this,EventsActivity.class);
        startActivity(intent);
    }

    public void goToNotifications(View view) {
        Intent intent = new Intent(this,RequestsActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
