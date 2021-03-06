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
public class AttenderEndpoint {

    private static final Logger logger = Logger.getLogger(AttenderEndpoint.class.getName());

    private static final int DEFAULT_LIST_LIMIT = 20;

    static {
        // Typically you would register this inside an OfyServive wrapper. See: https://code.google.com/p/objectify-appengine/wiki/BestPractices
        ObjectifyService.register(Attender.class);
    }

    /**
     * Returns the {@link Attender} with the corresponding ID.
     *
     * @param id the ID of the entity to be retrieved
     * @return the entity with the corresponding ID
     * @throws NotFoundException if there is no {@code Attender} with the provided ID.
     */
    @ApiMethod(
            name = "getAttender",
            path = "attender/{id}",
            httpMethod = ApiMethod.HttpMethod.GET)
    public Attender get(@Named("id") Long id) throws NotFoundException {
        logger.info("Getting Attender with ID: " + id);
        Attender attender = ofy().load().type(Attender.class).id(id).now();
        if (attender == null) {
            throw new NotFoundException("Could not find Attender with ID: " + id);
        }
        return attender;
    }

    /**
     * Inserts a new {@code Attender}.
     */
    @ApiMethod(
            name = "insertAttender",
            path = "attender",
            httpMethod = ApiMethod.HttpMethod.POST)
    public Attender insert(Attender attender) {
        // Typically in a RESTful API a POST does not have a known ID (assuming the ID is used in the resource path).
        // You should validate that attender.id has not been set. If the ID type is not supported by the
        // Objectify ID generator, e.g. long or String, then you should generate the unique ID yourself prior to saving.
        //
        // If your client provides the ID then you should probably use PUT instead.
        ofy().save().entity(attender).now();
        logger.info("Created Attender with ID: " + attender.getId());

        return ofy().load().entity(attender).now();
    }

    /**
     * Updates an existing {@code Attender}.
     *
     * @param id       the ID of the entity to be updated
     * @param attender the desired state of the entity
     * @return the updated version of the entity
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Attender}
     */
    @ApiMethod(
            name = "updateAttender",
            path = "attender/{id}",
            httpMethod = ApiMethod.HttpMethod.PUT)
    public Attender update(@Named("id") Long id, Attender attender) throws NotFoundException {
        // TODO: You should validate your ID parameter against your resource's ID here.
        EndpointUtils.checkAttenderExists(id);
        ofy().save().entity(attender).now();
        logger.info("Updated Attender: " + attender);
        return ofy().load().entity(attender).now();
    }

    /**
     * Deletes the specified {@code Attender}.
     *
     * @param id the ID of the entity to delete
     * @throws NotFoundException if the {@code id} does not correspond to an existing
     *                           {@code Attender}
     */
    @ApiMethod(
            name = "removeAttender",
            path = "attender/{id}",
            httpMethod = ApiMethod.HttpMethod.DELETE)
    public void remove(@Named("id") Long id) throws NotFoundException {
        EndpointUtils.checkAttenderExists(id);
        ofy().delete().type(Attender.class).id(id).now();
        logger.info("Deleted Attender with ID: " + id);
    }

    /**
     * List all entities.
     *
     * @param cursor used for pagination to determine which page to return
     * @param limit  the maximum number of entries to return
     * @return a response that encapsulates the result list and the next page token/cursor
     */
    @ApiMethod(
            name = "listAttenders",
            path = "attender",
            httpMethod = ApiMethod.HttpMethod.GET)
    public CollectionResponse<Attender> list(@Nullable @Named("cursor") String cursor, @Nullable @Named("limit") Integer limit) {
        limit = limit == null ? DEFAULT_LIST_LIMIT : limit;
        Query<Attender> query = ofy().load().type(Attender.class).limit(limit);
        if (cursor != null) {
            query = query.startAt(Cursor.fromWebSafeString(cursor));
        }
        QueryResultIterator<Attender> queryIterator = query.iterator();
        List<Attender> attenderList = new ArrayList<>(limit);
        while (queryIterator.hasNext()) {
            attenderList.add(queryIterator.next());
        }
        return CollectionResponse.<Attender>builder().setItems(attenderList).setNextPageToken(queryIterator.getCursor().toWebSafeString()).build();
    }


}