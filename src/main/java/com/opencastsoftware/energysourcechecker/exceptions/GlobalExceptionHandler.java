package com.opencastsoftware.energysourcechecker.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PostcodeException.class)
    public ResponseEntity<String> handlePostcodeException(PostcodeException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StartDateException.class)
    public ResponseEntity<String> handleStartDateException(StartDateException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(EndDateException.class)
    public ResponseEntity<String> handleStartDateException(EndDateException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UserAnswersException.class)
    public ResponseEntity<String> handleUserAnswersException(UserAnswersException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CarbonIntensityClientException.class)
    public ResponseEntity<String> handCarbonIntensityClientException(CarbonIntensityClientException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
