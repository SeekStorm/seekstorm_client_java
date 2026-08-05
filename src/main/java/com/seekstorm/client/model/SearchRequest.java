package com.seekstorm.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Objects;

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

    public SearchRequest {
        query = query == null ? "" : query;
        enableEmptyQuery = enableEmptyQuery == null ? false : enableEmptyQuery;
        offset = offset == null ? 0 : offset;
        length = length == null ? 10 : length;
        resultType = resultType == null ? ResultType.TopkCount : resultType;
        realtime = realtime == null ? false : realtime;
        highlights = highlights == null ? List.of() : List.copyOf(highlights);
        fieldFilter = fieldFilter == null ? List.of() : List.copyOf(fieldFilter);
        fields = fields == null ? List.of() : List.copyOf(fields);
        distanceFields = distanceFields == null ? List.of() : List.copyOf(distanceFields);
        queryFacets = queryFacets == null ? List.of() : List.copyOf(queryFacets);
        facetFilter = facetFilter == null ? List.of() : List.copyOf(facetFilter);
        resultSort = resultSort == null ? List.of() : List.copyOf(resultSort);
        queryTypeDefault = queryTypeDefault == null ? QueryType.Intersection : queryTypeDefault;
        queryRewriting = queryRewriting == null ? QueryRewriting.SearchOnly : queryRewriting;
        searchMode = searchMode == null ? SearchMode.Lexical : searchMode;
    }

    /**
     * Creates a simple lexical search request.
     *
     * @param query the search query
     */
    public SearchRequest(String query) {
        this(query, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
    }

    /**
     * Creates a fluent builder with defaults aligned to the C# client.
     *
     * @param query the search query
     * @return builder instance
     */
    public static Builder builder(String query) {
        return new Builder(query);
    }

    public static final class Builder {
        private final String query;
        private JsonNode queryVector;
        private Boolean enableEmptyQuery;
        private Integer offset;
        private Integer length;
        private ResultType resultType;
        private Boolean realtime;
        private List<String> highlights;
        private List<String> fieldFilter;
        private List<String> fields;
        private List<String> distanceFields;
        private List<String> queryFacets;
        private List<String> facetFilter;
        private List<String> resultSort;
        private QueryType queryTypeDefault;
        private QueryRewriting queryRewriting;
        private SearchMode searchMode;

        private Builder(String query) {
            this.query = Objects.requireNonNull(query, "query");
        }

        public Builder queryVector(JsonNode queryVector) {
            this.queryVector = queryVector;
            return this;
        }

        public Builder enableEmptyQuery(boolean enableEmptyQuery) {
            this.enableEmptyQuery = enableEmptyQuery;
            return this;
        }

        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder length(int length) {
            this.length = length;
            return this;
        }

        public Builder resultType(ResultType resultType) {
            this.resultType = resultType;
            return this;
        }

        public Builder realtime(boolean realtime) {
            this.realtime = realtime;
            return this;
        }

        public Builder highlights(List<String> highlights) {
            this.highlights = highlights;
            return this;
        }

        public Builder fieldFilter(List<String> fieldFilter) {
            this.fieldFilter = fieldFilter;
            return this;
        }

        public Builder fields(List<String> fields) {
            this.fields = fields;
            return this;
        }

        public Builder distanceFields(List<String> distanceFields) {
            this.distanceFields = distanceFields;
            return this;
        }

        public Builder queryFacets(List<String> queryFacets) {
            this.queryFacets = queryFacets;
            return this;
        }

        public Builder facetFilter(List<String> facetFilter) {
            this.facetFilter = facetFilter;
            return this;
        }

        public Builder resultSort(List<String> resultSort) {
            this.resultSort = resultSort;
            return this;
        }

        public Builder queryTypeDefault(QueryType queryTypeDefault) {
            this.queryTypeDefault = queryTypeDefault;
            return this;
        }

        public Builder queryRewriting(QueryRewriting queryRewriting) {
            this.queryRewriting = queryRewriting;
            return this;
        }

        public Builder searchMode(SearchMode searchMode) {
            this.searchMode = searchMode;
            return this;
        }

        public SearchRequest build() {
            return new SearchRequest(
                    query,
                    queryVector,
                    enableEmptyQuery,
                    offset,
                    length,
                    resultType,
                    realtime,
                    highlights,
                    fieldFilter,
                    fields,
                    distanceFields,
                    queryFacets,
                    facetFilter,
                    resultSort,
                    queryTypeDefault,
                    queryRewriting,
                    searchMode);
        }
    }
}