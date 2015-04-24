//package com.mycompany.ofytest;
//
//import android.content.Context;
//import android.os.AsyncTask;
//import android.widget.Toast;
//
//import com.example.ilay.myapplication.backend.eventApi.model.Event;
//import com.example.ilay.myapplication.backend.passengerApi.PassengerApi;
//import com.google.api.client.extensions.android.http.AndroidHttp;
//import com.google.api.client.extensions.android.json.AndroidJsonFactory;
//import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
//import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
//import com.mindstorm.api.quoteEndpoint.QuoteEndpoint;
//import com.mindstorm.api.quoteEndpoint.model.Quote;
//import com.example.ilay.myapplication.backend.eventApi.EventApi;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//
///**
// * Created by Ilay on 15/4/2015.
// */
//class EndpointsPassengerAsyncTask extends AsyncTask<Void, Void, List<Event>> {
//    private static PassengerApi myApiService = null;
//    private Context context;
//
//    EndpointsPassengerAsyncTask(Context context) {
//        this.context = context;
//    }
//
//    @Override
//    protected List<Event> doInBackground(Void... params) {
//        if(myApiService == null) {  // Only do this once
//            PassengerApi.Builder builder = new PassengerApi.Builder(AndroidHttp.newCompatibleTransport(),
//                    new AndroidJsonFactory(), null)
//                    // options for running against local devappserver
//                    // - 10.0.2.2 is localhost's IP address in Android emulator
//                    // - turn off compression when running against local devappserver
//                    .setRootUrl("http://192.168.43.113:8080/_ah/api/")
//                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
//                        @Override
//                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
//                            abstractGoogleClientRequest.setDisableGZipContent(true);
//                        }
//                    });
//            // end options for devappserver
//
//            myApiService = builder.build();
//        }
//
//        try {
//            Event event = new Event();
//            event.setId((long) 100);
//            event.setTitle("Birthday2");
//            myApiService.insert(event).execute();
//
//            return myApiService.list().execute().getItems();
//        } catch (IOException e) {
//            return Collections.EMPTY_LIST;
//        }
//    }
//
//    @Override
//    protected void onPostExecute(List<Event> result) {
//        for (Event q : result) {
//            Toast.makeText(context, q.getId() + " : " + q.getTitle(), Toast.LENGTH_LONG).show();
//        }
//    }
//}