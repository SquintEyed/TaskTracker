package org.example.task.tracker.api.exception;


public class BadRequestException extends RuntimeException {

    public BadRequestException(String message){
        super(message);
    }
}
