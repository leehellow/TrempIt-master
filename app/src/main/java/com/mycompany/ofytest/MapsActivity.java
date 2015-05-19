package com.mycompany.ofytest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    String[] namesList;
    long[] idList;
    float[] latList;
    float[] lngList;
    float eventLat;
    float eventLng;

   /* String name;
    long id;
    float lat;
    float lng; */

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        Intent intent = getIntent();
        namesList  = intent.getStringArrayExtra("nameslist");
        idList = intent.getLongArrayExtra("idList");
        latList = intent.getFloatArrayExtra("latList");
        lngList = intent.getFloatArrayExtra("lngList");
        eventLat = intent.getFloatExtra("eventLat", (float) -1.0);
        eventLng = intent.getFloatExtra("eventLng", (float) -1.0);

      /* name = intent.getStringExtra("name");
       id = intent.getLongExtra("id",-1);
       lat = intent.getFloatExtra("lat", (float) -1.0);
        lng = intent.getFloatExtra("lng", (float) -1.0);*/

        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        for (int i = 0; i < namesList.length ; i++) {
            mMap.addMarker(new MarkerOptions().position(new LatLng(latList[i], lngList[i])).title(namesList[i]));
        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(eventLat, eventLng)).title("Event Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

    }


}

