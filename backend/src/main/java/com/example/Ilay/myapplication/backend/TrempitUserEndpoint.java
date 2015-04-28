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
public class TrempitUserEndpoint {

    private static final Logger logger = Logger.getLogger(TrempitUserEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(TrempitUser.class);
    }

    /**
     * Returns the {@link TrempitUser} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code TrempitUser} with the provided ID.
     */
    @ApiMethod(
            name = "getTrempitUser",
            path = "trempitUser/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public TrempitUser get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting TrempitUser with ID: " + id);
        TrempitUser trempitUser = ofy().load().type(TrempitUser.class).id(id).now();
        if (trempitUser == null) {
            throw new NotFoundException("Could not find TrempitUser with ID: " + id);
        }
        return trempitUser;
    }

    /**
     * Inserts a new {@code TrempitUser}.
     */
    @ApiMethod(
            name = "insertTrempitUser",
            path = "trempitUser",
            httpMethod = ApiMethod.HttpMethod.POST)
    public TrempitUser insert(TrempitUser trempitUser) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that trempitUser.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(trempitUser).now();
        logger.info("Created TrempitUser with ID: " + trempitUser.getId());

        return ofy().load().entity(trempitUser).now();
    }

    /**
     * Updates an existing {@code TrempitUser}.
     *
     * @param id          the ID of the entity to be updated
     * @param trempitUser the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code TrempitUser}
     */
    @ApiMethod(
            name = "updateTrempitUser",
            path = "trempitUser/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public TrempitUser update(@Named("id") Long id, TrempitUser trempitUser) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(trempitUser).now();
        logger.info("Updated TrempitUser: " + trempitUser);
        return ofy().load().entity(trempitUser).now();
    }

    /**
     * Deletes the specified {@code TrempitUser}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code TrempitUser}
     */
    @ApiMethod(
            name = "removeTrempitUser",
            path = "trempitUser/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(TrempitUser.class).id(id).now();
        logger.info("Deleted TrempitUser with ID: " + id);
    }


    /**
     * Updates an existing {@code TrempitUser}.
     *
     * @param id          the ID of the entity to be updated
     * @param trempitUser the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code TrempitUser}
     */
    @ApiMethod(
            name = "addPassengerToTrempitUser",
            path = "trempitUser/addPassengerToUser",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public void addPassengerToUser(@Named("TrempitUserId") Long TrempitUserId, @Named("PassengerId") Long PassengerId) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(TrempitUserId);
        TrempitUser trempitUser = ofy().load().type(TrempitUser.class).id(TrempitUserId).now();
        Passenger passenger = ofy().load().type(Passenger.class).id(PassengerId).now();
        trempitUser.addPassengerToUser(passenger);
        ofy().save().entity(trempitUser).now();

    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "listTrempitUsers",
            path = "trempitUser",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<TrempitUser> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<TrempitUser> query = ofy().load().type(TrempitUser.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<TrempitUser> queryIterator = query.iterator();
        List<TrempitUser> trempitUserList = new ArrayList<TrempitUser>(limit);
        while (queryIterator.hasNext()) {
            trempitUserList.add(queryIterator.next());
        }
        return CollectionResponse.<TrempitUser>builder().setItems(trempitUserList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    /**
     * List all entities.
     *
     * @param trempitUserId used for pagination to determine which page to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "listPendingPassengers",
            path = "pendingPassengers",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Passenger> listPendingPassengers(@Named("TrempitUserId") Long trempitUserId) throws NotFoundException {
        EndpointUtils.checkTrempitUserExists(trempitUserId);
        TrempitUser trempitUser = ofy().load().type(TrempitUser.class).id(trempitUserId).now();
        List<Passenger> passengerList = trempitUser.getPassengerList();
        return CollectionResponse.<Passenger>builder().setItems(passengerList).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(TrempitUser.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find TrempitUser with ID: " + id);
        }
    }
}