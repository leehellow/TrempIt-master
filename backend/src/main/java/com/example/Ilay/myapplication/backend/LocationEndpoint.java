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
public class LocationEndpoint {

    private static final Logger logger = Logger.getLogger(LocationEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Location.class);
    }

    /**
     * Returns the {@link Location} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Location} with the provided ID.
     */
    @ApiMethod(
            name = "getLocation",
            path = "location/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Location get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Location with ID: " + id);
        Location location = ofy().load().type(Location.class).id(id).now();
        if (location == null) {
            throw new NotFoundException("Could not find Location with ID: " + id);
        }
        return location;
    }

    /**
     * Inserts a new {@code Location}.
     */
    @ApiMethod(
            name = "insertLocation",
            path = "location",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Location insert(Location location) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that location.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(location).now();
        logger.info("Created Location with ID: " + location.getId());

        return ofy().load().entity(location).now();
    }

    /**
     * Updates an existing {@code Location}.
     *
     * @param id       the ID of the entity to be updated
     * @param location the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Location}
     */
    @ApiMethod(
            name = "updateLocation",
            path = "location/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Location update(@Named("id") Long id, Location location) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        checkExists(id);
        ofy().save().entity(location).now();
        logger.info("Updated Location: " + location);
        return ofy().load().entity(location).now();
    }

    /**
     * Deletes the specified {@code Location}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Location}
     */
    @ApiMethod(
            name = "removeLocation",
            path = "location/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        checkExists(id);
        ofy().delete().type(Location.class).id(id).now();
        logger.info("Deleted Location with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "listLocations",
            path = "location",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Location> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Location> query = ofy().load().type(Location.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Location> queryIterator = query.iterator();
        List<Location> locationList = new ArrayList<Location>(limit);
        while (queryIterator.hasNext()) {
            locationList.add(queryIterator.next());
        }
        return CollectionResponse.<Location>builder().setItems(locationList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }

    private void checkExists(Long id) throws NotFoundException {
        try {
            ofy().load().type(Location.class).id(id).safe();
        } catch (com.googlecode.objectify.NotFoundException e) {
            throw new NotFoundException("Could not find Location with ID: " + id);
        }
    }
}