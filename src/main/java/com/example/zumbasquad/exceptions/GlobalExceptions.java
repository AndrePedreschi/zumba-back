package com.example.zumbasquad.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GlobalExceptions extends Exception {
    @ExceptionHandler
    public ResponseEntity<String> processErrorNotFound(ResourceNotFoundException ex){
        return ResponseEntity.status((HttpStatus.NOT_FOUND)).body(ex.getMessage());
    }
}
