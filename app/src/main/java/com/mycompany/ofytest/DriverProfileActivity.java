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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ilay.myapplication.backend.trempitApi.TrempitApi;
import com.example.ilay.myapplication.backend.trempitApi.model.Driver;
import com.example.ilay.myapplication.backend.trempitApi.model.TrempitUser;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
//import com.example.ilay.myapplication.backend.trempitUserApi.model.TrempitUser;

//import com.example.Ilay.myapplication.backend.Driver;
//import com.example.Ilay.myapplication.backend.TrempitUser;


/**
* Created by Harel on 24/04/2015.
*/
public class DriverProfileActivity extends ActionBarActivity {

    long driverId;
    long eventId;
    Driver driver;
    long passengerId;

    TrempitUser currentUser;
    GlobalState globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverprofile);

        globalState = (GlobalState) getApplicationContext();

        Intent intent = getIntent();
        driverId = (long) intent.getLongExtra("driverid", -1);
        eventId = (long) intent.getLongExtra("eventid", -1);
        passengerId = (long) intent.getLongExtra("passengerid", -1);
        Log.d("TrempIt", String.valueOf(driverId));
        currentUser = globalState.getCurrentUser();
        displayDriverData();
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

    public void sendRequestOnClick(View view) {
        sendPassengerRequestToCurrentDriver(passengerId);
    }





    public void displayDriverData() {
        new EndpointsAsyncTask(this).executeOnExecutor(EndpointsAsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void sendPassengerRequestToCurrentDriver(long passengerId) {
        Log.d("TrempIt", "DriversProfileActivity " + String.valueOf(eventId));
        new RequestEndpointsAsyncTask(this).executeOnExecutor(EndpointsAsyncTask.THREAD_POOL_EXECUTOR, passengerId);
    }

    class EndpointsAsyncTask extends AsyncTask<Void, Void, Driver> {
        private TrempitApi myApiService = null;
        private Context context;

        EndpointsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Driver doInBackground(Void... params) {
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
                return myApiService.getDriver(driverId).execute();
            } catch (IOException e) {
                Log.d("Trempit", "IO error");
                return null;
            }
        }

        @Override
        protected void onPostExecute(Driver result) {
//            for (Event q : result) {
//                List<Passenger> passlist= q.getPassengerList();
//                Toast.makeText(context, passlist.get(0).getFullName() + " : " + q.getTitle(), Toast.LENGTH_LONG).show();
//
//            }
            if (result == null || result.isEmpty()) {
                Log.d("TrempIt", "No currDriver retrieved");
                return;
            }

            driver = result;

            TextView username = (TextView) findViewById(R.id.username);
            username.setText(driver.getFullName());

            TextView availableSeats = (TextView) findViewById(R.id.availableSeats);
            int numberOfPassengers;
            if (driver.getPassengerList() == null) {
                numberOfPassengers = 0;
            }
            else {
                numberOfPassengers = driver.getPassengerList().size();
            }
            int numberOfSeats = driver.getAvailableSeats() - numberOfPassengers;
            availableSeats.setText("available seats: " + numberOfSeats);

            TextView startingLocation = (TextView) findViewById(R.id.startingLocation);
            startingLocation.setText(TrempitUtils.parseLocation(driver.getStartingLocation()));

            TextView arrivalTime = (TextView) findViewById(R.id.arrivalTime);
            arrivalTime.setText(TrempitUtils.parseDateTime(driver.getArrivalTime()));


        }
    }


    class RequestEndpointsAsyncTask extends AsyncTask<Long, Void, Boolean> {
        private TrempitApi myApiService = null;
        private Context context;

        RequestEndpointsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Long... params) {
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
                //TODO: figure out how to extract passenger id from trempitUser id: serverside or clientside
                Log.d("TrempIt", "RequestAsyncTask " + String.valueOf(eventId));
                myApiService.addPassengerRequest(driverId, params[0]).execute();

                return Boolean.TRUE;
            } catch (IOException e) {
                Log.d("Trempit", "IO error");
                return Boolean.FALSE;
            }

        }

        @Override
        protected void onPostExecute(Boolean param) {
            if (param == Boolean.TRUE) { // the request succeeded
                Toast.makeText(context, "Request sent to Driver!", Toast.LENGTH_LONG).show();
            }
            else { // the request failed
                Toast.makeText(context, "failed to make request", Toast.LENGTH_LONG).show();
            }
        }




        }















    }




