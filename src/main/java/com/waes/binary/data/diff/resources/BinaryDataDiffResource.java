package com.waes.binary.data.diff.resources;

import com.waes.binary.data.diff.models.BinaryDataDiffResult;
import com.waes.binary.data.diff.services.BinaryDataDiffService;
import com.waes.binary.data.diff.services.BinaryDataDiffServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController("v1/diff")
public class BinaryDataDiffResource {

    private static final Logger LOG = LoggerFactory.getLogger(BinaryDataDiffServiceImpl.class);

    private BinaryDataDiffService binaryDataDiffService;

    @Autowired
    public BinaryDataDiffResource(BinaryDataDiffService binaryDataDiffService) {
        this.binaryDataDiffService = binaryDataDiffService;
    }

    /**
     * Returns diff result of two sides
     *
     * @param id unique identifier of sides
     * @return result json representation
     */
    @GetMapping(value = "/v1/diff/{id}", produces = "application/json")
    public ResponseEntity<BinaryDataDiffResult> compareData(@PathVariable long id) {
        LOG.debug("Getting left and right sides comparision result, id: '{}'", id);

        BinaryDataDiffResult result = binaryDataDiffService.compareBinaryData(id);
        LOG.info("Diff result of left and right sides is: '{}'", result.getDescription());

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Posting left side data
     *
     * @param id   unique identifier of sides
     * @param data left side base64 encoded binary data
     * @return response entry with status code
     */
    @PostMapping(value = "/v1/diff/{id}/left", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Void> addLeftBinaryData(@PathVariable("id") Long id, @Valid @RequestBody byte[] data) {
        LOG.debug("Setting left side data with value: '{}', id: '{}'", data, id);
        binaryDataDiffService.setLeftSide(id, data);

        LOG.info("Left side data has been saved successfully, id: '{}'", id);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Posting right side data
     *
     * @param id   unique identifier of sides
     * @param data right side base64 encoded binary data
     * @return response entry with status code
     */
    @PostMapping(value = "/v1/diff/{id}/right", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<Void> addRightBinaryData(@PathVariable("id") Long id, @Valid @RequestBody byte[] data) {
        LOG.debug("Setting right side data with value: '{}', id: '{}'", data, id);

        binaryDataDiffService.setRightSide(id, data);
        LOG.info("Right side data has been saved successfully, id: '{}'", id);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
