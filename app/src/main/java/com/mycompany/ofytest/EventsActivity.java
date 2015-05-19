package com.mycompany.ofytest;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.ilay.myapplication.backend.trempitApi.model.Driver;
import com.example.ilay.myapplication.backend.trempitApi.model.Passenger;
import com.example.ilay.myapplication.backend.trempitApi.TrempitApi;
import com.example.ilay.myapplication.backend.trempitApi.model.Event;
import com.example.ilay.myapplication.backend.trempitApi.model.TrempitUser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class EventsActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    ArrayList<Event> events = new ArrayList<>();
    EventAdapter eventAdapter;
    TrempitUser currentUser;
    GlobalState globalState;

    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient googleApiClient;

    /**
     * Represents a geographical location.
     */
    protected Location lastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        buildGoogleApiClient();

//        currentUser.setFullName("Eran Katz");
//        currentUser.setId((long) 1000);

        eventAdapter = new EventAdapter(this, events);

        globalState = (GlobalState) getApplicationContext();


        final ListView listView = (ListView) findViewById(R.id.eventlistview);
        listView.setAdapter(eventAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    public void startEventActivity(View view) {
        Event event = (Event) view.getTag();
        Log.d("TrempIt", "Event title: " + event.getTitle());
        Intent intent = new Intent(this, DriversActivity.class);

        intent.putExtra("event", event.getId());
        Log.d("TrempIt", "EventsActivity " + String.valueOf(event.getId()));
        startActivity(intent);
    }

    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        // Provides a simple way of getting a device's location and is well suited for
        // applications that do not require a fine-grained location and that do not need location
        // updates. Gets the best and most recent location currently available, which may be null
        // in rare cases when a location is not available.
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (lastLocation != null) {
            com.example.ilay.myapplication.backend.trempitApi.model.Location location = new com.example.ilay.myapplication.backend.trempitApi.model.Location();
            location.setLatitude((float) lastLocation.getLatitude());
            location.setLongitude((float) lastLocation.getLongitude());
            location.setId((long) 1000);
            //currentUser.setHomeLocation(location);
        } else {
            Log.d("TrempIt", "could not find location");
        }

        // added here so the currentUser location will be loaded from GPS before storing in datastore
        //TODO: handle activity recreation (e.g. rotation of screen) - don't create new objects

        refreshActivity((View) findViewById(R.id.action_settings));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (globalState.getCurrentUser() == null) {
            //buildTestData((View) findViewById(R.id.eventlistview));
        }

    }

    public void refreshActivity(View view) {
        new EndpointsAsyncTask(this).executeOnExecutor(EndpointsAsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.d("TrempIt", "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.d("TrempIt", "Connection suspended");
        googleApiClient.connect();
    }

    public void buildTestData(View view) {
        new BuildTestEndpointsAsyncTask(this).executeOnExecutor(EndpointsAsyncTask.THREAD_POOL_EXECUTOR);
    }

     class EndpointsAsyncTask extends AsyncTask<Void, Void, List<Event>> {
        private  TrempitApi myApiService = null;
        private Context context;

        EndpointsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<Event> doInBackground(Void... params) {
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
                //myApiService.insertLocation(currentUser.getHomeLocation()).execute();
                //myApiService.insertTrempitUser(currentUser).execute();
                return myApiService.listEvents().execute().getItems();
            } catch (IOException e) {
                Log.d("Trempit", "IO error");
                return Collections.EMPTY_LIST;
            }
        }

        @Override
        protected void onPostExecute(List<Event> result) {
//            for (Event q : result) {
//                List<Passenger> passlist= q.getPassengerList();
//                Toast.makeText(context, passlist.get(0).getFullName() + " : " + q.getTitle(), Toast.LENGTH_LONG).show();
//
//            }
            if (result == null || result.isEmpty()) {
                Log.d("TrempIt", "No events retrieved");
                return;
            }

            eventAdapter.clear();
            eventAdapter.addAll(result);
            Log.d("TrempIt", "after post");
            eventAdapter.notifyDataSetChanged();
        }
    }


    class BuildTestEndpointsAsyncTask extends AsyncTask<Void, Void, Void> {
        private  TrempitApi myApiService = null;
        private Context context;

        BuildTestEndpointsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... params) {
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
                com.example.ilay.myapplication.backend.trempitApi.model.Location location1 = new com.example.ilay.myapplication.backend.trempitApi.model.Location();
                location1.setId((long) 1);
                location1.setCity("Tel Aviv");
                location1.setStreet("Ben Yehuda");
                com.example.ilay.myapplication.backend.trempitApi.model.Location location2 = new com.example.ilay.myapplication.backend.trempitApi.model.Location();
                location2.setId((long) 2);
                location2.setCity("Tel Aviv");
                location2.setStreet("Dizengoff");
                com.example.ilay.myapplication.backend.trempitApi.model.Location location3 = new com.example.ilay.myapplication.backend.trempitApi.model.Location();
                location3.setId((long) 3);
                location3.setCity("Petah Tikva");
                location3.setStreet("Gordon");

                Event event1 = new Event();
                event1.setId((long) 1);
                event1.setTitle("Birthday");
                event1.setLocation(location1);
                Date date1 = new Date(115, 3, 29, 19, 0);
                DateTime dateTime1 = new DateTime(date1);
                event1.setStartTime(dateTime1);

                Event event2 = new Event();
                event2.setId((long) 2);
                event2.setTitle("Wedding");
                event2.setLocation(location2);
                Date date2 = new Date(115, 4, 3, 20, 30);
                DateTime dateTime2 = new DateTime(date2);
                event2.setStartTime(dateTime2);

                TrempitUser trempitUser1 = new TrempitUser();
                trempitUser1.setId((long) 1);
                trempitUser1.setFullName("Eran Katz");
                trempitUser1.setHomeLocation(location3);
                currentUser = trempitUser1;
                globalState.setCurrentUser(currentUser);

                TrempitUser trempitUser2 = new TrempitUser();
                trempitUser2.setId((long) 2);
                trempitUser2.setFullName("Mahatma Gandhi");
                trempitUser2.setHomeLocation(location1);

                Passenger passenger1 = new Passenger();
                passenger1.setFullName(trempitUser1.getFullName());
                passenger1.setId((long) 10);
                passenger1.setStartingLocation(trempitUser1.getHomeLocation());
                //passenger1.setEvent(event1);

                Driver driver1 = new Driver();
                driver1.setId((long) 20);
                driver1.setFullName(trempitUser2.getFullName());
                driver1.setStartingLocation(trempitUser2.getHomeLocation());
                //driver1.setEvent(event1);
                Date date3 = new Date(115, 3, 29, 19, 30); //fashionably late
                DateTime dateTime3 = new DateTime(date3);
                driver1.setArrivalTime(dateTime3);
                driver1.setAvailableSeats(4);

                myApiService.insertLocation(location1).execute();
                myApiService.insertLocation(location2).execute();
                myApiService.insertLocation(location3).execute();
                myApiService.insertEvent(event1).execute();
                myApiService.insertEvent(event2).execute();
                myApiService.insertTrempitUser(trempitUser1).execute();
                myApiService.insertTrempitUser(trempitUser2).execute();
                myApiService.addPassengerToEvent(event1.getId(), trempitUser1.getId(), passenger1).execute();
                myApiService.addDriverToEvent(event1.getId(), trempitUser2.getId(), driver1).execute();



            } catch (IOException e) {
                Log.d("Trempit", "IO error");
            }

            return null;
        }


    }
}
