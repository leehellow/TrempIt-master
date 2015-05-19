package com.mycompany.ofytest;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.ilay.myapplication.backend.trempitApi.TrempitApi;
import com.example.ilay.myapplication.backend.trempitApi.model.TrempitUser;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;


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

    // updates the currentUser global variable from the facebook profile, and inserts to backend server
    public void updateCurrentTrempitUser() {

        // create the trempituser object
        TrempitUser currentUser = new TrempitUser();
        currentUser.setFullName(Profile.getCurrentProfile().getName());
        currentUser.setId(Long.valueOf(Profile.getCurrentProfile().getId()));

        // update the global variable so all activities can access the user
        globalState.setCurrentUser(currentUser);

        // insert the user to the server
        insertTrempitUserToServer(currentUser);

        Toast.makeText(this, "logged in to facebook:" + globalState.getCurrentUser().getFullName(), Toast.LENGTH_LONG).show();
    }

    // adds the user to the backend server
    private void insertTrempitUserToServer(TrempitUser trempitUser) {
        new addTrempitUserEndpointsAsyncTask(this).executeOnExecutor(addTrempitUserEndpointsAsyncTask.THREAD_POOL_EXECUTOR, trempitUser);
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





    class addTrempitUserEndpointsAsyncTask extends AsyncTask<TrempitUser, Void, Boolean> {
        private TrempitApi myApiService = null;
        private Context context;

        addTrempitUserEndpointsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(TrempitUser... params) {
            if(myApiService == null) {  // Only do this once
                TrempitApi.Builder builder = new TrempitApi.Builder(AndroidHttp.newCompatibleTransport(),
                        new AndroidJsonFactory(), null)
                        // options for running against local devappserver
                        // - 10.0.2.2 is localhost's IP address in Android emulator
                        // - turn off compression when running against local devappserver
                        .setRootUrl(TrempitConstants.SERVERPATH).setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        }).setApplicationName("Trempit");
                // end options for devappserver

                myApiService = builder.build();
            }

            try {
                myApiService.insertTrempitUser(params[0]).execute();

            } catch (IOException e) {
                Log.d("Trempit", "IO error");
                return Boolean.FALSE;
            }

            return Boolean.FALSE.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean param) {
            if (param == Boolean.FALSE) {
                Toast.makeText(context, "failed to create user" , Toast.LENGTH_LONG).show();
            }

        }


    }
}
