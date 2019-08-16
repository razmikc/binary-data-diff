package com.waes.binary.data.diff.services;

import com.waes.binary.data.diff.exceptions.BinaryDataMissingSideException;
import com.waes.binary.data.diff.exceptions.BinaryDataNotFoundException;
import com.waes.binary.data.diff.models.BinaryData;
import com.waes.binary.data.diff.models.BinaryDataDiffResult;
import com.waes.binary.data.diff.models.BinaryDataDiffResultType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BinaryDataDiffServiceTestIT {

    @Autowired
    private BinaryDataDiffServiceImpl binaryDataDiffService;

    @Test
    public void testSetLeftSideIfSideAlreadyExists() {
        final long diffId = 77;

        binaryDataDiffService.setLeftSide(diffId, Base64.getEncoder().encode("left_data".getBytes()));

        byte[] newData = Base64.getEncoder().encode("left_data_1".getBytes());
        final BinaryData data = binaryDataDiffService.setLeftSide(diffId, newData);


        assertThat(data.getId(), equalTo(diffId));
        assertThat(data.getLeft(), equalTo(newData));
        assertThat(data.getRight(), is(nullValue()));
    }

    @Test
    public void testSetLeftSide() {
        final long diffId = 66;

        byte[] leftSide = Base64.getEncoder().encode("left_data_1".getBytes());
        final BinaryData data = binaryDataDiffService.setLeftSide(diffId, leftSide);

        assertThat(data.getId(), equalTo(diffId));
        assertThat(data.getLeft(), equalTo(leftSide));
        assertThat(data.getRight(), is(nullValue()));
    }

    @Test
    public void testSetRightSideIfSideAlreadyExists() {
        final long diffId = 88;

        binaryDataDiffService.setRightSide(diffId, Base64.getEncoder().encode("left_data".getBytes()));

        byte[] newData = Base64.getEncoder().encode("left_data_1".getBytes());
        final BinaryData data = binaryDataDiffService.setRightSide(diffId, newData);


        assertThat(data.getId(), equalTo(diffId));
        assertThat(data.getRight(), equalTo(newData));
        assertThat(data.getLeft(), is(nullValue()));
    }

    @Test
    public void testSetRightSide() {
        final long diffId = 111;

        byte[] rightSide = Base64.getEncoder().encode("right_data".getBytes());
        final BinaryData data = binaryDataDiffService.setRightSide(diffId, rightSide);

        assertThat(data.getId(), equalTo(diffId));
        assertThat(data.getRight(), equalTo(rightSide));
        assertThat(data.getLeft(), is(nullValue()));
    }

    @Test(expected = BinaryDataNotFoundException.class)
    public void testCompareBinaryDataShouldThrowExceptionIfDataNotFound() {
        final long diffId = 2;

        binaryDataDiffService.compareBinaryData(diffId);

    }

    @Test(expected = BinaryDataMissingSideException.class)
    public void testCompareBinaryDataShouldThrowExceptionIfRightSideIsMissing() {
        final long diffId = 3;

        byte[] leftSide = Base64.getEncoder().encode("left_data".getBytes());
        binaryDataDiffService.setLeftSide(diffId, leftSide);

        binaryDataDiffService.compareBinaryData(diffId);

    }


    @Test(expected = BinaryDataMissingSideException.class)
    public void testCompareBinaryDataShouldThrowExceptionIfLeftSidesIsMissing() {
        final long diffId = 4;

        byte[] leftSide = Base64.getEncoder().encode("right_data".getBytes());
        binaryDataDiffService.setRightSide(diffId, leftSide);

        binaryDataDiffService.compareBinaryData(diffId);

    }

    @Test
    public void testCompareBinaryDataIfSidesAreIdentical() {
        final long diffId = 5;
        binaryDataDiffService.setLeftSide(diffId, Base64.getEncoder().encode("base64 encoded binary data".getBytes()));
        binaryDataDiffService.setRightSide(diffId, Base64.getEncoder().encode("base64 encoded binary data".getBytes()));

        BinaryDataDiffResult binaryDataDiffResult = binaryDataDiffService.compareBinaryData(diffId);
        assertThat((binaryDataDiffResult).getType(),
                equalTo(BinaryDataDiffResultType.ARE_IDENTICAL));
    }

    @Test
    public void testCompareBinaryDataIfSideHaveDifferentSizes() {
        final long diffId = 6;
        binaryDataDiffService.setLeftSide(diffId, Base64.getEncoder().encode("base64 encoded".getBytes()));
        binaryDataDiffService.setRightSide(diffId, Base64.getEncoder().encode("base64 encoded binary data".getBytes()));

        BinaryDataDiffResult binaryDataDiffResult = binaryDataDiffService.compareBinaryData(diffId);
        assertThat((binaryDataDiffResult).getType(),
                equalTo(BinaryDataDiffResultType.HAVE_DIFFERENT_SIZES));
    }

    @Test
    public void testCompareBinaryDataIfSideHaveDifferentContents() {
        final long diffId = 7;
        binaryDataDiffService.setLeftSide(diffId, Base64.getEncoder().encode("base64 enccded binaay data".getBytes()));
        binaryDataDiffService.setRightSide(diffId, Base64.getEncoder().encode("base64 encoded binary data".getBytes()));

        BinaryDataDiffResult binaryDataDiffResult = binaryDataDiffService.compareBinaryData(diffId);
        assertThat((binaryDataDiffResult).getType(),
                equalTo(BinaryDataDiffResultType.HAVE_DIFFERENT_CONTENT));
        assertThat((binaryDataDiffResult).getLength(), equalTo("26"));
        assertThat((binaryDataDiffResult).getOffsets(), equalTo("10,19"));

    }
}
