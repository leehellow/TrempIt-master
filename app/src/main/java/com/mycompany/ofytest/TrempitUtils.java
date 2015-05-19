package com.mycompany.ofytest;

import com.example.ilay.myapplication.backend.trempitApi.model.Driver;
import com.example.ilay.myapplication.backend.trempitApi.model.Event;
import com.example.ilay.myapplication.backend.trempitApi.model.Location;
import com.google.api.client.util.DateTime;

/**
 * Created by Ilay on 19/5/2015.
 */
public class TrempitUtils {

    public static String parseLocation(Location location) {
        String locationString = "";

        if (location == null) {
            return ""; // if the object doesn't have a defined location, don't display anything about it
        }

        String street = location.getStreet();
        String city = location.getCity();

        if (street != null) {
            locationString = locationString  + street;
        }
        if (city != null) {
            locationString = locationString + ", " + city;
        }

        return locationString;
    }

    public static String parseDateTime(DateTime dateTime) {
        if (dateTime == null){
            return "";
        }

        return dateTime.toStringRfc3339(); //TODO: parse DateTime
    }

    public static String parseEvent(Event event) {
        String eventString = "";
        String title = event.getTitle();
        String locationString = parseLocation(event.getLocation());
        String startTimeString = parseDateTime(event.getStartTime());

        if (title != null) {
            eventString = eventString + title + "\n";
        }

        eventString = eventString + startTimeString + "\n";
        eventString = eventString + locationString;

        return eventString;
    }

    // parsing for the driver list in driver activity
    public static String parseDriver(Driver driver) {
        String driverString = "";
        String locationString = parseLocation(driver.getStartingLocation());

        driverString += driver.getFullName() + "\n";
        driverString += locationString;

        return driverString;
    }
}
