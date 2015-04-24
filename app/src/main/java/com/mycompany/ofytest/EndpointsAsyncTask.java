package com.mycompany.ofytest;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.ilay.myapplication.backend.trempitApi.model.Passenger;
import com.example.ilay.myapplication.backend.trempitApi.model.Event;
import com.example.ilay.myapplication.backend.trempitApi.model.Driver;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.mindstorm.api.quoteEndpoint.QuoteEndpoint;
import com.mindstorm.api.quoteEndpoint.model.Quote;
import com.example.ilay.myapplication.backend.trempitApi.TrempitApi;

import java.io.IOException;
//import java.sql.Driver;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ilay on 15/4/2015.
 */
class EndpointsAsyncTask extends AsyncTask<Void, Void, List<Event>> {
    private static TrempitApi myApiService = null;
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
                    .setRootUrl("http://192.168.43.113:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
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

    }
}