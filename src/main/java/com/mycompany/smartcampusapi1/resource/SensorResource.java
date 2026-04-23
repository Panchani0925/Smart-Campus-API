
package com.mycompany.smartcampusapi1.resource;


import com.mycompany.smartcampusapi1.exception.LinkedResourceNotFoundException;
import com.mycompany.smartcampusapi1.model.Sensor;
import com.mycompany.smartcampusapi1.model.Room;
import com.mycompany.smartcampusapi1.store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.stream.Collectors;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    private final DataStore store = DataStore.getInstance();

    // GET /api/v1/sensors?type=CO2 - List all sensors with optional filter
    @GET
    public Response getAllSensors(@QueryParam("type") String type) {
        List<Sensor> sensorList = new ArrayList<>(store.getSensors().values());

        if (type != null && !type.isEmpty()) {
            sensorList = sensorList.stream()
                    .filter(s -> s.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
        }

        return Response.ok(sensorList).build();
    }

    // POST /api/v1/sensors - Register a new sensor
    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor.getId() == null || sensor.getId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "Sensor ID is required")).build();
        }

        // Validate roomId exists
        if (sensor.getRoomId() == null || !store.getRooms().containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException(
                "Room with ID '" + sensor.getRoomId() + "' does not exist."
            );
        }

        if (store.getSensors().containsKey(sensor.getId())) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", "Sensor with this ID already exists")).build();
        }

        // Set default status if not provided
        if (sensor.getStatus() == null || sensor.getStatus().isEmpty()) {
            sensor.setStatus("ACTIVE");
        }

        store.getSensors().put(sensor.getId(), sensor);
        store.getReadings().put(sensor.getId(), new ArrayList<>());

        // Link sensor to room
        Room room = store.getRooms().get(sensor.getRoomId());
        room.getSensorIds().add(sensor.getId());

        return Response.status(Response.Status.CREATED).entity(sensor).build();
    }

    // GET /api/v1/sensors/{sensorId} - Get specific sensor
    @GET
    @Path("/{sensorId}")
    public Response getSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = store.getSensors().get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Sensor not found: " + sensorId)).build();
        }
        return Response.ok(sensor).build();
    }

    // Sub-resource locator for readings
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
        return new SensorReadingResource(sensorId);
    }
}
