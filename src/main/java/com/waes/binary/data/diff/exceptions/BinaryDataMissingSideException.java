package com.waes.binary.data.diff.exceptions;
/**
 * Throws if one of sides in comparision is missing
 */
public class BinaryDataMissingSideException extends RuntimeException {

    public BinaryDataMissingSideException(Long id) {
        super("One of comparision sides is missing, id: " + id);
    }
}
