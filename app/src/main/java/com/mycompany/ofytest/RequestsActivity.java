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
import android.widget.TextView;

import com.example.ilay.myapplication.backend.trempitApi.TrempitApi;
import com.example.ilay.myapplication.backend.trempitApi.model.Event;
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

public class RequestsActivity extends ActionBarActivity {

    ArrayList<Passenger> passengers = new ArrayList<>();
    RequestAdapter requestAdapter;
    TrempitUser currentUser;
    GlobalState globalState;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);
        requestAdapter = new RequestAdapter(this, passengers);
        globalState = (GlobalState) getApplicationContext();
        currentUser = globalState.getCurrentUser();
        final ListView listView = (ListView) findViewById(R.id.requestlistview);
        listView.setAdapter(requestAdapter);

        refreshActivity(findViewById(R.id.button));
    }

    public void approveRequest(View view){


    }

    public void declineRequest(View view){
        Passenger passenger  = (Passenger) view.getTag();


    }

    public void refreshActivity(View view) {
        new GetRequestsEndpointsAsyncTask(this).executeOnExecutor(GetRequestsEndpointsAsyncTask.THREAD_POOL_EXECUTOR);
    }

    class GetRequestsEndpointsAsyncTask extends AsyncTask<Void, Void, List<Passenger>> {

        private TrempitApi myApiService = null;
        private Context context;

        GetRequestsEndpointsAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected List<Passenger> doInBackground(Void... params) {
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
                return myApiService.listPendingPassengers(currentUser.getId()).execute().getItems();
            } catch (IOException e) {
                Log.d("Trempit", "IO error");
                return Collections.EMPTY_LIST;
            }
        }

        @Override
        protected void onPostExecute(List<Passenger> result) {

            if (result == null || result.isEmpty()) {
                Log.d("TrempIt", "No passengers retrieved");
                return;
            }

            requestAdapter.clear();
            requestAdapter.addAll(result);
            Log.d("TrempIt", "after post");
            requestAdapter.notifyDataSetChanged();
        }
    }

//    class ApproveEndpointsAsyncTask extends AsyncTask<Void, Void, Void> {
//
//        private TrempitApi myApiService = null;
//        private Context context;
//
//        ApproveEndpointsAsyncTask(Context context) {
//            this.context = context;
//        }
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            if(myApiService == null) {  // Only do this once
//                TrempitApi.Builder builder = new TrempitApi.Builder(AndroidHttp.newCompatibleTransport(),
//                        new AndroidJsonFactory(), null)
//                        // options for running against local devappserver
//                        // - 10.0.2.2 is localhost's IP address in Android emulator
//                        // - turn off compression when running against local devappserver
//                        .setRootUrl(TrempitConstants.SERVERPATH).setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                            @Override
//                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                                abstractGoogleClientRequest.setDisableGZipContent(true);
//                            }
//                        }).setApplicationName("Trempit");
//                // end options for devappserver
//
//                myApiService = builder.build();
//            }
//
//            try {
//                Passenger passenger = (Passenger) view.getTag();
//                return myApiService.approvePassengerRequest(passenger.getid(), currentUser.getId()).execute().getItems();
//            }
//            } catch (IOException e) {
//                Log.d("Trempit", "IO error");
//                return Collections.EMPTY_LIST;
//            }
//        }
//
//        class DeclineEndpointsAsyncTask extends AsyncTask<Void, Void, Void> {
//
//            private TrempitApi myApiService = null;
//            private Context context;
//
//            ApproveEndpointsAsyncTask(Context context) {
//                this.context = context;
//            }
//
//            @Override
//            protected Void doInBackground(Void... params) {
//                if(myApiService == null) {  // Only do this once
//                    TrempitApi.Builder builder = new TrempitApi.Builder(AndroidHttp.newCompatibleTransport(),
//                            new AndroidJsonFactory(), null)
//                            // options for running against local devappserver
//                            // - 10.0.2.2 is localhost's IP address in Android emulator
//                            // - turn off compression when running against local devappserver
//                            .setRootUrl(TrempitConstants.SERVERPATH).setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                                @Override
//                                public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                                    abstractGoogleClientRequest.setDisableGZipContent(true);
//                                }
//                            }).setApplicationName("Trempit");
//                    // end options for devappserver
//
//                    myApiService = builder.build();
//                }
//
//                try {
//
//                } catch (IOException e) {
//                    Log.d("Trempit", "IO error");
//                    return Collections.EMPTY_LIST;
//                }
//            }
//
//
//            @Override
//        protected void onPostExecute(List<Passenger> result) {
//
//            if (result == null || result.isEmpty()) {
//                Log.d("TrempIt", "No passengers retrieved");
//                return;
//            }
//
//            requestAdapter.clear();
//            requestAdapter.addAll(result);
//            Log.d("TrempIt", "after post");
//            requestAdapter.notifyDataSetChanged();
//        }
//    }

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


}
