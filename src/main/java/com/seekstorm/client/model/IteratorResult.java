package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Result returned by the SeekStorm document iterator.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record IteratorResult(
        /** Number of skipped document IDs. */ Integer skip,
        /** The iterator results. */ List<ResultDocument> results) {

    /**
     * One iterator result item.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ResultDocument(
            /** The document ID. */ String docId,
            /** The document payload, if requested. */ JsonNode doc) {
    }
}