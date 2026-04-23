
package com.mycompany.smartcampusapi1.store;

import com.mycompany.smartcampusapi1.model.Room;
import com.mycompany.smartcampusapi1.model.Sensor;
import com.mycompany.smartcampusapi1.model.SensorReading;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {

    // Singleton instance
    private static final DataStore INSTANCE = new DataStore();

    // Thread-safe maps
    private final Map<String, Room> rooms = new ConcurrentHashMap<>();
    private final Map<String, Sensor> sensors = new ConcurrentHashMap<>();
    private final Map<String, List<SensorReading>> readings = new ConcurrentHashMap<>();

    private DataStore() {
        // Sample data
        Room r1 = new Room("LIB-301", "Library Quiet Study", 50);
        rooms.put(r1.getId(), r1);

        Room r2 = new Room("LAB-101", "Computer Lab", 30);
        rooms.put(r2.getId(), r2);

        Sensor s1 = new Sensor("TEMP-001", "Temperature", "ACTIVE", 22.5, "LIB-301");
        sensors.put(s1.getId(), s1);
        r1.getSensorIds().add(s1.getId());

        readings.put(s1.getId(), new ArrayList<>());
    }

    public static DataStore getInstance() {
        return INSTANCE;
    }

    public Map<String, Room> getRooms() { return rooms; }
    public Map<String, Sensor> getSensors() { return sensors; }
    public Map<String, List<SensorReading>> getReadings() { return readings; }
}
