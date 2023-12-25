package com.blubank.doctorappointment.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.webjars.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler
        extends ResponseEntityExceptionHandler{

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(Exception ex , ModelAndView model){
        model.addObject("errorMessage" , "An unhandled error occurred: " + ex.getMessage());
        return model;
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

    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeError(RuntimeException ex , ModelAndView model){
        model.addObject("errorMessage" , "An error occurred: " + ex.getMessage());
        return model;
    }
}