package com.royalmail.barcode;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

import static org.springframework.http.HttpStatus.OK;

@ControllerAdvice
public class BarcodeExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> onConstraintViolationException(final ConstraintViolationException e) {
        return new ResponseEntity<>(false, OK);
    }
}
