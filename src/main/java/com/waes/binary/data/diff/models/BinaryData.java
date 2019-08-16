package com.waes.binary.data.diff.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Objects;
import java.util.Optional;

@Table(name = "binary_data")
@Entity
public class BinaryData {
    /**
     * The unique identifier of sides
     */
    @Id
    private long id;
    /**
     * Left side binary data
     */
    @Lob
    private byte[] left;

    /**
     * Right side binary data
     */
    @Lob
    private byte[] right;

    /**
     * Default constructor
     */
    private BinaryData() {

    }

    /**
     * Constructor with id field
     *
     * @param id unique identifier of sides
     */
    public BinaryData(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getLeft() {
        return left;
    }

    public Optional<byte[]> getLeftAsOptional() {
        return Optional.ofNullable(left);
    }

    public void setLeft(byte[] left) {
        this.left = left;
    }

    public byte[] getRight() {
        return right;
    }

    public Optional<byte[]> getRightAsOptional() {
        return Optional.ofNullable(right);
    }

    public void setRight(byte[] right) {
        this.right = right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryData that = (BinaryData) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
