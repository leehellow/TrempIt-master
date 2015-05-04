package com.example.Ilay.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/**
 * WARNING: This generated code is intended as a sample or starting point for using a
 * Google Cloud Endpoints RESTful API with an Objectify entity. It provides no data access
 * restrictions and no data validation.
 * <p/>
 * DO NOT deploy this code unchanged as part of a real application to real users.
 */
@Api(
        name = "trempitApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.Ilay.example.com",
                ownerName = "backend.myapplication.Ilay.example.com",
                packagePath = ""
        )
)
public class DriverEndpoint {

    private static final Logger logger = Logger.getLogger(DriverEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Driver.class);
    }

    /**
     * Returns the {@link Driver} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Driver} with the provided ID.
     */
    @ApiMethod(
            name = "getDriver",
            path = "driver/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Driver get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Driver with ID: " + id);
        Driver driver = ofy().load().type(Driver.class).id(id).now();
        if (driver == null) {
            throw new NotFoundException("Could not find Driver with ID: " + id);
        }
        return driver;
    }


    private Driver insert(Driver driver, @Named("trempituserId") Long trempituserId) throws NotFoundException {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that driver.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        EndpointUtils.checkTrempitUserExists(trempituserId);
        TrempitUser trempitUser = ofy().load().type(TrempitUser.class).id(trempituserId).now();
        trempitUser.addDriverToUser(driver);
        ofy().save().entity(driver).now();
        ofy().save().entity(trempitUser).now();
        logger.info("Created Driver with ID: " + driver.getId());

        return ofy().load().entity(driver).now();
    }

    /**
     * Updates an existing {@code Driver}.
     *
     * @param id     the ID of the entity to be updated
     * @param driver the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Driver}
     */
    @ApiMethod(
            name = "updateDriver",
            path = "driver/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Driver update(@Named("id") Long id, Driver driver) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        EndpointUtils.checkDriverExists(id);
        ofy().save().entity(driver).now();
        logger.info("Updated Driver: " + driver);
        return ofy().load().entity(driver).now();
    }

    /**
     * Deletes the specified {@code Driver}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Driver}
     */
    @ApiMethod(
            name = "removeDriver",
            path = "driver/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        EndpointUtils.checkDriverExists(id);
        Driver driver = ofy().load().type(Driver.class).id(id).now();
        TrempitUser trempitUser = driver.getTrempitUser();
        Event event = driver.getEvent();

        trempitUser.removeDriverFromUser(driver);
        event.removeDriver(driver);

        ofy().save().entity(driver.getTrempitUser()).now();
        ofy().save().entity(driver.getEvent()).now();

        ofy().delete().type(Driver.class).id(id).now();
        logger.info("Deleted Driver with ID: " + id);
    }


    private CollectionResponse<Driver> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Driver> query = ofy().load().type(Driver.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Driver> queryIterator = query.iterator();
        List<Driver> driverList = new ArrayList<>(limit);
        while (queryIterator.hasNext()) {
            driverList.add(queryIterator.next());
        }
        return CollectionResponse.<Driver>builder().setItems(driverList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    /**
     * Updates an existing {@code Driver}.
     *
     * @param passengerid     the ID of the entity to be updated
     * @param driverid the desired state of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Driver}
     */
    @ApiMethod(
            name = "approvePassengerRequest",
            path = "driver/approveRequest",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public void approveRequest(@Named("driverid") Long driverid, @Named("passengerid") Long passengerid) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        EndpointUtils.checkDriverExists(driverid);
        EndpointUtils.checkPassengerExists(passengerid);
        Driver driver = ofy().load().type(Driver.class).id(driverid).now();
        Passenger passenger = ofy().load().type(Passenger.class).id(passengerid).now();
        driver.addPassengerToPassengerList(passenger);
        driver.removePassengerFromPendingPassengerList(passenger);
        passenger.setDriver(driver);

        ofy().save().entity(driver).now();
        ofy().save().entity(passenger).now();
        logger.info("Added passenger: " + passengerid + " to driver: " + driverid);
    }

    /**
     * Updates an existing {@code Driver}.
     *
     * @param trempituserid     the ID of the entity to be updated
     * @param driverid the desired state of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Driver}
     */
    @ApiMethod(
            name = "addPassengerRequest",
            path = "driver/addRequest",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public void addRequest(@Named("driverid") Long driverid, @Named("trempituserid") Long trempituserid, @Named("passengerid") Long passengerid) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        EndpointUtils.checkDriverExists(driverid);
        EndpointUtils.checkTrempitUserExists(trempituserid);
        EndpointUtils.checkPassengerExists(passengerid);
        Driver driver = ofy().load().type(Driver.class).id(driverid).now();
        TrempitUser trempitUser = ofy().load().type(TrempitUser.class).id(trempituserid).now();
        Passenger passenger = ofy().load().type(Passenger.class).id(passengerid).now();
        List<Passenger> passengerList = trempitUser.getPassengerList();

        Passenger eventPassenger = null; // the passenger object for the event

        for (Passenger passenger: passengerList) {
            if (passenger.getEvent().getId().equals(eventid)) {
                eventPassenger = passenger;
            }
        }

        if (eventPassenger == null) {
            eventPassenger = new Passenger();
            eventPassenger.setId(trempitUser.getId());
            eventPassenger.setFullName(trempitUser.getFullName());
            eventPassenger.setEvent(event);
            eventPassenger.setStartingLocation(trempitUser.getHomeLocation());
            eventPassenger.setTrempitUser(trempitUser);

            ofy().save().entity(eventPassenger).now();
            event.addPassenger(eventPassenger);
            ofy().save().entity(event).now();
        }




        driver.addPassengerToPendingPassengerList(eventPassenger);
        ofy().save().entity(driver).now();
        logger.info("Added passenger : " + eventPassenger.getId() + " to driver: " + driverid + "pending list");
    }



    /**
     * List all entities.
     *
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "listEventDrivers",
            path = "event/listDrivers",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Driver> listEventDrivers(@Named("id") Long id) throws NotFoundException {
        EndpointUtils.checkEventExists(id);
        Event event = ofy().load().type(Event.class).id(id).now();
        List<Driver> eventDriverList = event.getDriverList();
        return CollectionResponse.<Driver>builder().setItems(eventDriverList).build();
    }


    /**
     * Deletes the specified {@code Event}.
     *
     * @param eventid the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Event}
     */
    @ApiMethod(
            name = "addDriverToEvent",
            path = "event/addDriver",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public void addDriver(@Named("eventid") Long eventid, @Named("trempituserid") Long trempitUserId, Driver driver) throws NotFoundException {
        EndpointUtils.checkEventExists(eventid);
        EndpointUtils.checkTrempitUserExists(trempitUserId);
        Event event = ofy().load().type(Event.class).id(eventid).now();
        TrempitUser trempitUser = ofy().load().type(TrempitUser.class).id(trempitUserId).now();
        event.addDriver(driver);
        driver.setEvent(event);
        driver.setTrempitUser(trempitUser);
        trempitUser.addDriverToUser(driver);
        ofy().save().entity(event).now();
        ofy().save().entity(driver).now();
        ofy().save().entity(trempitUser).now();
        logger.info("Added driver: " + driver.getId() + "to event: " + eventid);
    }



}