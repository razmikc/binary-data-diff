package com.waes.binary.data.diff.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BinaryDataMissingSideAdvice {

    @ExceptionHandler(BinaryDataMissingSideException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    String binaryDiffDataNotFoundHandler(BinaryDataMissingSideException ex) {
        return ex.getMessage();
    }
}
