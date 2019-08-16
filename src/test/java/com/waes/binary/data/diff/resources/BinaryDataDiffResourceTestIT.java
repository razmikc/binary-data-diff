package com.waes.binary.data.diff.resources;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Base64;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BinaryDataDiffResourceTestIT {

    @Autowired
    private MockMvc mvc;

    @Test
    public void shouldSetLeftSide() throws Exception {
        final int diffId = 1;

        byte[] leftData = Base64.getEncoder().encode("left-data".getBytes());

        mvc.perform(MockMvcRequestBuilders
                .post("/v1/diff/{id}/left", diffId)
                .content(leftData)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldSetRightSide() throws Exception {
        final int diffId = 1;

        byte[] rightData = Base64.getEncoder().encode("right-data".getBytes());

        mvc.perform(MockMvcRequestBuilders
                .post("/v1/diff/{id}/right", diffId)
                .content(rightData)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void compareDataWithIdenticalSides() throws Exception {

        final int diffId = 1;

        byte[] leftData = Base64.getEncoder().encode("testdata".getBytes());
        byte[] rightData = Base64.getEncoder().encode("testdata".getBytes());

        mvc.perform(MockMvcRequestBuilders
                .post("/v1/diff/{id}/right", diffId)
                .content(rightData)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .post("/v1/diff/{id}/left", diffId)
                .content(leftData)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/{id}", diffId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"offsets\":null,\"length\":null,\"type\":\"ARE_IDENTICAL\",\"description\":\"Sides are identical\"}"));

    }

    @Test
    public void compareDataForSidesWithUnequalSizes() throws Exception {
        final int diffId = 1;

        byte[] leftData = Base64.getEncoder().encode("testdata1".getBytes());
        byte[] rightData = Base64.getEncoder().encode("testdata".getBytes());

        mvc.perform(MockMvcRequestBuilders
                .post("/v1/diff/{id}/right", diffId)
                .content(rightData)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .post("/v1/diff/{id}/left", diffId)
                .content(leftData)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/{id}", diffId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"offsets\":null,\"length\":null,\"type\":\"HAVE_DIFFERENT_SIZES\",\"description\":\"Sides have different sizes\"}"));

    }

    @Test
    public void compareDataForSidesWithDifferentContent() throws Exception {
        final int diffId = 1;

        byte[] leftData = Base64.getEncoder().encode("testdata1".getBytes());
        byte[] rightData = Base64.getEncoder().encode("testdata2".getBytes());

        mvc.perform(MockMvcRequestBuilders
                .post("/v1/diff/{id}/right", diffId)
                .content(rightData)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .post("/v1/diff/{id}/left", diffId)
                .content(leftData)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/{id}", diffId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"offsets\":\"8\",\"length\":\"9\",\"type\":\"HAVE_DIFFERENT_CONTENT\",\"description\":\"Sides have different content\"}"));

    }

    @Test
    public void compareDataAndOneOfSidesIsMissing() throws Exception {
        final int diffId = 55;

        byte[] rightData = Base64.getEncoder().encode("testdata".getBytes());

        mvc.perform(MockMvcRequestBuilders
                .post("/v1/diff/{id}/right", diffId)
                .content(rightData)
                .contentType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(status().isOk());

        mvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/{id}", diffId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void compareDataAndThrowNotFoundException() throws Exception {
        final int diffId = 10;

        mvc.perform(MockMvcRequestBuilders
                .get("/v1/diff/{id}", diffId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
