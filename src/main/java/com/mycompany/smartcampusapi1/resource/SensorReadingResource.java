package com.mycompany.smartcampusapi1.resource;

import com.mycompany.smartcampusapi1.exception.SensorUnavailableException;
import com.mycompany.smartcampusapi1.model.Sensor;
import com.mycompany.smartcampusapi1.model.SensorReading;
import com.mycompany.smartcampusapi1.store.DataStore;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    private final String sensorId;
    private final DataStore store = DataStore.getInstance();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    // GET /api/v1/sensors/{sensorId}/readings
    @GET
    public Response getReadings() {
        Sensor sensor = store.getSensors().get(sensorId);
        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Sensor not found: " + sensorId)).build();
        }

        List<SensorReading> readingList = store.getReadings()
                .getOrDefault(sensorId, new ArrayList<>());

        return Response.ok(readingList).build();
    }

    // POST /api/v1/sensors/{sensorId}/readings
    @POST
    public Response addReading(SensorReading reading) {
        Sensor sensor = store.getSensors().get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Sensor not found: " + sensorId)).build();
        }

        // Check sensor status - MAINTENANCE sensors cannot accept readings
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                "Sensor '" + sensorId + "' is under MAINTENANCE and cannot accept new readings."
            );
        }

        // Create new reading with generated ID and timestamp
        SensorReading newReading = new SensorReading(reading.getValue());

        // Store reading
        store.getReadings()
             .computeIfAbsent(sensorId, k -> new ArrayList<>())
             .add(newReading);

        // Side effect: update parent sensor's currentValue
        sensor.setCurrentValue(newReading.getValue());

        return Response.status(Response.Status.CREATED).entity(newReading).build();
    }
}