package com.seekstorm.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SeekStormClientTest {

    @Test
    void builderCreatesClient() {
        assertDoesNotThrow(() -> SeekStormClient.builder()
                .baseUrl("http://127.0.0.1")
                .apiKey("test-key")
                .build());
    }
}