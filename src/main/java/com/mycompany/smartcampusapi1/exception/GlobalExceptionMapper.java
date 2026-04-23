
package com.mycompany.smartcampusapi1.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable exception) {
        // Log the full stack trace server-side only
        LOGGER.log(Level.SEVERE, "Unexpected error occurred", exception);

        // Return generic message to client - NEVER expose stack trace
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON)
                .entity(Map.of(
                    "status", 500,
                    "error", "Internal Server Error",
                    "message", "An unexpected error occurred. Please contact the administrator."
                ))
                .build();
    }
}
