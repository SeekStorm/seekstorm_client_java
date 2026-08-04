package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Highlight configuration for document snippets.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Highlight(
        /** Field name to highlight. */ String field,
        /** Optional alias for the highlight output. */ String name,
        /** Number of fragments to return. */ Integer fragmentNumber,
        /** Fragment size in characters. */ Integer fragmentSize,
        /** Enables highlight markup in snippet output. */ Boolean highlightMarkup) {
}