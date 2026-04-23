
package com.mycompany.smartcampusapi1;


import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

import com.mycompany.smartcampusapi1.resource.DiscoveryResource;
import com.mycompany.smartcampusapi1.resource.RoomResource;
import com.mycompany.smartcampusapi1.resource.SensorResource;
import com.mycompany.smartcampusapi1.exception.*;
import com.mycompany.smartcampusapi1.filter.LoggingFilter;

@ApplicationPath("/api/v1")
public class SmartCampusApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();

        // Resources
        classes.add(DiscoveryResource.class);
        classes.add(RoomResource.class);
        classes.add(SensorResource.class);

        // Exception Mappers
        classes.add(RoomNotEmptyExceptionMapper.class);
        classes.add(LinkedResourceNotFoundExceptionMapper.class);
        classes.add(SensorUnavailableExceptionMapper.class);
        classes.add(GlobalExceptionMapper.class);

        // Filters
        classes.add(LoggingFilter.class);

        return classes;
    }
}