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
                return event1 == null;
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

    public static void checkTrempitUserExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(TrempitUser.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find TrempitUser with ID: " + id);
        }

    }

    public static void checkLocationExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Location.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find TrempitUser with ID: " + id);
        }

    }

    public static void checkAttenderExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Attender.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find TrempitUser with ID: " + id);
        }

    }
}
