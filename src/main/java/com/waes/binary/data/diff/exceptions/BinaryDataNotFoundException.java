package com.waes.binary.data.diff.exceptions;

/**
 * Throws if there is no binary data with given id
 */
public class BinaryDataNotFoundException extends RuntimeException {

    public BinaryDataNotFoundException(Long id) {
        super("Could not find binary data with id: " + id);
    }
}
