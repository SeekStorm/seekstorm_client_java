package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Request payload for updating a single SeekStorm document.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UpdateDocumentRequest(
        /** The document ID to update. */ Long docId,
        /** The updated JSON document. */ JsonNode document) {
}