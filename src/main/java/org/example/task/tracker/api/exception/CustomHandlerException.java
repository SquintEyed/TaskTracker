package org.example.task.tracker.api.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class CustomHandlerException{

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDto> badRequestExceptionHandler(Exception ex) {
        log.error("BadRequestException during execution of application: " +  ex.getMessage());
        return new ResponseEntity<>(new ErrorDto(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> notFoundExceptionHandler(Exception ex) {
        log.error("NotFoundException during execution of application: " +  ex.getMessage());
        return new ResponseEntity<>(new ErrorDto(HttpStatus.NOT_FOUND, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> otherException(Exception ex) {
        log.error("Exception during execution of application: " +  ex.getMessage());
        return new ResponseEntity<>(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
