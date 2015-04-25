package com.mycompany.ofytest;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ilay.myapplication.backend.trempitApi.TrempitApi;
import com.example.ilay.myapplication.backend.trempitApi.model.Event;
import com.example.ilay.myapplication.backend.trempitApi.model.Passenger;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.Collections;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    ListView listView;
    ArrayAdapter<Event> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
    }

    public void getQuotes(View v) {
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
                        .setRootUrl("https://trans-setup-92615.appspot.com/_ah/api/").setApplicationName("Trempit");
                // end options for devappserver

                myApiService = builder.build();
            }

            try {
                Event event = new Event();
                event.setId((long) 100);
                event.setTitle("Birthday2");
                myApiService.insertEvent(event).execute();
                Passenger passenger = new Passenger();
                passenger.setId((long) 10);
                passenger.setFullName("Eran Katz");
                myApiService.insertPassenger(passenger).execute();
                com.example.ilay.myapplication.backend.trempitApi.model.Driver driver = new com.example.ilay.myapplication.backend.trempitApi.model.Driver();
                driver.setId((long) 20);
                driver.setFullName("Eran Nahag");
                myApiService.insertDriver(driver).execute();
                myApiService.addPassengerToEvent(event.getId(),passenger.getId()).execute();

                myApiService.addDriverToEvent(driver.getId(), event.getId()).execute();

                return myApiService.listEvents().execute().getItems();
            } catch (IOException e) {
                return Collections.EMPTY_LIST;
            }
        }

        @Override
        protected void onPostExecute(List<Event> result) {
            for (Event q : result) {
                List<Passenger> passlist= q.getPassengerList();
                Toast.makeText(context, passlist.get(0).getFullName() + " : " + q.getTitle(), Toast.LENGTH_LONG).show();

            }

//            arrayAdapter = new ArrayAdapter<Event>(context, R.layout.item_event, result);
//            listView.setAdapter(arrayAdapter);





        }
    }
}
