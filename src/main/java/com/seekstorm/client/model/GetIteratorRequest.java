package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

/**
 * Request payload for document iteration.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record GetIteratorRequest(
        /** Optional starting document ID. */ Long documentId,
        /** Number of document IDs to skip. */ Integer skip,
        /** Number of document IDs to take. */ Integer take,
        /** Whether deleted documents should be included. */ Boolean includeDeleted,
        /** Whether to include document payloads in results. */ Boolean includeDocument,
        /** Fields to include in each document. */ List<String> fields) {
}