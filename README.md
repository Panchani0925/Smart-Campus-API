**Smart Campus REST API**

Overview : This project implements a RESTful API for managing a university Smart Campus system, developed using JAX-RS (Jersey).

The API is designed to manage:
 
* Rooms
* Sensors
* Sensor Readings  

It follows REST principles such as:

* Resource-based design
* Proper HTTP methods
* Meaningful status codes
* JSON communication

The system simulates a real-world backend service for campus facilities management.

Technologies Used:

* Java
* JAX-RS (Jersey)
* Maven
* In-memory data structures (HashMap, ArrayList)

**How to Run the Project**

1.) Clone the repository:
        git clone <your-repo-link>
2.) Open the project in IDE ( NetBeans )
3.) Build using Maven:
        mvn clean install
4.) Run the server ( Tomcat )
5.) Access API:
      http://localhost:8080/SmartCampusAPI1/

**Sample API Endpoints**

  **Rooms**
    * GET /api/v1/rooms
    * POST /api/v1/rooms
    * GET /api/v1/rooms/{id}
    * DELETE /api/v1/rooms/{id}
    
  **Sensors**
    * GET /api/v1/sensors
    * GET /api/v1/sensors?type=CO2
    * POST /api/v1/sensors
  
  **Sensor Readings**
    * GET /api/v1/sensors/{id}/readings
    * POST /api/v1/sensors/{id}/readings

**Sample CURL Commands**

# Get all rooms
curl -X GET http://localhost:8080/api/v1/rooms

# Create a room
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{"id":"R1","name":"Lab","capacity":50}'

# Create sensor
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{"id":"S1","type":"CO2","status":"ACTIVE","roomId":"R1"}'

# Filter sensors
curl -X GET "http://localhost:8080/api/v1/sensors?type=CO2"

# Add reading
curl -X POST http://localhost:8080/api/v1/sensors/S1/readings \ 
-H "Content-Type: application/json" \ 
-d '{"value":45.5}'

**Coursework Answers**

_Part 1: Service Architecture & Setup_

1. JAX-RS Resource Lifecycle

     Normally, JAX-RS creates a new object for each request.
     So, every HTTP request uses a different object.
     This helps avoid problems with shared data.

   But in this project:
      We use in-memory data like HashMap and ArrayList.
      So, data should be stored in static collections to keep it safe.
      We may also need synchronization to prevent multiple users changing data at the same time.

   Because of this design:
      Data will not be lost
      The system will be safe when many users access it at the same time

2. Importance of HATEOAS

    HATEOAS (Hypermedia as the Engine of Application State)means APIs return links to related resources.

    Benefits:
      Clients do not need to save or remember URLs
      Easy to move from one resource to another
      API becomes more flexible and can grow easily

  Compared to static documentation:
      Clients can find endpoints by themselves
      No need to depend too much on external documents

_Part 2: Room Management_

1. Returning IDs vs Full Objects

   Returning only IDs:
      Less data transfer (better performance)
      Requires extra requests from client

  Returning full objects:
      More useful information in one response
      Slightly higher bandwidth usage

  In this API, returning full objects is better for usability.

2. DELETE Idempotency

  Yes, DELETE is idempotent.

  Explanation:
    First request → deletes the room
    Repeated request → room no longer exists
    Response remains consistent (e.g., 404)

  Thus, multiple identical requests produce the same result.

_Part 3: Sensor Operations_

1. @Consumes JSON Behavior

If client sends:
      text/plain or application/xml

Then:
      JAX-RS will reject the request
      Returns 415 Unsupported Media Type

Because method only accepts JSON.

2. QueryParam vs PathParam

Using QueryParam:
    /sensors?type=CO2

Better because:
    Designed for filtering
    Flexible and optional
    Supports multiple filters

PathParam:
    /sensors/type/CO2

Less flexible and not ideal for search operations.


_Part 4: Sub-Resources_

1. Sub-Resource Locator Benefits

Advantages:
    Cleaner code structure
    Separation of concerns
    Easier maintenance

Instead of one large class:
    Logic is split into smaller classes
    Improves readability

2. Sensor Reading Update Logic

When a new reading is added:
    It is stored in reading list
    The sensor’s currentValue is updated

This ensures:
    Data consistency
    Latest value always available

_Part 5: Error Handling & Logging_

1. HTTP 409 Conflict

Used when:
    Trying to delete a room with sensors

Reason:
    Operation conflicts with current state

2. HTTP 422 vs 404

422 is better because:
    Request format is valid
    But contains invalid data (wrong roomId)

404 is for missing endpoint, not invalid payload.

3. HTTP 403 Forbidden

Used when:
    Sensor is in MAINTENANCE
    Cannot accept readings

4. Risk of Exposing Stack Traces

Exposing stack traces can reveal:
      Internal class names
      File structure
      Server logic

Attackers can use this to:
      Identify vulnerabilities
      Plan attacks

Therefore, API must hide internal errors.

5. Logging with Filters

Using filters is better because:
    Centralized logging
    No duplicate code
    Cleaner resource methods

Logs:
    Request method + URI
    Response status

**Conclusion**

This project demonstrates:
    Proper RESTful API design
    Use of JAX-RS framework
    Error handling and validation
    Clean and scalable architecture

The API is designed to simulate a real-world smart campus backend system.


    
