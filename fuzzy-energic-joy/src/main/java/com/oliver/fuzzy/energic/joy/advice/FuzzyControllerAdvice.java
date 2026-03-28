package com.oliver.fuzzy.energic.joy.advice;

import com.oliver.fuzzy.energic.joy.exception.SensorNotFoundException;
import com.oliver.fuzzy.energic.joy.exception.ValidationException;
import com.oliver.fuzzy.energic.joy.model.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FuzzyControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(ValidationException ex){
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handle(SensorNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

}
