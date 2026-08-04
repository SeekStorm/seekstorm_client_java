package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;

/**
 * Search response returned by SeekStorm.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SearchResponse(
        /** Query execution time in milliseconds. */ Long time,
        /** Original query string. */ String originalQuery,
        /** Normalized query string. */ String query,
        /** Result offset. */ Integer offset,
        /** Result length. */ Integer length,
        /** Number of results returned in the current page. */ Long count,
        /** Total number of matching documents. */ Long countTotal,
        /** Parsed query terms. */ List<String> queryTerms,
        /** Search results. */ List<SearchResultItem> results,
        /** Facet buckets returned by the server. */ Map<String, JsonNode> facets,
        /** Search suggestions. */ List<String> suggestions) {

    /**
     * One search result item.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record SearchResultItem(
            /** The document ID. */ String docId,
            /** The document payload, if requested. */ JsonNode doc) {
    }
}