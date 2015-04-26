package com.example.Ilay.myapplication.backend;

import com.google.api.server.spi.response.NotFoundException;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * Created by Lee on 4/24/2015.
 */
public final class EndpointUtils {

    private EndpointUtils() {

    }

    public static void checkEventExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Event.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Event with ID: " + id);
        }
    }

    public static boolean checkEventNotExists(Event event) throws NotFoundException {
            Event event1;
            try {
                event1 = ofy().load().entity(event).now();
                if (event1 == null) {
                    return true;
                } else {
                    return false;
                }
            }
            catch (Exception e) {
                return true;
            }


    }

    public static void checkPassengerExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Passenger.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Passenger with ID: " + id);
        }

    }

    public static void checkDriverExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Driver.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Driver with ID: " + id);
        }

    }
}
