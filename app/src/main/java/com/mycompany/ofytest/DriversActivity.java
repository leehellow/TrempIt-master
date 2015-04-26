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

import com.example.ilay.myapplication.backend.trempitApi.TrempitApi;
import com.example.ilay.myapplication.backend.trempitApi.model.Driver;
import com.example.ilay.myapplication.backend.trempitApi.model.Event;
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
    Long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers);

        Intent intent = getIntent();
        long currUserId = (long) intent.getSerializableExtra("user");
        eventId = (long) intent.getSerializableExtra("event");
        Log.d("TrempIt", String.valueOf(currUserId));

        driverAdapter  = new DriverAdapter(this, drivers);
        //driverAdapter.add(new Driver());
        new EndpointsAsyncTask(this).executeOnExecutor(EndpointsAsyncTask.THREAD_POOL_EXECUTOR);

        final ListView listView = (ListView) findViewById(R.id.driverlistview);
        Log.d("TrempIt", listView.toString());
        listView.setAdapter(driverAdapter);

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


    public void startEventActivity(View view) {
        Driver driver = (Driver) view.getTag();
        Log.d("TrempIt", "Driver name: " + driver.getFullName());
//        Intent intent = new Intent(this, DriversActivity.class);
//        intent.putExtra("user", currentUser.getId());//TODO: what is the first parameter for?
//        intent.putExtra("event", event.getId());
//        startActivity(intent);
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
                        .setRootUrl("http://192.168.43.113:8080/_ah/api/").setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
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
                myApiService.insertDriver(driver);
                myApiService.addDriverToEvent(driver.getId(), eventId);
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

            driverAdapter.addAll(result);
            Log.d("TrempIt", "after post");
            driverAdapter.notifyDataSetChanged();





        }
    }

}
