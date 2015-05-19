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
import android.widget.ListView;
import android.widget.Toast;

import com.example.ilay.myapplication.backend.trempitApi.TrempitApi;
import com.example.ilay.myapplication.backend.trempitApi.model.Driver;
import com.example.ilay.myapplication.backend.trempitApi.model.Event;
import com.example.ilay.myapplication.backend.trempitApi.model.Location;
import com.example.ilay.myapplication.backend.trempitApi.model.Passenger;
import com.example.ilay.myapplication.backend.trempitApi.model.TrempitUser;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
* Created by Harel on 24/04/2015.
*/
public class DriversActivity extends ActionBarActivity{

    TrempitUser user;
    Event event;

    ArrayList<Driver> drivers = new ArrayList<>();
    DriverAdapter driverAdapter;
    TrempitUser currentUser;
    Passenger currPassenger;
    Driver currDriver;
    Long eventId;

    //for map activity
    float eventLat;
    float eventLng;

    GlobalState globalState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);

        globalState = (GlobalState) getApplicationContext();
        currentUser = globalState.getCurrentUser();

        currPassenger = createPassengerFromTrempitUser(currentUser);
        insertPassengerToServer(currPassenger);

        Intent intent = getIntent();
        eventId = (Long) intent.getLongExtra("event", -1);

        // for map activity
        eventLat = intent.getFloatExtra("eventLat", (float) -1.0);
        eventLng = intent.getFloatExtra("eventLng", (float) -1.0);

        //Log.d("TrempIt", String.valueOf(currentUser.getId()));

        driverAdapter  = new DriverAdapter(this, drivers);
        //driverAdapter.add(new Driver());
        refreshActivity(findViewById(R.id.requestlistview));

        final ListView listView = (ListView) findViewById(R.id.driverlistview);
        Log.d("TrempIt", listView.toString());
        listView.setAdapter(driverAdapter);

    }

    public void refreshActivity(View view) {
        new EndpointsAsyncTask(this).executeOnExecutor(EndpointsAsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public void startDriverActivity(View view) {
        Driver driver = (Driver) view.getTag();
        Log.d("TrempIt", "Driver name: " + driver.getFullName());
        Intent intent = new Intent(this, DriverProfileActivity.class);
        intent.putExtra("driverid", driver.getId());
        intent.putExtra("eventid", eventId);
        intent.putExtra("passengerid" , currPassenger.getId());
        Log.d("TrempIt", "DriversActivity " + String.valueOf(eventId));
        startActivity(intent);
    }

    public void driverSignupOnClick(View view) {
        removePassengerFromServer(currPassenger.getId());
        Driver driver = createDriverFromTrempitUser(currentUser);
        insertDriverToServer(driver);
        driverAdapter.add(driver);
    }

    class EndpointsAsyncTask extends AsyncTask<Void, Void, List<Driver>> {
        private TrempitApi myApiService = null;
        private Context context;

        EndpointsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<Driver> doInBackground(Void... params) {
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
                Driver driver = new Driver();
                driver.setFullName("Eran Nahag");
                driver.setId((long) 10000);
                Location location = new Location();
                location.setId((long) 1000);
                location.setCity("Tel Aviv");
                location.setStreet("Dizengoff");
                driver.setStartingLocation(location);
                //myApiService.insertDriver(driver).execute();
                //myApiService.addDriverToEvent(driver.getId(), eventId).execute();
                return myApiService.listEventDrivers(eventId).execute().getItems();
            } catch (IOException e) {
                Log.d("Trempit", "IO error");
                return Collections.EMPTY_LIST;
            }
        }

        @Override
        protected void onPostExecute(List<Driver> result) {
//            for (Event q : result) {
//                List<Passenger> passlist= q.getPassengerList();
//                Toast.makeText(context, passlist.get(0).getFullName() + " : " + q.getTitle(), Toast.LENGTH_LONG).show();
//
//            }

            if (result == null || result.isEmpty()) {
                Log.d("TrempIt", "No drivers retrieved");
                return;
            }

            driverAdapter.clear();
            driverAdapter.addAll(result);
            Log.d("TrempIt", "after post");
            driverAdapter.notifyDataSetChanged();


        }
    }

    // create a passenger object using the fields of a trempit user
    private Passenger createPassengerFromTrempitUser (TrempitUser trempitUser) {
        Passenger passenger = new Passenger();
        passenger.setTrempitUser(trempitUser);
        passenger.setFullName(trempitUser.getFullName());
        passenger.setStartingLocation(trempitUser.getHomeLocation());

        passenger.setId(trempitUser.getId()); //TODO: figure out how to set the id to be unique, or let the backend create the id automatically without problems

        return passenger;
    }

    // adds the passenger to the backend server
    private void insertPassengerToServer(Passenger passenger) {
        new addPassengerEndpointsAsyncTask(this).executeOnExecutor(addPassengerEndpointsAsyncTask.THREAD_POOL_EXECUTOR, passenger);
    }


    class addPassengerEndpointsAsyncTask extends AsyncTask<Passenger, Void, Boolean> {
        private  TrempitApi myApiService = null;
        private Context context;

        addPassengerEndpointsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Passenger... params) {
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
                // add the passenger to the server
                myApiService.addPassengerToEvent(eventId, currentUser.getId(), params[0]).execute();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // update the current trempit user passenger list
                myApiService.addPassengerToTrempitUser(params[0].getId(), currentUser.getId());

            } catch (IOException e) {
                Log.d("Trempit", "IO error");
                return Boolean.FALSE;
            }

            return Boolean.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean param) {
            if (param == Boolean.FALSE) {
                Toast.makeText(context, "failed to create passenger", Toast.LENGTH_LONG).show();
            }

        }


    }

    // create a driver object using the fields of a trempit user
    private Driver createDriverFromTrempitUser (TrempitUser trempitUser) {
        Driver driver = new Driver();
        driver.setTrempitUser(trempitUser);
        driver.setFullName(trempitUser.getFullName());
        driver.setStartingLocation(trempitUser.getHomeLocation());
        driver.setAvailableSeats(TrempitConstants.DEFAULT_AVAILABLE_SEATS);

        driver.setId(trempitUser.getId()); //TODO: figure out how to set the id to be unique, or let the backend create the id automatically without problems

        return driver;
    }

    // adds the passenger to the backend server
    private void insertDriverToServer(Driver driver) {
        new addDriverEndpointsAsyncTask(this).executeOnExecutor(addDriverEndpointsAsyncTask.THREAD_POOL_EXECUTOR, driver);
    }

    class addDriverEndpointsAsyncTask extends AsyncTask<Driver, Void, Boolean> {
        private  TrempitApi myApiService = null;
        private Context context;

        addDriverEndpointsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Boolean doInBackground(Driver... params) {
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
                // add the passenger to the server
                myApiService.addDriverToEvent(eventId, currentUser.getId(), params[0]).execute();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // update the current trempit user passenger list
                myApiService.addDriverToTrempitUser(params[0].getId(), currentUser.getId());

            } catch (IOException e) {
                Log.d("Trempit", "IO error");
                return Boolean.FALSE;
            }

            return Boolean.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean param) {
            if (param == Boolean.FALSE) {
                Toast.makeText(context, "failed to create driver", Toast.LENGTH_LONG).show();
            }

        }


    }

    // removes the passenger from the backend server
    private void removePassengerFromServer(Long passengerId) {
        new removePassengerEndpointsAsyncTask(this).executeOnExecutor(addDriverEndpointsAsyncTask.THREAD_POOL_EXECUTOR, passengerId);
    }

    class removePassengerEndpointsAsyncTask extends AsyncTask<Long, Void, Boolean> {
        private  TrempitApi myApiService = null;
        private Context context;

        removePassengerEndpointsAsyncTask(Context context) {
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
                // add the passenger to the server
                myApiService.removePassenger(params[0]).execute();

            } catch (IOException e) {
                Log.d("Trempit", "IO error");
                return Boolean.FALSE;
            }

            return Boolean.TRUE;
        }

        @Override
        protected void onPostExecute(Boolean param) {
            if (param == Boolean.FALSE) {
                Toast.makeText(context, "failed to remove passenger", Toast.LENGTH_LONG).show();
            }

        }


    }





    //********************* for map activity


    public String[] getDriversName (){
        String[] namesArr = new String[drivers.size()];
        for(int i = 0; i < drivers.size() ; i++) {
            namesArr[i] = drivers.get(i).getFullName();
        }
        return namesArr;
    }

    public Long[] getDriversId (){
        Long[] idArr = new Long[drivers.size()];
        for(int i = 0; i < drivers.size() ; i++) {
            idArr[i] = drivers.get(i).getId();
        }
        return idArr;
    }

    public float[] getDriversLat (){
        float[] latArr = new float[drivers.size()];
        for(int i = 0; i < drivers.size() ; i++) {
            latArr[i] = drivers.get(i).getStartingLocation().getLatitude();
        }
        return latArr;
    }


    public float[] getDriversLng (){
        float[] lngArr = new float[drivers.size()];
        for(int i = 0; i < drivers.size() ; i++) {
            lngArr[i] = drivers.get(i).getStartingLocation().getLongitude();
        }
        return lngArr;
    }







    public void startMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("nameslist", getDriversName());
        intent.putExtra("idList", getDriversId());
        intent.putExtra("latList", getDriversLat());
        intent.putExtra("lngList", getDriversLng());
        intent.putExtra("eventLat", eventLat);
        intent.putExtra("eventLng", eventLng);
        startActivity(intent);
    }

}
