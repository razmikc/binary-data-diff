package com.waes.binary.data.diff.services;

import com.waes.binary.data.diff.exceptions.BinaryDataMissingSideException;
import com.waes.binary.data.diff.exceptions.BinaryDataNotFoundException;
import com.waes.binary.data.diff.models.BinaryData;
import com.waes.binary.data.diff.models.BinaryDataDiffResult;
import com.waes.binary.data.diff.models.BinaryDataDiffResult.BinaryDataDiffResultBuilder;
import com.waes.binary.data.diff.models.BinaryDataDiffResultType;
import com.waes.binary.data.diff.reporsitories.BinaryDataDiffRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
public class BinaryDataDiffServiceImpl implements BinaryDataDiffService {

    private static final Logger LOG = LoggerFactory.getLogger(BinaryDataDiffServiceImpl.class);

    private BinaryDataDiffRepository binaryDataDiffRepository;

    @Autowired
    public BinaryDataDiffServiceImpl(BinaryDataDiffRepository binaryDataDiffRepository) {
        this.binaryDataDiffRepository = binaryDataDiffRepository;
    }

    @Override
    public BinaryData setLeftSide(long id, byte[] leftData) {
        final BinaryData leftSide = binaryDataDiffRepository.findById(id).orElse(new BinaryData(id));
        leftSide.setLeft(leftData);
        return binaryDataDiffRepository.save(leftSide);
    }

    @Override
    public BinaryData setRightSide(long id, byte[] rightData) {
        final BinaryData rightSide = binaryDataDiffRepository.findById(id).orElse(new BinaryData(id));
        rightSide.setRight(rightData);
        return binaryDataDiffRepository.save(rightSide);
    }

    @Override
    public BinaryDataDiffResult compareBinaryData(long id) {
        BinaryData binaryData = binaryDataDiffRepository.findById(id).orElseThrow(() -> {
            LOG.error("Binary data is missing for given id: '{}'", id);
            return new BinaryDataNotFoundException(id);
        });
        // Check if both sides are set otherwise throw exception
        if (binaryData.getLeft() == null || binaryData.getRight() == null) {
            LOG.error("One of sides is missing, id: '{}'", id);
            throw new BinaryDataMissingSideException(id);
        }

        LOG.debug("Comparing left and right sides for diff id: '{}'", id);
        return this.compareData(binaryData.getLeft(), binaryData.getRight());
    }

    private BinaryDataDiffResult compareData(byte[] left, byte[] right) {
        byte[] leftBytes = Base64.getDecoder().decode(left);
        byte[] rightBytes = Base64.getDecoder().decode(right);

        if (Arrays.equals(leftBytes, rightBytes)) {
            return new BinaryDataDiffResultBuilder(BinaryDataDiffResultType.ARE_IDENTICAL).build();
        } else if (leftBytes.length != rightBytes.length) {
            return new BinaryDataDiffResultBuilder(BinaryDataDiffResultType.HAVE_DIFFERENT_SIZES).build();
        } else {
            List<String> offsets = new ArrayList<>();
            for (int i = 0; i < leftBytes.length; ++i) {
                if ((leftBytes[i] ^ rightBytes[i]) != 0) {
                    offsets.add("" + i);
                }
            }
            return new BinaryDataDiffResultBuilder(BinaryDataDiffResultType.HAVE_DIFFERENT_CONTENT)
                    .offsets(offsets)
                    .length(leftBytes.length)
                    .build();
        }
    }
}
