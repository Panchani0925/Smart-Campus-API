
package com.mycompany.smartcampusapi1.resource;

import com.mycompany.smartcampusapi1.exception.RoomNotEmptyException;
import com.mycompany.smartcampusapi1.model.Room;
import com.mycompany.smartcampusapi1.store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoomResource {

    private final DataStore store = DataStore.getInstance();

    // GET /api/v1/rooms - List all rooms
    @GET
    public Response getAllRooms() {
        List<Room> roomList = new ArrayList<>(store.getRooms().values());
        return Response.ok(roomList).build();
    }

    // POST /api/v1/rooms - Create a new room
    @POST
    public Response createRoom(Room room) {
        if (room.getId() == null || room.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Room ID is required")).build();
        }
        if (store.getRooms().containsKey(room.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Room with this ID already exists")).build();
        }
        store.getRooms().put(room.getId(), room);
        return Response.status(Response.Status.CREATED).entity(room).build();
    }

    // GET /api/v1/rooms/{roomId} - Get a specific room
    @GET
    @Path("/{roomId}")
    public Response getRoom(@PathParam("roomId") String roomId) {
        Room room = store.getRooms().get(roomId);
        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Room not found: " + roomId)).build();
        }
        return Response.ok(room).build();
    }

    // DELETE /api/v1/rooms/{roomId} - Delete a room (only if no sensors)
    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = store.getRooms().get(roomId);

        if (room == null) {
            // Idempotent: already gone, return 204
            return Response.noContent().build();
        }

        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                "Room '" + roomId + "' cannot be deleted. It has " +
                room.getSensorIds().size() + " sensor(s) still assigned."
            );
        }

        store.getRooms().remove(roomId);
        return Response.noContent().build();
    }
}