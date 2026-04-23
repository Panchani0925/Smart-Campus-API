
package com.mycompany.smartcampusapi1.exception;

public class RoomNotEmptyException extends RuntimeException {
    public RoomNotEmptyException(String message) {
        super(message);
    }
}