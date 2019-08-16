package com.waes.binary.data.diff.models;

/**
 * Comparision result types
 */
public enum BinaryDataDiffResultType {
    ARE_IDENTICAL("Sides are identical"),
    HAVE_DIFFERENT_SIZES("Sides have different sizes"),
    HAVE_DIFFERENT_CONTENT("Sides have different content");

    private final String description;

    BinaryDataDiffResultType(String description) {
        this.description = description;
    }

    public final String getDescription() {
        return description;
    }
}
