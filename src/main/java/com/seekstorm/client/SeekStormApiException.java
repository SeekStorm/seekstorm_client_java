package com.seekstorm.client;

import java.net.http.HttpResponse;

/**
 * Exception thrown when a SeekStorm request returns a non-2xx response.
 */
public final class SeekStormApiException extends RuntimeException {
    private final int statusCode;
    private final String responseBody;

    /**
     * Creates a new exception.
     *
     * @param statusCode HTTP status code returned by the server
     * @param responseBody raw response body returned by the server
     */
    public SeekStormApiException(int statusCode, String responseBody) {
        super("SeekStorm request failed with status " + statusCode + ": " + responseBody);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }

    /**
     * Creates a new exception from an HTTP response.
     *
     * @param response the HTTP response that failed
     */
    public SeekStormApiException(HttpResponse<String> response) {
        this(response.statusCode(), response.body());
    }

    /**
     * Returns the HTTP status code.
     *
     * @return the status code
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Returns the raw response body.
     *
     * @return the response body
     */
    public String getResponseBody() {
        return responseBody;
    }
}