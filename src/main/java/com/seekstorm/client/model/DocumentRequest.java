package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Request payload for indexing a single document.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DocumentRequest(
        /** Optional document ID supplied by the caller. */ String docId,
        /** The JSON document to index. */ JsonNode document) {
}