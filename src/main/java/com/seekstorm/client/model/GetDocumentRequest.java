package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * Request payload for fetching a document with field and highlight options.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetDocumentRequest(
        /** Query terms used for highlighting and relevance hints. */ List<String> queryTerms,
        /** Fields/snippet settings to highlight in the returned document. */ List<Highlight> highlights,
        /** Fields to include in the returned document. */ List<String> fields,
        /** Fields used for distance calculations. */ List<String> distanceFields) {
}