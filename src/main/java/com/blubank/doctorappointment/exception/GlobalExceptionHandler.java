package com.blubank.doctorappointment.exception;

import com.hazelcast.org.snakeyaml.engine.v2.exceptions.DuplicateKeyException;
import com.sun.jdi.request.DuplicateRequestException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.webjars.NotFoundException;

import javax.validation.ValidationException;

@ControllerAdvice
public class GlobalExceptionHandler
        extends ResponseEntityExceptionHandler{

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> defaultErrorHandler(Exception ex , WebRequest request){
        String error = "errorMessage: " + ex.getMessage();
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleInvalidState(IllegalStateException ex ,WebRequest request){
        String error = "errorMessage: " + ex.getMessage();
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        String error = "errorMessage: " + ex.getMessage();
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex, WebRequest request) {
        String error = "errorMessage: " + ex.getMessage();
        return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeError(RuntimeException ex , WebRequest request){
        String error = "errorMessage: " + ex.getMessage();
        return  handleExceptionInternal(ex,error,new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,request);
    }

    @ExceptionHandler(DuplicateRequestException.class)
    public ResponseEntity<Object> handleDuplicateEntryException(DuplicateKeyException ex) {
        return new ResponseEntity<>("Duplicate request: " + ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        return new ResponseEntity<>("errorMessage: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}