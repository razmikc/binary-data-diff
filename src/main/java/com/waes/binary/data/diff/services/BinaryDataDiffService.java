package com.waes.binary.data.diff.services;

import com.waes.binary.data.diff.exceptions.BinaryDataMissingSideException;
import com.waes.binary.data.diff.exceptions.BinaryDataNotFoundException;
import com.waes.binary.data.diff.models.BinaryData;
import com.waes.binary.data.diff.models.BinaryDataDiffResult;

public interface BinaryDataDiffService {
    /**
     * Save left side of comparision
     *
     * @param id       diff identifier
     * @param leftData left side data to be saved
     * @return saved binary data
     */
    BinaryData setLeftSide(long id, byte[] leftData);

    /**
     * Save right side of comparision
     *
     * @param id        diff identifier
     * @param rightData right side data to be saved
     * @return saved binary data
     */
    BinaryData setRightSide(long id, byte[] rightData);

    /**
     * Compares two sides based on given identifier
     *
     * @param id diff identifier
     * @return Comparision result of two sides
     * @throws BinaryDataMissingSideException if one of sides is missing
     * @throws BinaryDataNotFoundException    if there is no binary data to be compared for given id
     */
    BinaryDataDiffResult compareBinaryData(long id);
}
