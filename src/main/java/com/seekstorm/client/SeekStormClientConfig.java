package com.seekstorm.client;

import java.net.URI;
import java.time.Duration;

/**
 * Immutable configuration for {@link SeekStormClient}.
 */
public record SeekStormClientConfig(
        URI baseUrl,
        String apiKey,
        Duration connectTimeout,
        Duration requestTimeout) {

    public SeekStormClientConfig {
        if (baseUrl == null) {
            baseUrl = URI.create("http://127.0.0.1");
        }
        if (connectTimeout == null) {
            connectTimeout = Duration.ofSeconds(10);
        }
        if (requestTimeout == null) {
            requestTimeout = Duration.ofSeconds(30);
        }
    }

    public static SeekStormClientConfig defaults(String apiKey) {
        return new SeekStormClientConfig(URI.create("http://127.0.0.1"), apiKey, Duration.ofSeconds(10), Duration.ofSeconds(30));
    }
}