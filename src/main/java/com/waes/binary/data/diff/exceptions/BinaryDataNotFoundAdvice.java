package com.waes.binary.data.diff.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BinaryDataNotFoundAdvice {

    @ExceptionHandler(BinaryDataNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String binaryDiffDataNotFoundHandler(BinaryDataNotFoundException ex) {
        return ex.getMessage();
    }
}
