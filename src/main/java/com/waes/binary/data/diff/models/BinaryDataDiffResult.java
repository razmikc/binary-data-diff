package com.waes.binary.data.diff.models;

import java.util.List;

/**
 * Json Base64 encoded binary data diff result
 */
public final class BinaryDataDiffResult {

    /**
     * Offsets of different bytes
     */
    private final String offsets;

    /**
     * Length of data
     */
    private final String length;

    /**
     * Type of diff result
     */
    private final BinaryDataDiffResultType type;
    /**
     * Description of result
     */
    private final String description;

    /**
     * Constructor is used only by builder
     *
     * @param builder result build static class instance
     */
    private BinaryDataDiffResult(BinaryDataDiffResultBuilder builder) {
        this.offsets = builder.offsets;
        this.length = builder.length;
        this.type = builder.type;
        this.description = type.getDescription();
    }

    public String getOffsets() {
        return offsets;
    }

    public String getLength() {
        return length;
    }

    public BinaryDataDiffResultType getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public static class BinaryDataDiffResultBuilder {
        private String offsets;

        private String length;

        private BinaryDataDiffResultType type;

        public BinaryDataDiffResultBuilder(BinaryDataDiffResultType type) {
            this.type = type;
        }

        public BinaryDataDiffResultBuilder offsets(List<String> offsets) {
            this.offsets = String.join(",", offsets);
            return this;
        }

        public BinaryDataDiffResultBuilder length(int length) {
            this.length = String.valueOf(length);
            return this;
        }

        public BinaryDataDiffResult build() {
            return new BinaryDataDiffResult(this);
        }
    }
}
