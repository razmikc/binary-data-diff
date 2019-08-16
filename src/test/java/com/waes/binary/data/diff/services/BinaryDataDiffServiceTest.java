package com.waes.binary.data.diff.services;

import com.waes.binary.data.diff.exceptions.BinaryDataMissingSideException;
import com.waes.binary.data.diff.exceptions.BinaryDataNotFoundException;
import com.waes.binary.data.diff.models.BinaryData;
import com.waes.binary.data.diff.models.BinaryDataDiffResult;
import com.waes.binary.data.diff.models.BinaryDataDiffResultType;
import com.waes.binary.data.diff.reporsitories.BinaryDataDiffRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Base64;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BinaryDataDiffServiceTest {
    @InjectMocks
    private BinaryDataDiffServiceImpl binaryDataDiffService;

    @Mock
    public BinaryDataDiffRepository repository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetLeftSideIfSideAlreadyExists() {
        final long diffId = 55;
        final BinaryData binaryData = new BinaryData(diffId);
        final byte[] leftSide = "left_data".getBytes();

        when(repository.findById(diffId)).thenReturn(Optional.of(binaryData));
        when(repository.save(any(BinaryData.class))).then(returnsFirstArg());

        final BinaryData data = binaryDataDiffService.setLeftSide(diffId, leftSide);

        assertThat(data.getId(), equalTo(diffId));
        assertThat(data.getLeft(), equalTo(leftSide));
    }

    @Test
    public void testSetLeftSide() {
        final long diffId = 55;
        final byte[] leftSide = "left_data".getBytes();

        when(repository.findById(diffId)).thenReturn(Optional.empty());
        when(repository.save(any(BinaryData.class))).then(returnsFirstArg());

        final BinaryData data = binaryDataDiffService.setLeftSide(diffId, leftSide);

        assertThat(data.getId(), equalTo(diffId));
        assertThat(data.getLeft(), equalTo(leftSide));
    }

    @Test
    public void testSetRightSide() {
        final long diffId = 55;
        final BinaryData binaryData = new BinaryData(diffId);
        final byte[] rightSide = "right_data".getBytes();

        when(repository.findById(diffId)).thenReturn(Optional.of(binaryData));
        when(repository.save(any(BinaryData.class))).then(returnsFirstArg());

        final BinaryData data = binaryDataDiffService.setRightSide(diffId, rightSide);

        assertThat(data.getId(), equalTo(diffId));
        assertThat(data.getRight(), equalTo(rightSide));
    }

    @Test
    public void testSetRightSideIfAlreadyExists() {
        final long diffId = 55;
        final byte[] rightSide = "right_data".getBytes();

        when(repository.findById(diffId)).thenReturn(Optional.empty());
        when(repository.save(any(BinaryData.class))).then(returnsFirstArg());

        final BinaryData data = binaryDataDiffService.setRightSide(diffId, rightSide);

        assertThat(data.getId(), equalTo(diffId));
        assertThat(data.getRight(), equalTo(rightSide));
    }

    @Test(expected = BinaryDataNotFoundException.class)
    public void testCompareBinaryDataShouldThrowExceptionIfDataNotFound() {
        final long diffId = 1;

        when(repository.findById(diffId)).thenReturn(Optional.empty());

        binaryDataDiffService.compareBinaryData(diffId);

    }

    @Test(expected = BinaryDataMissingSideException.class)
    public void testCompareBinaryDataShouldThrowExceptionIfRightSideIsMissing() {
        final long diffId = 1;

        final BinaryData data = new BinaryData(diffId);
        data.setLeft("left".getBytes());

        when(repository.findById(diffId)).thenReturn(Optional.of(data));

        binaryDataDiffService.compareBinaryData(diffId);

    }


    @Test(expected = BinaryDataMissingSideException.class)
    public void testCompareBinaryDataShouldThrowExceptionIfLeftSidesIsMissing() {
        final long diffId = 1;

        final BinaryData data = new BinaryData(diffId);
        data.setRight("right".getBytes());

        when(repository.findById(diffId)).thenReturn(Optional.of(data));

        binaryDataDiffService.compareBinaryData(diffId);

    }

    @Test
    public void testCompareBinaryDataIfSidesAreIdentical() {
        final long diffId = 15;

        final BinaryData data = new BinaryData(diffId);
        data.setRight(Base64.getEncoder().encode("base64 encoded binary data".getBytes()));
        data.setLeft(Base64.getEncoder().encode("base64 encoded binary data".getBytes()));

        when(repository.findById(diffId)).thenReturn(Optional.of(data));

        BinaryDataDiffResult binaryDataDiffResult = binaryDataDiffService.compareBinaryData(diffId);
        assertThat((binaryDataDiffResult).getType(),
                equalTo(BinaryDataDiffResultType.ARE_IDENTICAL));
        assertThat(binaryDataDiffResult.getLength(), is(nullValue()));
        assertThat(binaryDataDiffResult.getOffsets(), is(nullValue()));
    }

    @Test
    public void testCompareBinaryDataIfSideHaveDifferentSizes() {
        final long diffId = 11;

        final BinaryData data = new BinaryData(diffId);
        data.setRight(Base64.getEncoder().encode("base64 encoded binary data".getBytes()));
        data.setLeft(Base64.getEncoder().encode("base64 encoded".getBytes()));

        when(repository.findById(diffId)).thenReturn(Optional.of(data));

        BinaryDataDiffResult binaryDataDiffResult = binaryDataDiffService.compareBinaryData(diffId);
        assertThat(binaryDataDiffResult.getType(),
                equalTo(BinaryDataDiffResultType.HAVE_DIFFERENT_SIZES));
        assertThat(binaryDataDiffResult.getLength(), is(nullValue()));
        assertThat(binaryDataDiffResult.getOffsets(), is(nullValue()));

    }

    @Test
    public void testCompareBinaryDataIfSideHaveDifferentContents() {
        final long diffId = 11;

        final BinaryData data = new BinaryData(diffId);
        data.setRight(Base64.getEncoder().encode("base64 encoded binary data".getBytes()));
        data.setLeft(Base64.getEncoder().encode("base64 encoded test11 data".getBytes()));

        when(repository.findById(diffId)).thenReturn(Optional.of(data));

        BinaryDataDiffResult binaryDataDiffResult = binaryDataDiffService.compareBinaryData(diffId);
        assertThat(binaryDataDiffResult.getType(),
                equalTo(BinaryDataDiffResultType.HAVE_DIFFERENT_CONTENT));
        assertThat(binaryDataDiffResult.getLength(), equalTo("26"));
        assertThat(binaryDataDiffResult.getOffsets(), equalTo("15,16,17,18,19,20"));

    }
}
