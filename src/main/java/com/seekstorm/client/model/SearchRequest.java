package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;

/**
 * Request payload for SeekStorm search queries.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record SearchRequest(
        /** The lexical query string. */ String query,
        /** Optional query vector for vector or hybrid search. */ JsonNode queryVector,
        /** Allows an empty query when true. */ Boolean enableEmptyQuery,
        /** Search offset. */ Integer offset,
        /** Number of results to return. */ Integer length,
        /** Response/result type requested by the server. */ ResultType resultType,
        /** Enables realtime search over uncommitted documents. */ Boolean realtime,
        /** Fields to highlight. */ List<String> highlights,
        /** Field filter expressions. */ List<String> fieldFilter,
        /** Fields to include in the response. */ List<String> fields,
        /** Distance fields. */ List<String> distanceFields,
        /** Facets requested by the query. */ List<String> queryFacets,
        /** Facet filters. */ List<String> facetFilter,
        /** Result sort clauses. */ List<String> resultSort,
        /** Default query type. */ QueryType queryTypeDefault,
        /** Query rewriting mode. */ QueryRewriting queryRewriting,
        /** Search mode such as lexical or hybrid. */ SearchMode searchMode) {

    /**
     * Creates a simple lexical search request.
     *
     * @param query the search query
     */
    public SearchRequest(String query) {
        this(query, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }
}